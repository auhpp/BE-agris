package com.agri_supplies_shop.dto.request;

import com.agri_supplies_shop.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String fullName;

    private String userName;

    private String avatar;

    private String password;

    private String phone_number;

    private String email;

    private Gender gender;

    private Date dateOfBirth;

    private String provider;
}
