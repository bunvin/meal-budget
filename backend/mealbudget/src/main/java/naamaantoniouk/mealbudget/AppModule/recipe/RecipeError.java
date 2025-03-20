package naamaantoniouk.mealbudget.AppModule.recipe;

import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum RecipeError implements ErrorMessage{

    RECIPE_NOT_FOUND(2001, "AppException: recipe not found"),
    RECIPE_ALREADY_EXIST(2002, "AppException: recipe already exist");

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

}
