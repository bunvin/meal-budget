package naamaantoniouk.mealbudget.AppModule.recipe;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public interface RecipeService {
    RecipeDTO addRecipe(RecipeDTO recipe) throws AppException;
    RecipeDTO getSingleRecipe(int id) throws AppException;
    void updateRecipe(int id, RecipeDTO product) throws AppException;
    void deleteRecipe(int id) throws AppException;
    List<RecipeDTO>getAllRecipies() throws AppException;

    double calcTotalNIS(int recipeId) throws AppException;
    Map<LocalDate, Double> getRecipePriceHistory(int recipeId) throws AppException;
}
