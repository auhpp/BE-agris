package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.WarehouseRequest;
import com.agri_supplies_shop.dto.response.WarehouseResponse;
import com.agri_supplies_shop.entity.Warehouse;
import com.agri_supplies_shop.repository.WarehouseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseConverter {
    ModelMapper modelMapper;

    WarehouseRepository warehouseRepository;

    public WarehouseResponse toResponse(Warehouse warehouse) {
        WarehouseResponse response = modelMapper.map(warehouse, WarehouseResponse.class);
        Integer cnt = warehouseRepository.countProductQuantity(warehouse.getId());
        if (cnt == null) {
            cnt = 0;
        }
        response.setProductQuantity(cnt);
        if (warehouse.getWarehouseDetails() != null) {
            response.setStock(warehouse.getWarehouseDetails().stream()
                    .map(it -> it.getStock())
                    .reduce(0,
                            (a, b) -> a + b
                    ));
        } else {
            response.setStock(0);
        }
        return response;
    }

    public void toExistedEntity(Warehouse warehouse, WarehouseRequest request) {
        modelMapper.map(request, warehouse);
    }

    public Warehouse toEntity(WarehouseRequest request) {
        return modelMapper.map(request, Warehouse.class);
    }


}
