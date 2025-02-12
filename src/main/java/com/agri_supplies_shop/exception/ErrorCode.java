package com.agri_supplies_shop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    CATEGORY_NOT_FOUND(1001, "Category not found", HttpStatus.NOT_FOUND),
    SUPPLIER_NOT_FOUND(1002, "Supplier not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1003, "Product not found", HttpStatus.NOT_FOUND),
    ATTRIBUTE_NOT_FOUND(1004, "Attribute not found", HttpStatus.NOT_FOUND),
    PRODUCT_PRICE_NOT_FOUND(1005, "Product price not found", HttpStatus.NOT_FOUND),
    PRODUCT_VARIANT_VALUE_NOT_FOUND(1006, "Product variant value not found", HttpStatus.NOT_FOUND),
    VARIANT_VALUE_NOT_FOUND(1007, "Variant value not found", HttpStatus.NOT_FOUND),
    USER_EXISTED(1008, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1009, "User not existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1010, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    WRONG_PASSWORD(1011, "Wrong password", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND(1012, "Cart item not found", HttpStatus.BAD_REQUEST),
    DELETE_FAILED(1013, "The product is in one order", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode status;

    ErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
