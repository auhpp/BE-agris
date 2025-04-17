package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.SupplierRequest;
import com.agri_supplies_shop.dto.response.SupplierResponse;
import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.entity.WarehouseReceipt;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierConverter {
    ModelMapper modelMapper;

    public SupplierResponse toResponse(Supplier supplier) {
        SupplierResponse response = modelMapper.map(supplier, SupplierResponse.class);
        response.setStatus(supplier.getStatus().name());
        response.setContactName(supplier.getContactName());
        List<WarehouseReceipt> warehouseReceipts = supplier.getWarehouseReceipts();
        if (warehouseReceipts != null) {
            Long amount = warehouseReceipts.stream().reduce(
                    0L, (res, curr) -> res + curr.getAmount(), Long::sum
            );
            response.setAmount(amount);
        }
        response.setDebt(supplier.getDebt());

        return response;
    }

    public Supplier toEntity(SupplierRequest supplier) {
        return modelMapper.map(supplier, Supplier.class);
    }
}
