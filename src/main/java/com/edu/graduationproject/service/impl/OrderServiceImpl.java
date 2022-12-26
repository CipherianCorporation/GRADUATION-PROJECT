package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.entity.OrderDetails;
import com.edu.graduationproject.entity.OrderStatus;
import com.edu.graduationproject.entity.PersonalAccessToken;
import com.edu.graduationproject.entity.Product;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.entity.Voucher;
import com.edu.graduationproject.model.EOrderStatus;
import com.edu.graduationproject.model.EPaypalPaymentMethod;
import com.edu.graduationproject.model.IOrderTypeCount;
import com.edu.graduationproject.model.MailInfo;
import com.edu.graduationproject.repository.OrderDetailRepository;
import com.edu.graduationproject.repository.OrderRepository;
import com.edu.graduationproject.service.MailerService;
import com.edu.graduationproject.service.OrderService;
import com.edu.graduationproject.service.PersonalAccessTokenService;
import com.edu.graduationproject.service.ProductService;
import com.edu.graduationproject.service.UserService;
import com.edu.graduationproject.service.VoucherService;
import com.edu.graduationproject.utils.DateUtils;
import com.edu.graduationproject.utils.URLUtils;
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

    @Autowired
    VoucherService voucherService;

    @Override
    @Transactional(rollbackFor = { Exception.class, Throwable.class, RuntimeException.class })
    public Order create(HttpServletRequest req, Map<String, Object> orderMap) {
        // mapping from json data to appropriate objects
        ObjectMapper mapper = new ObjectMapper();
        Double total = 0.0;
        JsonNode orderJson = mapper.convertValue(orderMap, JsonNode.class);
        Order order = mapper.convertValue(orderMap, Order.class);
        order.setCreatedAt(new Date());
        // mapping order details json array to Java List
        List<OrderDetails> list = mapper
                .convertValue(orderJson.get("order_details"), new TypeReference<List<OrderDetails>>() {
                })
                .stream().peek(o -> o.setOrder(order)).collect(Collectors.toList());
        for (OrderDetails detail : list) {
            if (detail.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }
            Product product = productService.findById(detail.getProduct().getId());
            // set subtracted price if product has sale off
            if (product.getSale_off() != null && product.getSale_off() > 0) {
                detail.setPrice(product.getPrice() * (100 - product.getSale_off()) / 100);
            } else {
                detail.setPrice(product.getPrice());
            }
            total += detail.getPrice() * detail.getQuantity();
            // increment product sold, decrease product stock
            Long oldSold = product.getSold();
            Long oldStock = product.getStock();
            product.setSold(oldSold + detail.getQuantity());
            product.setStock(oldStock < 0 ? 0 : oldStock - detail.getQuantity()); // prevent stock < 0
            detail.setProduct(product);
        }
        // if voucher is present then calculate discount and set voucher isUsed to true
        if (order.getVoucher() != null) {
            Optional<Voucher> optV = voucherService.findById(order.getVoucher().getId());
            if (!optV.isPresent()) {
                optV = voucherService.findByCodeIsNotDeleted(order.getVoucher().getCode());
            }
            if (optV.isPresent()) {
                Voucher v = optV.get();
                v.setIsUsed(true);
                voucherService.update(v.getId(), v); // 1. update voucher
                total -= (total * (v.getValue() / 100));
            }
        }
        if(order.getPayment_method() == EPaypalPaymentMethod.cod) {
            order.setOrderStatus(new OrderStatus(EOrderStatus.processing.name()));
            // add more 20k for shipping fee
            total += 20000;
        } else {
            order.setOrderStatus(new OrderStatus(EOrderStatus.success.name()));
        }
        order.setTotal(total);
        order.setAddress(order.getUser().getAddress());
        Order createdOrder = orderRepo.save(order); // 2. save order
        orderDetailRepo.saveAll(list); // 3. save order details
        this.sendEmailReceipt(createdOrder, req); // 4. send email receipt
        return createdOrder;
    }

    @Override
    public void sendEmailReceipt(Order order, HttpServletRequest request) {
        // create accessToken
        String randomStr = RandomString.make(30);
        String abilities = "DOWNLOAD";
        String downloadLink = URLUtils.getBaseURl(request) + "/rest/orders/download-invoice?accessToken="
                + randomStr + "&orderId=" + order.getId();
        accessTokenService.create(new PersonalAccessToken(randomStr, abilities));
        System.out.println(downloadLink);
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
                Hoặc link sau: %s <br><br>

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
                        downloadLink,
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
        if (orderStatus.equals("cancel")) {
            // increment product sold to 1, decrease product stock to 1
            orderData.getOrder_details().forEach(detail -> {
                Product product = productService.findById(detail.getProduct().getId());
                Long oldSold = product.getSold();
                Long oldStock = product.getStock();
                product.setSold(oldSold < 0 ? 0 : oldSold - detail.getQuantity()); // prevent sold < 0
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
