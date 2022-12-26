package com.edu.graduationproject.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.edu.graduationproject.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface ForgotPasswordService {
    void updateResetPasswordToken(String token, String email) throws Exception;

    Optional<User> getByResetPasswordToken(String token);

    void updatePassword(User account, String newPass);

    void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException;

    String sendEMailForgotPassword(String token, String email, HttpServletRequest request);

    String resetPassword(String token, String password);
}
