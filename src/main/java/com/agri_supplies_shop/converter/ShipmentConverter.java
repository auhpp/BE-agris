package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.ShipmentRequest;
import com.agri_supplies_shop.dto.response.ShipmentResponse;
import com.agri_supplies_shop.entity.Shipment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShipmentConverter {
    ModelMapper modelMapper;

    public ShipmentResponse toResponse(Shipment shipment) {
        ShipmentResponse response = modelMapper.map(shipment, ShipmentResponse.class);
        if (shipment.getExpiry() != null) {
            String status = "ACTIVE";
            if (shipment.getExpiry().isBefore(LocalDate.now())) {
                status = "INACTIVE";
            }
            response.setStatus(status);
        }
        if (shipment.getShipmentDetails() != null) {
            Long stock = shipment.getWarehouseDetails().stream().reduce(
                    0L, (res, wd) -> res + wd.getStock(), Long::sum
            );
            response.setQuantity(stock);

        } else {
            response.setQuantity(0L);
        }
        return response;
    }

    public Shipment toEntity(ShipmentRequest request) {
        Shipment shipment = Shipment.builder()
                .name(request.getName())
                .expiry(request.getExpiry())
                .build();
        return modelMapper.map(shipment, Shipment.class);
    }
}
