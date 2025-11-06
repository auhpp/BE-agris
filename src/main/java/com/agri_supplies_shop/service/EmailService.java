package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.OtpValidateRequest;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;

import java.util.concurrent.ExecutionException;

public interface EmailService {
    void sendCreateAccountEmail(String fullName, String email) throws MessagingException, JOSEException;

    Boolean generateOTPForResetPassword(String email);

    Boolean validateOTP(OtpValidateRequest request) throws ExecutionException;
}
