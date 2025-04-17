package com.agri_supplies_shop.web.rest;


import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.request.StaffSearchRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.ApiResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.StaffResponse;
import com.agri_supplies_shop.service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffResource {
    StaffService staffService;

    @PostMapping("/account")
    public ApiResponse createStaffAccount(@RequestBody StaffRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .result(staffService.createStaffAccount(request))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse search(@RequestParam Map<String, String> request,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        StaffSearchRequest staffRequest = new StaffSearchRequest();
        staffRequest.setFullName(request.get("fullName"));
        staffRequest.setPhoneNumber(request.get("phoneNumber"));
        staffRequest.setEmail(request.get("email"));
        return ApiResponse.<PageResponse<StaffResponse>>builder()
                .code(200)
                .result(staffService.search(staffRequest, page, size))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<StaffResponse> getMyInfo() {
        return ApiResponse.<StaffResponse>builder()
                .code(200)
                .result(staffService.getMyInfo())
                .build();
    }
}
