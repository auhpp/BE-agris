package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;
import com.agri_supplies_shop.dto.response.ShipmentResponse;
import com.agri_supplies_shop.entity.ReceiptDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceiptDetailConverter {
    ModelMapper modelMapper;
    ProductVariantValueConverter variantValueConverter;

    public ReceiptDetailResponse toResponse(ReceiptDetail receiptDetail) {
        ReceiptDetailResponse response = ReceiptDetailResponse.builder()
                .unitPrice(receiptDetail.getImportPrice())
                .id(receiptDetail.getId())
                .build();
        //Shipment
        ProductVariantValueResponse variantValueResponse = variantValueConverter.toResponse(
                receiptDetail.getShipmentDetails().get(0).getShipment().getProductVariantValue());
        response.setProductVariant(variantValueResponse);
        List<ShipmentResponse> shipmentResponses = receiptDetail.getShipmentDetails().stream().map(
                it -> {
                    ShipmentResponse sRes = ShipmentResponse.builder()
                            .name(it.getShipment().getName())
                            .expiry(it.getShipment().getExpiry())
                            .quantity((long) it.getQuantity())
                            .build();
                    return sRes;
                }
        ).toList();
        response.setShipments(shipmentResponses);
        return response;
    }
}
