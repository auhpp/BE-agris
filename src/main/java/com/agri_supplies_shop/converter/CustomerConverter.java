package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.CustomerRequest;
import com.agri_supplies_shop.dto.response.AddressResponse;
import com.agri_supplies_shop.dto.response.CustomerResponse;
import com.agri_supplies_shop.entity.Customer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerConverter {
    ModelMapper modelMapper;

    AddressConverter addressConverter;

    public Customer toEntity(CustomerRequest request) {
        return modelMapper.map(request, Customer.class);
    }

    public void toExistsEntity(CustomerRequest request, Customer customer) {
        modelMapper.map(request, customer);
    }

    public CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = modelMapper.map(customer, CustomerResponse.class);
        response.setUserName(customer.getAccount().getUserName());
        response.setPassword(customer.getAccount().getPassword());
        response.setEmail(customer.getEmail());
        response.setStatus(customer.getStatus().name());
        if (customer.getAddresses() != null) {
            List<AddressResponse> addressResponses = customer.getAddresses().stream().map(
                    addressConverter::toResponse
            ).toList();
            response.setAddresses(addressResponses);
        }
        return response;
    }
}
