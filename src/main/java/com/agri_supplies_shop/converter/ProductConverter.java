package com.agri_supplies_shop.converter;

import com.agri_supplies_shop.dto.request.ProductRequest;
import com.agri_supplies_shop.dto.response.*;
import com.agri_supplies_shop.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductConverter {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductVariantValueConverter pVariantValueConverter;

    @Autowired
    private AttributeConverter attributeConverter;

    public Product toEntity(ProductRequest request) {
        //Fix error: destination class have more field than source class
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Product product = modelMapper.map(request, Product.class);
        product.setProductVariantValues(null);
        return product;
    }

    public void toExistsEntity(ProductRequest request, Product product){
        modelMapper.map(request, product);
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
                                    attributeConverter.toAttributeResponse(it)
                    ).toList();

            response.setAttributes(attributeResponses);
        }
        return response;
    }
}
