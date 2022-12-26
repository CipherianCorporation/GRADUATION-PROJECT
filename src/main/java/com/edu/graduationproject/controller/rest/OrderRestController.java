package com.edu.graduationproject.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.entity.OrderDetails;
import com.edu.graduationproject.entity.OrderStatus;
import com.edu.graduationproject.entity.PersonalAccessToken;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.model.EOrderStatus;
import com.edu.graduationproject.model.EPaypalPaymentMethod;
import com.edu.graduationproject.model.IOrderTypeCount;
import com.edu.graduationproject.service.ExportService;
import com.edu.graduationproject.service.OrderService;
import com.edu.graduationproject.service.PersonalAccessTokenService;
import com.edu.graduationproject.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin("*")
@RestController
public class OrderRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderRestController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    ExportService exportService;

    @Autowired
	UserService userService;

    @Autowired
    PersonalAccessTokenService accessTokenService;

    @GetMapping("/rest/orders")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/rest/orders/count")
    public ResponseEntity<Long> getOrdersCount() {
        return ResponseEntity.ok(orderService.getCount());
    }

    @GetMapping("/rest/orders/type-count")
    public ResponseEntity<List<IOrderTypeCount>> getOrdersTypeCount() {
        return ResponseEntity.ok(orderService.getTypeCount());
    }

    @GetMapping("/rest/orders/revenue")
    public ResponseEntity<Double> getOrdersRevenue() {
        return ResponseEntity.ok(orderService.getTotalRevenue());
    }

    @PostMapping("/rest/orders")
    public ResponseEntity<Order> create(HttpServletRequest req, @RequestBody Map<String, Object> orderMap, Authentication auth) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Optional<User> loggedinUser = userService.findByUsername(auth.getName());
            orderMap.put("user", loggedinUser.get());
            orderMap.put("payment_method", EPaypalPaymentMethod.cod);

            JsonNode orderJson = mapper.convertValue(orderMap, JsonNode.class);
            JsonNode total = orderJson.get("total");
            JsonNode address = orderJson.get("address");
            JsonNode user = orderJson.get("user");
            if(!loggedinUser.isPresent()){
                return ResponseEntity.badRequest().build();
            }
            if (total == null || address == null || user == null) {
                return ResponseEntity.badRequest().build();
            }
            if(user != null){
                JsonNode username = user.get("username");
                if(username == null){
                    return ResponseEntity.badRequest().build();
                }
            }
            return ResponseEntity.ok(orderService.create(req, orderMap));
        } catch (Exception e) {
            LOGGER.error("Error while creating order and sending email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/rest/orders/send-email-receipt")
    public ResponseEntity<Object> sendEmailReceipt(HttpServletRequest request, @RequestBody JsonNode orderData) {
        try {
            JsonNode total = orderData.get("total");
            JsonNode address = orderData.get("address");
            JsonNode user = orderData.get("user");
            if (total == null || address == null || user == null) {
                return ResponseEntity.badRequest().build();
            }
            if(user != null){
                JsonNode username = user.get("username");
                if(username == null){
                    return ResponseEntity.badRequest().build();
                }
            }
            // orderService.sendEmailReceipt(orderData, request);
            return ResponseEntity.ok("Send email receipt successfully");
        } catch (Exception e) {
            LOGGER.error("Error while sending email receipt", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Example: GET
    // /rest/orders/download-invoicet?accessToken=s82hxhxqausnwuex23s&orderId=100001
    // Nếu tìm thấy findToken và finđOrder từ requestParam sẽ so sánh abilities nếu
    // là DOWNLOAD mới cho download invoice, sau đó sẽ xóa accessToken
    @GetMapping("/rest/orders/download-invoice")
    public ResponseEntity<String> downloadInvoice(HttpServletResponse response,
            @RequestParam("accessToken") String accessToken, @RequestParam("orderId") Long orderId) {
        try {
            Optional<PersonalAccessToken> findToken = accessTokenService.findByToken(accessToken);
            Optional<Order> findOrder = orderService.findById(orderId);
            if (findToken.isPresent() && findOrder.isPresent()) {
                if (findToken.get().getAbilities().equals("DOWNLOAD")) {
                    exportService.exportInvoice(orderId, response);
                    accessTokenService.deleteById(findToken.get().getId());
                    return new ResponseEntity<>(HttpStatus.OK); // 200
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
                }
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        } catch (IOException e) {
            LOGGER.error("Error while downloading invoice " + orderId, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @GetMapping("/rest/order/list")
    public List<Order> findAllOrder() {
        return orderService.findAll();
    }

    @GetMapping("/rest/order/list/{userId}")
    public List<Order> findAllOrderByUser(@PathVariable("userId") Integer userId) {
        return orderService.findByUserId(userId);
    }

    @GetMapping("/rest/order/detail/{orderId}")
    public List<OrderDetails> findOrderDetail(@PathVariable("orderId") Long orderId) {
        return orderService.findOrderDetailsByOrderId(orderId);
    }

    @GetMapping("/rest/order/sortstatus")
    public List<Order> findOderSortStatus() {
        return orderService.findAllSortStatus();
    }

    @PutMapping("/rest/order/orderstatus/{orderId}")
    public ResponseEntity<Integer> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Order order) {
        try {
            List<OrderDetails> order_details = order.getOrder_details();
            return ResponseEntity.ok(orderService.updateStatus(String.valueOf(order.getOrderStatus().getName()), orderId, order_details));

        } catch (Exception e) {
            LOGGER.error("Error while updating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rest/order/{orderId}")
    public ResponseEntity<List<Order>> findByIdForSearch(@PathVariable("orderId") Long orderId) {
        try {
            return ResponseEntity.ok(orderService.searchByOrderId(orderId));
        } catch (Exception e) {
            LOGGER.error("Error while searching order", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
