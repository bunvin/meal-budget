package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import naamaantoniouk.mealbudget.ErrorHandeling.ErrorMessage;

public enum RecipeIngredientError implements ErrorMessage{
    RECIPE_INGREDIENTS_NOT_FOUND(3000, "AppException: no ingredients in listed to Recipe {0}"),
    RECIPE_INGREDIENT_NOT_FOUND(3001, "AppException: recipe ingredient ID {0} not found"),
    RECIPE_INGREDIENT_ALREADY_EXIST(3002, "AppException: recipe ingredient {0} already exist"),
    RECIPE_INGREDIENT_AMOUNT_INVALID(3003, "AppException: recipe ingredient amount {0} is invalid"),
    RECIPE_INGREDIENT_IN_RECIPE(3004, "AppException: recipe already has ingredient {0}");

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

    public AppException createException(Object... args) {
        return new AppException(this, args);
    }
}
