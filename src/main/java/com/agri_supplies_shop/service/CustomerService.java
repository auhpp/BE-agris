package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.CustomerSearchRequest;
import com.agri_supplies_shop.dto.request.PasswordRequest;
import com.agri_supplies_shop.dto.request.CustomerRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.ImageResponse;
import com.agri_supplies_shop.dto.response.PageResponse;
import com.agri_supplies_shop.dto.response.CustomerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    CustomerResponse getMyInfo();
    PageResponse<CustomerResponse> search(CustomerSearchRequest request, int page, int size);
    void deleteUser(List<Long> ids);
    AddressResponse createAndUpdateAddress(AddressRequest request);
    void deleteAddress(Long addressId);
    ImageResponse uploadAvatar(MultipartFile avatar, Long id) throws IOException;
}
