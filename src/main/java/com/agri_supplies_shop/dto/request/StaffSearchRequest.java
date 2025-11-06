package com.agri_supplies_shop.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffSearchRequest {
    private Long id;
    private String fullName;

    private String phoneNumber;

    private String email;

    private String status;

}
