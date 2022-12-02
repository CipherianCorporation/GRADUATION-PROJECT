package com.edu.graduationproject.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.edu.graduationproject.model.EPaypalPaymentIntent;
import com.edu.graduationproject.model.EPaypalPaymentMethod;
import com.edu.graduationproject.service.OrderService;
import com.edu.graduationproject.service.PaypalService;
import com.edu.graduationproject.utils.CommonUtils;
import com.edu.graduationproject.utils.URLUtils;
import com.fasterxml.jackson.databind.JsonNode;
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
    private static JsonNode orderJsonNode;

    @Autowired
    private PaypalService paypalService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/paypal")
    @ResponseBody
    public ResponseEntity<Object> pay(
            @RequestBody JsonNode orderData,
            HttpServletRequest request, ModelMap model)
            throws IOException {
        HashMap<String, String> map = new HashMap<>();
        orderJsonNode = orderData;
        Double _total = CommonUtils.convertCurrency("VND", "USD", orderData.get("total").asDouble());
        if (_total == 0.0) {
            return ResponseEntity.ok(CHECKOUT_PAGE_URL);
        }
        String cancelUrl = URLUtils.getBaseURl(request) + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + PAYPAL_SUCCESS_URL;

        try {
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
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            map.put("message", "Error when checkout with Paypal!");
            return ResponseEntity.internalServerError().build();
        }
        map.put("returned_url", CHECKOUT_PAGE_URL);
        return ResponseEntity.ok(map);
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
                orderService.create(orderJsonNode);
                model.addAttribute("message", "You have successfully completed the payment!");
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
