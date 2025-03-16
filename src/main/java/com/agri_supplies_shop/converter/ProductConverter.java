package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.enums.Origin;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductConverter {
    ModelMapper modelMapper;

    ProductVariantValueConverter pVariantValueConverter;

    AttributeConverter attributeConverter;

    public Product toEntity(ProductRequest request) {
        //Fix error: destination class have more field than source class
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Product product = modelMapper.map(request, Product.class);
        product.setProductVariantValues(null);
        return product;
    }

    public Product toExistsEntity(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setOrigin(Origin.valueOf(request.getOrigin()));
        product.setProductionDate(request.getProductionDate());
        product.setExpiry(request.getExpiry());
        if (request.getThumbnail() != null) {
            product.setThumbnail(request.getThumbnail());
        }
        return product;
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        response.setOrigin(product.getOrigin().getName());
        //Category
        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .build();
        response.setCategory(categoryResponse);

        //Supplier
        SupplierResponse supplierResponse = SupplierResponse
                .builder()
                .id(product.getSupplier().getId())
                .name(product.getSupplier().getName())
                .build();
        response.setSupplier(supplierResponse);
        // product variant value
        if (product.getProductVariantValues() != null) {
            List<ProductVariantValueResponse> productVariantValueResponses =
                    product.getProductVariantValues().stream().map(
                            it ->
                                    pVariantValueConverter.toResponse(it)
                    ).toList();
            response.setVariants(productVariantValueResponses);
        }
        //attribute
        if (product.getProductAttributeValues() != null) {
            List<AttributeResponse> attributeResponses =
                    product.getProductAttributeValues().stream().map(
                            it ->
                                    attributeConverter.toResponse(it)
                    ).toList();

            response.setAttributes(attributeResponses);
        }

        //image
        if (product.getProductImages() != null) {
            List<ImageResponse> imageResponses = product.getProductImages()
                    .stream().map(
                            it -> ImageResponse.builder().id(it.getId())
                                    .filePath(it.getPath()).build()
                    ).toList();
            response.setImages(imageResponses);
        }
        return response;
    }
}
