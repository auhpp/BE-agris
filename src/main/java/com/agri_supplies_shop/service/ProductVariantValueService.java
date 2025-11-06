package com.agri_supplies_shop.service;

import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;

import java.util.List;

public interface ProductVariantValueService {
    ProductVariantValueResponse create(VariantValueRequest variantValueRequest, Long productId);

    Boolean delete(List<Long> ids);

    //    ImageResponse uploadThumbnail(MultipartFile file, Long id) throws IOException;
    List<ProductVariantValueResponse> search(String name);

    Long getStock(Long pVariantId);
}
