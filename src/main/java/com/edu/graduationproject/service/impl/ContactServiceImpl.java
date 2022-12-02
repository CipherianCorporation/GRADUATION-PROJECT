package com.edu.graduationproject.service.impl;

import com.edu.graduationproject.model.MailInfo;
import com.edu.graduationproject.service.ContactService;
import com.edu.graduationproject.service.MailerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    MailerService mailerService;

    @Override
    public void sendContact(JsonNode contactData) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setTo("shoeshy.shop@gmail.com");
        mailInfo.setSubject("Liên hệ từ khác hàng!!!");
        String content = "<p>Thông tin khách hàng liên hệ.</p>"
                        + "<p>Họ và tên: "+contactData.get("username")+".</p>"
                        + "<p>Email: "+contactData.get("email")+".</p>"
                        + "<p>Sđt: "+contactData.get("phone")+".</p>"
                        + "<p>Địa chỉ: "+contactData.get("address")+".</p>"
                        + "<p>Tiêu đề: "+contactData.get("title")+".</p>"
                        + "<p>Nội dung: "+contactData.get("content")+".</p>";
        mailInfo.setBody(content);
        mailerService.queue(mailInfo);
    }

}
