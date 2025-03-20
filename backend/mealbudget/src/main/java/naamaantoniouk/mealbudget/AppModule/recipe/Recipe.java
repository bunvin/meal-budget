package naamaantoniouk.mealbudget.AppModule.recipe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredient;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int servings;
    
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    //product : quantity in gr, Note: in front order by category id in recipe
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createDateTime = LocalDateTime.now();
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

    //no args
    public Recipe() {
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDateTime = LocalDateTime.now();
    }

    public static class Builder {
        private Recipe recipe = new Recipe();

        public Builder id(int id) {
            recipe.id = id;
            return this;
        }

        public Builder name(String name) {
            recipe.name = name;
            return this;
        }

        public Builder servings(int servings) {
            recipe.servings = servings;
            return this;
        }

        public Builder difficulty(Difficulty difficulty) {
            recipe.difficulty = difficulty;
            return this;
        }

        public Builder ingredients(List<RecipeIngredient> ingredients) {
            recipe.ingredients = new ArrayList<>(ingredients);
            return this;
        }

        public Recipe build() {
            if (recipe.name == null || recipe.name.trim().isEmpty()) {
                throw new IllegalStateException("Recipe name cannot be empty.");
            }
            if (recipe.servings <= 0) {
                throw new IllegalStateException("Servings must be greater than zero.");
            }
            return recipe;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // toString method
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", difficulty=" + difficulty +
                ", ingredients=" + ingredients.size() +
                '}';
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

}
