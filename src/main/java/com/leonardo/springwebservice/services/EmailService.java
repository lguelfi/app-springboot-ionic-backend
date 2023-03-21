package com.leonardo.springwebservice.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.domain.Order;

public interface EmailService {
    
    void sendOrderConfirmationEmail(Order order);
    void sendEmail(SimpleMailMessage msg);
    void sendOrderConfirmationHtmlEmail(Order order);
    void sendHtmlEmail(MimeMessage message);
    void sendNewPasswordEmail(Client client, String newPassword);
}
