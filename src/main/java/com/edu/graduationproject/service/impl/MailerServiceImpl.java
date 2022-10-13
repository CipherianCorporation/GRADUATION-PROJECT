package com.edu.graduationproject.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.model.MailInfo;
import com.edu.graduationproject.service.MailerService;

@Service
public class MailerServiceImpl implements MailerService {
    @Autowired
    private JavaMailSender sender;

    List<MailInfo> list = new ArrayList<>();

    @Override
    public void send(MailInfo mail) throws MessagingException {
        // TODO Auto-generated method stub
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setFrom(mail.getFrom());
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getBody(), true);
        helper.setReplyTo(mail.getFrom());
        String[] cc = mail.getCc();
        if (cc != null && cc.length > 0) {
            helper.setCc(cc);
        }
        String[] bcc = mail.getBcc();
        if (bcc != null && bcc.length > 0) {
            helper.setBcc(bcc);
        }

        List<File> files = mail.getFiles();
        if (files.size() > 0) {
            for (File file : files) {
                helper.addAttachment(file.getName(), file);
            }
        }
        sender.send(message);
    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        // TODO Auto-generated method stub
        this.send(new MailInfo(to, subject, body));
    }

    @Override
    public void queue(MailInfo mail) {
        // TODO Auto-generated method stub
        list.add(mail);
    }

    @Override
    public void queue(String to, String subject, String body) {
        // TODO Auto-generated method stub
        queue(new MailInfo(to, subject, body));
    }

}
