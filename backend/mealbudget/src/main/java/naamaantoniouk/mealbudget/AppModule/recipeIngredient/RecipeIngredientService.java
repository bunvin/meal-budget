package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import java.util.List;

import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public interface RecipeIngredientService {
    //getSingle, update, delete. get all by recipe Id
    RecipeIngredientDTO addRecipeIngredient(int productId, int RecipeId) throws AppException;
    RecipeIngredientDTO getSingleRecipeIngredient(int ingredientId) throws AppException;
    void updateRecipeIngredient(int ingredientId, RecipeIngredientDTO recipeIngredient) throws AppException;
    void deleteRecipeIngredient(int ingredientId) throws AppException;
    List<RecipeIngredientDTO> recipeIngredients(int recipeId);

}
