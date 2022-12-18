package com.edu.graduationproject.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.utils.ExcelExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.graduationproject.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @RequestMapping("/order/checkout")
    public String checkout() {
        return "order/checkout";
    }

    @RequestMapping("/order/list")
    public String list(Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        model.addAttribute("orders", orderService.findByUsername(username));
        return "order/list";
    }

    @RequestMapping("/order/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) throws JsonProcessingException {
        // System.out.println(new
        // ObjectMapper().writeValueAsString(orderService.findById(id).getOrder_details()));
        model.addAttribute("order", orderService.findById(id).get());
        return "order/detail";
    }

    @GetMapping("/order/detail")
    public String viewDetailOrder(){
        return "order/detail";
    }

    @GetMapping("/orders/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Orders" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Order> listOrder = orderService.findAll();
        String sheetName = "Orders";
        ExcelExporter excelExporter = new ExcelExporter(listOrder,sheetName);

        excelExporter.export(response);
    }

    @GetMapping("/order/shipper")
    public String viewDetailForShipper(){
        return "order/order_shipper";
    }
}
