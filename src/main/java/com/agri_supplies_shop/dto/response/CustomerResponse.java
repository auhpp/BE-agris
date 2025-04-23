package com.agri_supplies_shop.dto.response;

import com.agri_supplies_shop.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;

    private String fullName;

    private String userName;

    private String avatar;

    private String password;

    private String phoneNumber;

    private String email;

    private Gender gender;

    private Date dateOfBirth;

    private List<AddressResponse> addresses;
    private String status;

}
