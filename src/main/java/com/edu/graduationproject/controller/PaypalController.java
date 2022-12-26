package com.edu.graduationproject.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.model.EPaypalPaymentIntent;
import com.edu.graduationproject.model.EPaypalPaymentMethod;
import com.edu.graduationproject.service.OrderService;
import com.edu.graduationproject.service.PaypalService;
import com.edu.graduationproject.service.UserService;
import com.edu.graduationproject.utils.CommonUtils;
import com.edu.graduationproject.utils.URLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaypalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaypalController.class);

    public static final String PAYPAL_SUCCESS_URL = "/pay/success";
    public static final String PAYPAL_CANCEL_URL = "/pay/cancel";
    // request mapping in OrderController.java
    public static final String CHECKOUT_PAGE_URL = "/order/checkout";
    public static final String HOMEPAGE_URL = "/order/checkout";
    private static Map<String, Object> _orderMap;
    private static JsonNode _orderJson;
    private static Authentication _auth;
    private static HttpServletRequest _request;
    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private OrderService orderService;

    @Autowired
    UserService userService;

    @PostMapping("/paypal")
    @ResponseBody
    public ResponseEntity<Object> pay(
            @RequestBody Map<String, Object> orderMap,
            HttpServletRequest request, ModelMap model, Authentication auth)
            throws IOException {
        HashMap<String, String> map = new HashMap<>();
        try {
            Optional<User> loggedinUser = userService.findByUsername(auth.getName());
            _orderMap = orderMap;
            _request = request;
            _auth = auth;
            _orderMap.put("user", loggedinUser.get());
            _orderMap.put("payment_method", EPaypalPaymentMethod.paypal);

            JsonNode orderJson = mapper.convertValue(_orderMap, JsonNode.class);
            JsonNode total = orderJson.get("total");
            JsonNode address = orderJson.get("address");
            JsonNode user = orderJson.get("user");
            if (!loggedinUser.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            if (total == null || address == null || user == null) {
                return ResponseEntity.badRequest().build();
            }
            if (user != null) {
                JsonNode username = user.get("username");
                if (username == null) {
                    return ResponseEntity.badRequest().build();
                }
            }
            Double _total = CommonUtils.convertCurrency("VND", "USD",
                    Double.parseDouble(orderMap.get("total").toString()));
            if (_total == 0.0) {
                return ResponseEntity.ok(CHECKOUT_PAGE_URL);
            }
            String cancelUrl = URLUtils.getBaseURl(_request) + PAYPAL_CANCEL_URL;
            String successUrl = URLUtils.getBaseURl(_request) + PAYPAL_SUCCESS_URL;
            Payment payment = paypalService.createPayment(
                    _total,
                    "USD",
                    EPaypalPaymentMethod.paypal,
                    EPaypalPaymentIntent.sale,
                    "Pay for ShoeShy order",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    System.out.println(links.getHref());
                    map.put("returned_url", links.getHref());
                    return ResponseEntity.ok(map);
                }
            }
            map.put("returned_url", CHECKOUT_PAGE_URL);
            return ResponseEntity.ok(map);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            map.put("message", "Error when checkout with Paypal!");
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "Error when checkout with Paypal!");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = PAYPAL_CANCEL_URL)
    public ModelAndView cancelPay(ModelMap model) {
        model.addAttribute("message", "You have canceled the payment!");
        return new ModelAndView("redirect:/", model);
    }

    @GetMapping(value = PAYPAL_SUCCESS_URL)
    public ModelAndView successPay(ModelMap model, @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                Order createdOrder = orderService.create(_request, _orderMap);
                System.out.println(_request.getRequestURI());
                model.addAttribute("message",
                        "Bạn đã thanh toán Paypal thành công, chúng tôi đã gửi email hóa đơn vào hòm thư của bạn!");
                model.addAttribute("isPaymentSuccess", true);
                return new ModelAndView("redirect:/", model);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        return new ModelAndView("redirect:/", model);
    }
}
