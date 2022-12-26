package com.edu.graduationproject.service;

import javax.mail.MessagingException;

import com.edu.graduationproject.model.MailInfo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


//@EnableAutoConfiguration(exclude = MailerService.class)
public interface MailerService {
    // void send(MailInfo mail)throws MessagingException;
    // void send(String to, String subject, String body) throws MessagingException;
    void send(MailInfo mail) throws MessagingException;

    void send(String to, String subject, String body) throws MessagingException;

    void queue(MailInfo mail);

    void queue(String to, String subject, String body);

}
