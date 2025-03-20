package naamaantoniouk.mealbudget.AppModule.recipe;

import java.util.ArrayList;
import java.util.List;

import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredient;

public class RecipeDTO {
    private int id;
    private String name;
    private int servings;
    private Difficulty difficulty;
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    // No-args constructor
    public RecipeDTO() {
    }
    
    // All-args constructor
    public RecipeDTO(int id, String name, int servings, Difficulty difficulty, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.difficulty = difficulty;
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
    }

    // Builder pattern
    public static class Builder {
        private RecipeDTO recipeDTO = new RecipeDTO();

        public Builder id(int id) {
            recipeDTO.id = id;
            return this;
        }

        public Builder name(String name) {
            recipeDTO.name = name;
            return this;
        }

        public Builder servings(int servings) {
            recipeDTO.servings = servings;
            return this;
        }

        public Builder difficulty(Difficulty difficulty) {
            recipeDTO.difficulty = difficulty;
            return this;
        }

        public Builder ingredients(List<RecipeIngredient> ingredients) {
            recipeDTO.ingredients = new ArrayList<>(ingredients);
            return this;
        }

        public RecipeDTO build() {
            return recipeDTO;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients != null ? new ArrayList<>(ingredients) : new ArrayList<>();
    }
    
    // Helper methods for ingredients
    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }
    
    public void removeIngredient(RecipeIngredient ingredient) {
        this.ingredients.remove(ingredient);
    }
    
    // toString method
    @Override
    public String toString() {
        return "RecipeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", difficulty=" + difficulty +
                ", ingredients=" + ingredients.size() +
                '}';
    }
}