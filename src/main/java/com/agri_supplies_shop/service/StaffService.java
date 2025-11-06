package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.request.StaffSearchRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.StaffResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StaffService {
    StaffResponse getMyInfo();

    AccountResponse createStaffAccount(StaffRequest request);

    StaffResponse updateStaff(Long id, StaffRequest request);

    PageResponse<StaffResponse> search(StaffSearchRequest request, int page, int size);

    ImageResponse uploadAvatar(MultipartFile avatar, Long id) throws IOException;

    Boolean recallStaff(Long staffId);
}
