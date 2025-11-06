package com.agri_supplies_shop.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Long debt;
    private String address;

    private String status;

    private String contactName;
    private Long amount;
}
