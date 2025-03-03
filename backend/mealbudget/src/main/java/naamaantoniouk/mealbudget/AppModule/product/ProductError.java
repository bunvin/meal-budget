package naamaantoniouk.mealbudget.AppModule.product;

import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum ProductError implements ErrorMessage{
    PRODUCT_NOT_FOUND(1001, "AppException: product not found"),
    PRODUCT_ALREADY_EXIST(1002, "AppException: product already exist");

    private final int code;
    private final String message;

    ProductError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
