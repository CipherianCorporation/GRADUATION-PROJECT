package com.edu.graduationproject.utils;

import java.awt.Color;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.edu.graduationproject.entity.Order;
import com.edu.graduationproject.entity.OrderDetails;
import com.edu.graduationproject.entity.Product;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class InvoiceExport {
    private Order order;
    private List<OrderDetails> listOrderDetails;
    private NumberFormat numberFormatter = new DecimalFormat("##,###,###.00");

    public InvoiceExport(List<OrderDetails> listOrderDetails, Order order) {
        this.listOrderDetails = listOrderDetails;
        this.order = order;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(Color.BLACK);

        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {

        for (OrderDetails order : listOrderDetails) {
            Product p = order.getProduct();
            Double price = p.getPrice() - (p.getPrice() * (p.getSale_off() / 100));
            table.addCell(String.valueOf(p.getId()));
            table.addCell(order.getProduct().getName());
            table.addCell(String.valueOf(numberFormatter.format(price)));
            table.addCell(String.valueOf(order.getQuantity()));
            table.addCell(String.valueOf(numberFormatter.format(price * order.getQuantity())));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);

        Paragraph p = new Paragraph("INVOICE \n SHOESHY", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph p4 = new Paragraph("\n----------------------------------------------", font);
        p4.setAlignment(Paragraph.ALIGN_CENTER);

        Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font1.setSize(15);

        NumberFormat numberFormatter = new DecimalFormat("##,###,###.00");
        SimpleDateFormat sm = new SimpleDateFormat("MMM dd yyyy");

        String paymentMethod = order.getPayment_method().toString().equals("cod")
                ? order.getPayment_method() + " ( +20,000 d)"
                : order.getPayment_method().toString();

        Paragraph p1 = new Paragraph("Code orders: " + order.getId()
                + "\nRecipient's name: " + order.getUser().getFullname()
                + "\nPhone: " + order.getUser().getPhone()
                + "\nAddress: " + order.getAddress()
                + "\nPayment method: " + paymentMethod
                + "\nOrder date: " + sm.format(order.getCreatedAt()), font1);
        p1.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph p2 = new Paragraph("Total price: " + numberFormatter.format(order.getTotal()), font1);
        p2.setAlignment(Paragraph.ALIGN_RIGHT);

        Paragraph p3 = new Paragraph("\nThank you for shopping at SHOESHY website - " +
                "The best quality shoe store in Vietnam, here is your invoice.", font1);
        p3.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(p);
        document.add(p4);
        document.add(p1);
        
        if (order.getVoucher() != null) {
            Paragraph p5 = new Paragraph("Voucher discount: " + order.getVoucher().getValue() +
                    "% (-" + (numberFormatter.format(order.getTotal() * (order.getVoucher().getValue() / 100))) + ")", font1);
            p5.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(p5);
        }

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] { 1.6f, 3.4f, 3.0f, 1.8f, 2.7f });
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.add(p2);
        document.add(p4);
        document.add(p3);

        document.close();

    }
}
