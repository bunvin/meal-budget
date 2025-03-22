package naamaantoniouk.mealbudget.AppModule.recipe;

import java.util.List;

import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredientDTO;

public class RecipeCreateRequestDTO {
    private RecipeDTO recipe;
    private List<RecipeIngredientDTO> ingredients;

    public RecipeCreateRequestDTO() {
    }

    public RecipeCreateRequestDTO(RecipeDTO recipe, List<RecipeIngredientDTO> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public RecipeDTO getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDTO recipe) {
        this.recipe = recipe;
    }

    public List<RecipeIngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }
}
