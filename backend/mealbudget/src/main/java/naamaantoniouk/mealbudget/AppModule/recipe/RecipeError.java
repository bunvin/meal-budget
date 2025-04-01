package naamaantoniouk.mealbudget.AppModule.recipe;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum RecipeError implements ErrorMessage{

    RECIPE_NOT_FOUND(3001, "AppException: recipe ID {0} not found"),
    RECIPE_ALREADY_EXIST(3002, "AppException: recipe ID {0} already in use");

    private final int code;
    private final String message;

    RecipeError(int code, String message) {
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

    public AppException createException(Object... args) {
        return new AppException(this, args);
    }

}
