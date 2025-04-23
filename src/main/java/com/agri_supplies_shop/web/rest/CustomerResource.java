package com.agri_supplies_shop.web.rest;

import com.agri_supplies_shop.dto.request.AddressRequest;
import com.agri_supplies_shop.dto.request.CustomerRequest;
import com.agri_supplies_shop.dto.request.CustomerSearchRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.service.CustomerService;
import com.agri_supplies_shop.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerResource {

    CustomerService customerService;
    ImageService imageService;

    @PostMapping
    public ApiResponse<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .result(customerService.createCustomer(request))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<CustomerResponse> getMyInfo() {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .result(customerService.getMyInfo())
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<CustomerResponse>> search(
            CustomerSearchRequest request,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<CustomerResponse>>builder()
                .code(200)
                .result(customerService.search(request, page, size))
                .build();
    }

    @PostMapping("/{id}")
    public ApiResponse<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest request) {
        return ApiResponse.<CustomerResponse>builder()
                .code(200)
                .result(customerService.updateCustomer(id, request))
                .build();
    }

    @DeleteMapping("/{ids}")
    public void deleteUser(@PathVariable(name = "ids") List<Long> ids) {
        customerService.deleteUser(ids);
    }

    @PostMapping("/address")
    public ApiResponse<AddressResponse> createAndUpdateAddress(@RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .code(200)
                .result(customerService.createAndUpdateAddress(request))
                .build();
    }

    @DeleteMapping("/address/{addressId}")
    public void deleteAddress(@PathVariable("addressId") Long addressId) {
        customerService.deleteAddress(addressId);
    }


    @PostMapping("/avatar")
    public ApiResponse<ImageResponse> uploadAvatar(
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {
        return ApiResponse.<ImageResponse>builder()
                .code(200)
                .result(
                        customerService.uploadAvatar(avatar, customerId)
                )
                .build();
    }



}
