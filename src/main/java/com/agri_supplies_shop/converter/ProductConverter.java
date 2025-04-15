package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.entity.ProductVariantValue;
import com.agri_supplies_shop.entity.Shipment;
import com.agri_supplies_shop.entity.WarehouseDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
        if (request.getThumbnail() != null) {
            product.setThumbnail(request.getThumbnail());
        }
        return product;
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        //Category
        CategoryResponse categoryResponse = CategoryResponse
                .builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .build();
        response.setCategory(categoryResponse);

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
        //reserved
        Long reserved = product.getProductVariantValues().stream().reduce(
                0L, (res, pv) -> res + pv.getReserved(),
                Long::sum
        );
        response.setReserved(reserved);

        //stock
        if (product.getProductVariantValues() != null) {
            List<Shipment> shipmentNoExpiry = new ArrayList<>();
            List<Shipment> shipmentHasExpiry = new ArrayList<>();
            for (ProductVariantValue pv : product.getProductVariantValues()) {
                if (pv.getShipments().get(0).getExpiry() != null) {
                    shipmentHasExpiry.addAll(pv.getShipments().stream().filter(
                            sm ->
                                    Optional.ofNullable(sm).map(Shipment::getExpiry).filter(
                                            expiry -> expiry.isAfter(LocalDate.now())
                                    ).isPresent()
                    ).toList());
                } else {
                    shipmentNoExpiry.addAll(pv.getShipments());
                }
            }
            List<Shipment> shipments = Stream.of(shipmentNoExpiry, shipmentHasExpiry)
                    .flatMap(Collection::stream).toList();
            List<WarehouseDetail> warehouseDetails = shipments.stream().flatMap(
                    it -> it.getWarehouseDetails().stream()
            ).toList();
            Long stock = warehouseDetails.stream().reduce(
                    0L, (partialAgeResult, wd) -> partialAgeResult + wd.getStock(), Long::sum
            );
            response.setStock(stock - reserved);
        } else {
            response.setStock(0L);
        }

        return response;
    }
}
