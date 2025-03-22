package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum RecipeIngredientError implements ErrorMessage{
    RECIPE_INGREDIENT_NOT_FOUND(3001, "AppException: recipe ingredient not found"),
    RECIPE_INGREDIENT_ALREADY_EXIST(3002, "AppException: recipe ingredient already exist"),
    RECIPE_INGREDIENT_AMOUNT_INVALID(3003, "AppException: recipe ingredient amount is invalid"),
    RECIPE_INGREDIENT_IN_RECIPE(3004, "AppException: recipe already has ingredient");

    private final int code;
    private final String message;

    RecipeIngredientError(int code, String message) {
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
