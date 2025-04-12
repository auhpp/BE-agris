package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailResource {
    EmailService emailService;

    @PostMapping("/{fullName}/{email}")
    public ApiResponse sendConfirmAccountEmail(@PathVariable("fullName") String fullName,
                                               @PathVariable("email") String email
    ) throws MessagingException {
        emailService.sendCreateAccountEmail(fullName, email);
        return ApiResponse.builder()
                .code(200)
                .result("Success")
                .build();
    }
}
