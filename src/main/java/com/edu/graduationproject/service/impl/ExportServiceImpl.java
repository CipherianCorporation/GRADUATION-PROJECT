package com.edu.graduationproject.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.edu.graduationproject.entity.OrderDetails;
import com.edu.graduationproject.utils.InvoiceExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.entity.Product;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.service.ExportService;
import com.edu.graduationproject.service.OrderService;
import com.edu.graduationproject.service.ProductService;
import com.edu.graduationproject.service.UserService;
import com.edu.graduationproject.utils.CsvExporter;
import com.edu.graduationproject.utils.ExcelExporter;
import com.edu.graduationproject.utils.PdfExporter;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Override
    public void exportExcel(Object entity, String fileAndSheetName, HttpServletResponse response) throws IOException {
        response.setContentType("appplication/octet-stream;charset=UTF-8");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileAndSheetName + "_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        if (entity instanceof Product) {
            ExcelExporter<Product> excelExporter = new ExcelExporter<Product>(productService.findAll(),
                    fileAndSheetName);
            excelExporter.export(response);
        } else if (entity instanceof User) {
            ExcelExporter<User> excelExporter = new ExcelExporter<User>(userService.findAll(), fileAndSheetName);
            excelExporter.export(response);
        } else if (entity instanceof Order) {
            ExcelExporter<Order> excelExporter = new ExcelExporter<Order>(orderService.findAll(), fileAndSheetName);
            excelExporter.export(response);
        }
    }

    @Override
    public void exportPDF(Object entity, String fileAndTitleName, HttpServletResponse response) throws IOException {
        response.setContentType("appplication/pdf;charset=UTF-8");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileAndTitleName + "_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        if (entity instanceof Product) {
            PdfExporter<Product> pdfExporter = new PdfExporter<Product>(productService.findAll(), fileAndTitleName);
            pdfExporter.export(response);
        } else if (entity instanceof User) {
            PdfExporter<User> pdfExporter = new PdfExporter<User>(userService.findAll(), fileAndTitleName);
            pdfExporter.export(response);
        } else if (entity instanceof Order) {
            PdfExporter<Order> pdfExporter = new PdfExporter<Order>(orderService.findAll(), fileAndTitleName);
            pdfExporter.export(response);
        }
    }

    @Override
    public void exportCSV(Object entity, String fileAndTitleName, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv;charset=UTF-8");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileAndTitleName + "_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        if (entity instanceof Product) {
            CsvExporter<Product> csvExporter = new CsvExporter<Product>(productService.findAll(), fileAndTitleName);
            csvExporter.export(response.getWriter());
        } else if (entity instanceof User) {
            CsvExporter<User> csvExporter = new CsvExporter<User>(userService.findAll(), fileAndTitleName);
            csvExporter.export(response.getWriter());
        } else if (entity instanceof Order) {
            CsvExporter<Order> csvExporter = new CsvExporter<Order>(orderService.findAll(), fileAndTitleName);
            csvExporter.export(response.getWriter());
        }
    }

    @Override
    public void exportInvoice(Long orderId, HttpServletResponse response) throws IOException {
        response.setContentType("appplication/pdf;charset=UTF-8");
        List<OrderDetails> listOrdersDetails = orderService.findOrderDetailsByOrderId(orderId);
        Order order = orderService.findById(orderId).get();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Invoice_" + order.getId() + ".pdf";
        response.setHeader(headerKey, headerValue);

        InvoiceExport exporter = new InvoiceExport(listOrdersDetails, order);
        exporter.export(response);
    }

}
