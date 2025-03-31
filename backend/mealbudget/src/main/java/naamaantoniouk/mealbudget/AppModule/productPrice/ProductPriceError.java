package naamaantoniouk.mealbudget.AppModule.productPrice;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum ProductPriceError implements ErrorMessage {
    PRICES_NOT_FOUND(2001, "AppException: No price history found for product ID {0}"),
    PRICES_NOT_FOUND_BETWEEN_DATES(2002, "AppException: No prices found for product ID {0} between dates {1} and {2}"),
    PRICE_CHANGE_ZERO(2003, "AppException: Cannot calculate percentage change from zero initial price for product ID {0}");

    private final int code;
    private final String messageTemplate;

    ProductPriceError(int code, String messageTemplate) {
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
