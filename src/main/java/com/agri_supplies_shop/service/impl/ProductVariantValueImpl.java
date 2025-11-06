package com.agri_supplies_shop.service.impl;


import com.agri_supplies_shop.converter.ProductVariantValueConverter;
import com.agri_supplies_shop.dto.request.VariantValueRequest;
import com.agri_supplies_shop.dto.response.ProductVariantValueResponse;
import com.agri_supplies_shop.entity.*;
import com.agri_supplies_shop.exception.AppException;
import com.agri_supplies_shop.exception.ErrorCode;
import com.agri_supplies_shop.repository.CalculationUnitRepository;
import com.agri_supplies_shop.repository.ProductRepository;
import com.agri_supplies_shop.repository.ProductVariantValueRepository;
import com.agri_supplies_shop.repository.VariantValueRepository;
import com.agri_supplies_shop.service.ImageService;
import com.agri_supplies_shop.service.ProductVariantValueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantValueImpl implements ProductVariantValueService {
    VariantValueRepository variantValueRepository;

    ProductVariantValueRepository productVariantValueRepository;

    ProductVariantValueConverter variantValueConverter;

    ProductRepository productRepository;

    ImageService imageService;

    CalculationUnitRepository calculationUnitRepository;

    @Override
    @Transactional
    public ProductVariantValueResponse create(VariantValueRequest request, Long productId) {
        //Product variant value
        ProductVariantValue productVariantValue;
        if (request.getId() != null) {
            productVariantValue = productVariantValueRepository.findById(request.getId()).orElseThrow(
                    () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
            );
        } else {
            productVariantValue = variantValueConverter.toEntity(request);
        }
        //Calculation unit
        CalculationUnit calculationUnit = calculationUnitRepository.findById(request.getCalculationUnitId()).
                orElseThrow(
                        () -> new AppException(ErrorCode.CALCULATION_NOT_EXISTED)
                );
        productVariantValue.setCalculationUnit(calculationUnit);
        //Create sku
        List<String> variantIds = request.getVariantCombination().stream().map(
                it -> {
                    VariantValue variantValue = variantValueRepository.findByValue(it);
                    System.out.println("");
                    return variantValue.getId().toString();
                }
        ).toList();
        String sku = String.join("-", variantIds);
        productVariantValue.setSku(sku);
        productVariantValue.setProduct(productRepository.findById(productId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)
        ));
        //Save product variant value
        ProductVariantValue variantValueEntity = productVariantValueRepository.save(productVariantValue);
        return variantValueConverter.toResponse(variantValueEntity);
    }

    @Override
    @Transactional
    public Boolean delete(List<Long> ids) {
        ids.forEach(
                it -> {
                    ProductVariantValue productVariant = productVariantValueRepository.findById(it).orElseThrow(
                            () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
                    );
                    if (productVariant.getShipments().isEmpty()
                            && productVariant.getCarts().isEmpty() &&
                            productVariant.getProduct().getProductVariantValues().size() > 1
                    ) {
                        productVariantValueRepository.deleteById(it);
                    } else {
                        throw new AppException(ErrorCode.DELETE_FAILED);
                    }
                }
        );
        return true;
    }

    @Override
    public List<ProductVariantValueResponse> search(String name) {
        if (!Objects.equals(name, "")) {
            List<Product> products = productRepository.findByNameContaining(name);
            List<ProductVariantValueResponse> variantValues = new ArrayList<>();
            products.forEach(
                    it ->
                            variantValues.addAll(it.getProductVariantValues().stream().map(
                                    vartiant ->
                                            variantValueConverter.toResponse(vartiant)
                            ).toList())
            );
            return variantValues;
        }
        return List.of();
    }

    @Override
    public Long getStock(Long pVariantId) {
        ProductVariantValue productVariant = productVariantValueRepository.findById(pVariantId).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
        );
        //Stock
        if (productVariant.getShipments() != null && !productVariant.getShipments().isEmpty()) {
            List<Shipment> shipments;
            if (productVariant.getShipments().get(0).getExpiry() != null) {
                shipments = productVariant.getShipments().stream().filter(
                        sm ->
                                Optional.ofNullable(sm).map(Shipment::getExpiry).filter(
                                        expiry -> expiry.isAfter(LocalDate.now())
                                ).isPresent()
                ).toList();
            } else {
                shipments = productVariant.getShipments();
            }
            List<WarehouseDetail> warehouseDetails = shipments.stream().flatMap(
                    it -> it.getWarehouseDetails().stream()
            ).toList();
            Long stock = warehouseDetails.stream().reduce(
                    0L, (result, wd) -> result + wd.getStock(), Long::sum
            );
            Long stockAvailable = stock;
            if (productVariant.getReserved() != null) {
                stockAvailable = stock - productVariant.getReserved();
            }
            if (stockAvailable < 0) {
                return 0L;
            }
            return stockAvailable;
        } else {
            return 0L;
        }
    }

//    @Override
//    public ImageResponse uploadThumbnail(MultipartFile file, Long id) throws IOException {
//        ProductVariantValue product = productVariantValueRepository.findById(id).orElseThrow(
//                () -> new AppException(ErrorCode.PRODUCT_VARIANT_VALUE_NOT_FOUND)
//        );
//        ImageResponse pathUrl = imageService.saveImage(file);
//        productVariantValueRepository.save(product);
//        return pathUrl;
//    }
}
