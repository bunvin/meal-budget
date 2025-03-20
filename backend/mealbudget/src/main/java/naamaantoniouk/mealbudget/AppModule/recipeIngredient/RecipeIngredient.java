package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.AppModule.recipe.Recipe;

@Entity
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", updatable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", updatable = false)
    private Product product;

    @Column(name = "amount")
    private double amount;
    private String unit; //gr or ml

    @Column(updatable = false)
    private LocalDateTime createDateTime = LocalDateTime.now();
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

    // no-args constructor
    public RecipeIngredient() {
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDateTime = LocalDateTime.now();
    }

// Builder pattern
public static class Builder {
    private RecipeIngredient recipeIngredient = new RecipeIngredient();

    public Builder id(int id) {
        recipeIngredient.id = id;
        return this;
    }

    public Builder recipe(Recipe recipe) {
        recipeIngredient.recipe = recipe;
        return this;
    }

    public Builder product(Product product) {
        recipeIngredient.product = product;
        return this;
    }

    public Builder amount(double amount) {
        recipeIngredient.amount = amount;
        return this;
    }

    public Builder unit(String unit) {
        recipeIngredient.unit = unit;
        return this;
    }

    public RecipeIngredient build() {
        if (recipeIngredient.amount <= 0) {
            throw new IllegalStateException("Amount must be greater than zero.");
        }
        if (recipeIngredient.unit == null || recipeIngredient.unit.trim().isEmpty()) {
            throw new IllegalStateException("Unit cannot be empty.");
        }
        return recipeIngredient;
    }
}

public static Builder builder() {
    return new Builder();
}

// toString method
@Override
public String toString() {
    return "RecipeIngredient{" +
            "id=" + id +
            ", product=" + (product != null ? product.getName() : "null") +
            ", amount=" + amount +
            ", unit='" + unit + '\'' +
            ", createDateTime=" + createDateTime +
            ", modifiedDateTime=" + modifiedDateTime +
            '}';
}

// Getters and Setters
public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public Recipe getRecipe() {
    return recipe;
}

public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
}

public Product getProduct() {
    return product;
}

public void setProduct(Product product) {
    this.product = product;
}

public double getAmount() {
    return amount;
}

public void setAmount(double amount) {
    this.amount = amount;
}

public String getUnit() {
    return unit;
}

public void setUnit(String unit) {
    this.unit = unit;
}

public LocalDateTime getCreateDateTime() {
    return createDateTime;
}

public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
}

}