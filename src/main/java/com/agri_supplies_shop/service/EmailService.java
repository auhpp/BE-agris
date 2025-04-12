package com.agri_supplies_shop.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendCreateAccountEmail(String fullName, String email) throws MessagingException;
}
