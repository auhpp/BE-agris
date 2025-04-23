package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.OtpValidateRequest;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.service.EmailService;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailResource {
    EmailService emailService;

    @PostMapping("/{fullName}/{email}")
    public ApiResponse sendConfirmAccountEmail(@PathVariable("fullName") String fullName,
                                               @PathVariable("email") String email
    ) throws MessagingException, JOSEException {
        emailService.sendCreateAccountEmail(fullName, email);
        return ApiResponse.builder()
                .code(200)
                .result("Success")
                .build();
    }

    @PostMapping("/otp/reset_password/{email}")
    public ApiResponse generateOTPForResetPassword(
            @PathVariable("email") String email
    ) {
        Boolean valid = emailService.generateOTPForResetPassword(email);
        return ApiResponse.builder()
                .code(200)
                .result(valid)
                .build();
    }

    @PostMapping("/otp/validate")
    public ApiResponse validateOTP(
            @RequestBody OtpValidateRequest request
            ) throws ExecutionException {
        Boolean valid = emailService.validateOTP(request);
        return ApiResponse.builder()
                .code(200)
                .result(valid)
                .build();
    }


}
