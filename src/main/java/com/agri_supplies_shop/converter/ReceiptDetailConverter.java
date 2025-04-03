package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.response.ReceiptDetailResponse;
import com.agri_supplies_shop.entity.ReceiptDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReceiptDetailConverter {
    ModelMapper modelMapper;
    ProductVariantValueConverter variantValueConverter;

    public ReceiptDetailResponse toResponse(ReceiptDetail receiptDetail) {
        ReceiptDetailResponse response = ReceiptDetailResponse.builder()
                .unitPrice(receiptDetail.getImportPrice())
                .build();
        return response;
    }
}
