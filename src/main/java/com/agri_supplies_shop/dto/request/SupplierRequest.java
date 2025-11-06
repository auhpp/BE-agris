package com.agri_supplies_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequest {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String status;

    private String contactName;
}
