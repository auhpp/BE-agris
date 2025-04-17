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
    DELETE_FAILED(1013, "The product is in one orders", HttpStatus.BAD_REQUEST),
    FILE_NOT_SUPPORTED(1014, "only .jpeg and .png images are supported", HttpStatus.BAD_REQUEST),
    EMAIL_EXIT(1015, "Email existed", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXIT(1016, "Phone number existed", HttpStatus.BAD_REQUEST),
    WAREHOUSE_RECEIPT_NOT_EXISTED(1017, "Warehouse receipt not existed", HttpStatus.BAD_REQUEST),
    WAREHOUSE_NOT_EXISTED(1018, "Warehouse not existed", HttpStatus.BAD_REQUEST),
    CALCULATION_NOT_EXISTED(1019, "Calculation not existed", HttpStatus.BAD_REQUEST),
    SHIPMENT_NOT_EXISTED(1020, "Shipment not existed", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_EXISTED(1021, "Account not existed", HttpStatus.BAD_REQUEST),
    STAFF_NOT_EXISTED(1022, "Staff not existed", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(1023, "Orders not existed", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_INVENTORY(1024, "Insufficient inventory not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1025, "Category existed", HttpStatus.BAD_REQUEST),
    CALCULATION_EXISTED(1026, "Calculation existed", HttpStatus.BAD_REQUEST),
    PAYMENT_REASON_NOT_EXISTED(1027, "Payment reason not existed", HttpStatus.BAD_REQUEST),
    PAYEE_TYPE_NOT_EXISTED(1028, "Payee type reason not existed", HttpStatus.BAD_REQUEST),
    PAYMENT_SLIP_NOT_VALID(1029, "Payment slip not valid", HttpStatus.BAD_REQUEST),
    PAYMENT_REASON_EXISTED(1030, "Payment reason existed", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatusCode status;

    ErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
