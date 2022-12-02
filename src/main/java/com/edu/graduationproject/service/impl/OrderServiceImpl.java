package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.entity.OrderDetails;
import com.edu.graduationproject.entity.PersonalAccessToken;
import com.edu.graduationproject.entity.Product;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.model.IOrderTypeCount;
import com.edu.graduationproject.model.MailInfo;
import com.edu.graduationproject.repository.OrderDetailRepository;
import com.edu.graduationproject.repository.OrderRepository;
import com.edu.graduationproject.service.MailerService;
import com.edu.graduationproject.service.OrderService;
import com.edu.graduationproject.service.PersonalAccessTokenService;
import com.edu.graduationproject.service.ProductService;
import com.edu.graduationproject.service.UserService;
import com.edu.graduationproject.utils.CommonUtils;
import com.edu.graduationproject.utils.DateUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bytebuddy.utility.RandomString;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    PersonalAccessTokenService accessTokenService;

    @Autowired
    UserService userService;

    @Autowired
    OrderDetailRepository orderDetailRepo;

    @Autowired
    ProductService productService;

    @Autowired
    MailerService mailerService;

    @Override
    public Order create(JsonNode orderData) {
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.convertValue(orderData, Order.class);
        User user = userService.findByUsername(order.getUser().getUsername()).get();
        order.setCreatedAt(new Date());
        order.setUser(user);
        orderRepo.save(order);
        List<OrderDetails> list = mapper
                .convertValue(orderData.get("order_details"), new TypeReference<List<OrderDetails>>() {
                })
                .stream().peek(o -> o.setOrder(order)).collect(Collectors.toList());

        // increment product sold to 1, decrease product stock to 1
        list.forEach((detail) -> {
            Product product = productService.findById(detail.getProduct().getId());
            Long oldSold = product.getSold();
            Long oldStock = product.getStock();
            product.setSold(oldSold + detail.getQuantity());
            product.setStock(oldStock - detail.getQuantity());
        });
        orderDetailRepo.saveAll(list);
        return order;
    }

    @Override
    public void sendEmailReceipt(JsonNode orderData, HttpServletRequest request) {
        Order order = new ObjectMapper().convertValue(orderData, Order.class);

        // create accessToken
        String randomStr = RandomString.make(30);
        String abilities = "DOWNLOAD";
        String downloadLink = CommonUtils.getSiteURL(request) + "/rest/orders/download-invoice?accessToken="
                + randomStr + "&orderId=" + order.getId();
        accessTokenService.create(new PersonalAccessToken(randomStr, abilities));

        MailInfo mailInfo = new MailInfo();
        String recipientEmail = order.getUser().getEmail();
        mailInfo.setTo(recipientEmail);
        mailInfo.setSubject("ShoeShy - Hóa đơn mua hàng - #" + order.getId());
        String content = """
                Xin chào, %s <br>

                Cảm ơn bạn đã mua hàng tại website ShoeShy - Cửa hàng giày dép chất lượng nhất Việt Nam, dưới
                đây là hóa đơn của bạn <br><br>

                Mã số đơn: %s <br>
                Tên khách hàng: %s <br>
                SDT: %s <br>
                Địa chỉ giao hàng: %s <br>
                Phương thức thanh toán: %s <br>
                Ngày đặt: %s <br>
                Tổng số tiền: đ <strong> %s </strong> <br><br>

                Nhấn vào <a href="%s">ĐÂY</a> để download hóa đơn <br><br>

                Hân hạnh, <br>
                ShoeShy Team <br>
                """
                .formatted(
                        order.getUser().getFullname(),
                        order.getId().toString(),
                        order.getUser().getFullname(),
                        order.getUser().getPhone(),
                        order.getAddress(),
                        order.getPayment_method().toString().toUpperCase(),
                        DateUtils.formatDateTime(order.getCreatedAt()),
                        String.format("%,.0f", order.getTotal()),
                        downloadLink);
        mailInfo.setBody(content);
        mailerService.queue(mailInfo);

    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepo.findById(id);
    }

    @Override
    public List<Order> findByUsername(String username) {
        return orderRepo.findByUsername(username);
    }

    @Override
    public List<Order> findAll() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<OrderDetails> findOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepo.findOrderDetailsByOrderId(orderId);
    }

    @Override
    public List<Order> findAllSortStatus() {
        return orderRepo.findSortWithStatus();
    }

    @Override
    @Transactional(rollbackFor = { Exception.class, Throwable.class })
    public int updateStatus(String orderStatus, Long orderId, List<OrderDetails> orderDetails) {
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        Order orderData = orderOpt.get();
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.convertValue(orderData, Order.class);
        if (orderStatus.equals("cancel")) {
            List<OrderDetails> list = mapper
                    .convertValue(orderData.getOrder_details(), new TypeReference<List<OrderDetails>>() {
                    })
                    .stream().peek(o -> o.setOrder(order)).collect(Collectors.toList());

            // increment product sold to 1, decrease product stock to 1
            list.forEach((detail) -> {
                Product product = productService.findById(detail.getProduct().getId());
                Long oldSold = product.getSold();
                Long oldStock = product.getStock();
                product.setSold(oldSold - detail.getQuantity());
                product.setStock(oldStock + detail.getQuantity());
            });
        }
        return orderRepo.updateStatus(orderStatus, orderId);
    }

    @Override
    public List<Order> searchByOrderId(Long orderId) {
        return orderRepo.searchByOrderId(orderId);
    }

    @Override
    public Long getCount() {
        return orderRepo.getCount();
    }

    @Override
    public Double getTotalRevenue() {
        return orderRepo.getTotalRevenue();
    }

    @Override
    public List<IOrderTypeCount> getTypeCount() {
        return orderRepo.getTypeCount();
    }
}
