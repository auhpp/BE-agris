package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.request.StaffSearchRequest;
import com.agri_supplies_shop.dto.response.AccountResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.StaffResponse;

public interface StaffService {

    AccountResponse createStaffAccount(StaffRequest request);

    PageResponse<StaffResponse> search(StaffSearchRequest request, int page, int size);
}
