package naamaantoniouk.mealbudget.AppModule.product;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum ProductError implements ErrorMessage {
    PRODUCT_NOT_FOUND(1001, "AppException: Product with ID {0} not found"),
    PRODUCT_ALREADY_EXIST(1002, "AppException: Product with name '{0}' already exists"),
    PRODUCT_INVALID_PRICE(1003, "AppException: Invalid price {0} for product ID {1}"),
    PRODUCT_INVALID_QUANTITY(1004, "AppException: Invalid quantity {0} for product ID {1}"),
    PRODUCT_INVALID_CATEGORY(1005, "AppException: Invalid category '{0}' for product ID {1}"),
    PRODUCT_DATABASE_ERROR(1006, "AppException: Database error while processing product ID {0}: {1}");

    private final int code;
    private final String messageTemplate;

    ProductError(int code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return messageTemplate;
    }

    public AppException createException(Object... args) {
        return new AppException(this, args);
    }
}
