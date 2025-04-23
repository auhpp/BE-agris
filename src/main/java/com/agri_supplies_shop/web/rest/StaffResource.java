package com.agri_supplies_shop.web.rest;


import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.request.StaffSearchRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.service.StaffService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ApiResponse search(StaffSearchRequest request,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {

        return ApiResponse.<PageResponse<StaffResponse>>builder()
                .code(200)
                .result(staffService.search(request, page, size))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<StaffResponse> getMyInfo() {
        return ApiResponse.<StaffResponse>builder()
                .code(200)
                .result(staffService.getMyInfo())
                .build();
    }

    @PostMapping("/{id}")
    public ApiResponse<StaffResponse> updateStaff(@PathVariable("id") Long id, @RequestBody StaffRequest request) {
        return ApiResponse.<StaffResponse>builder()
                .code(200)
                .result(staffService.updateStaff(id, request))
                .build();
    }

    @PostMapping("/avatar")
    public ApiResponse<ImageResponse> uploadAvatar(
            @RequestParam(value = "staffId", required = false) Long staffId,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {
        return ApiResponse.<ImageResponse>builder()
                .code(200)
                .result(
                        staffService.uploadAvatar(avatar, staffId)
                )
                .build();
    }

    @DeleteMapping("/recall")
    public ApiResponse<Boolean> uploadAvatar(
            @RequestParam(value = "staffId", required = false) Long staffId
    ) throws IOException {
        return ApiResponse.<Boolean>builder()
                .code(200)
                .result(
                        staffService.recallStaff(staffId)
                )
                .build();
    }
}
