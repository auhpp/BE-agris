package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.StaffRequest;
import com.agri_supplies_shop.dto.response.StaffResponse;
import com.agri_supplies_shop.entity.Staff;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffConverter {
    ModelMapper modelMapper;

    public Staff toEntity(StaffRequest request) {
        return modelMapper.map(request, Staff.class);
    }

    public void toExistsEntity(StaffRequest request, Staff staff) {
        modelMapper.map(request, staff);
    }

    public StaffResponse toResponse(Staff staff) {
        StaffResponse response = modelMapper.map(staff, StaffResponse.class);
        if (staff.getAccount() != null) {
            response.setUserName(staff.getAccount().getUserName());
            response.setPassword(staff.getAccount().getPassword());
        }
        response.setEmail(staff.getEmail());
        response.setStatus(staff.getStatus().name());
        return response;
    }
}
