package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.AppModule.recipe.Recipe;

@Entity
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "recipe_id", updatable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", updatable = false)
    private Product product;
    @Column(updatable=false)
    private String name;
    @Column(updatable=false)
    private String unit; //gr or ml
    @Column(updatable=false)
    private double price_per_gr;
    @Column(updatable=false)
    private String img;

    private double amount;

    @Column(updatable = false)
    private LocalDateTime createDateTime = LocalDateTime.now();
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

    // no-args constructor
    public RecipeIngredient() {
    }

    // Method to update price_per_gr from product
    public void updatePriceNameImg() {
        if (this.product != null) {
            this.price_per_gr = this.product.getPrice_per_gr();
            this.name = this.product.getName();
            this.img = this.product.getImg();
            this.unit = this.product.getUnit();
        }
    }


    @PrePersist
    protected void onCreate() {
        this.createDateTime = LocalDateTime.now();
        this.modifiedDateTime = LocalDateTime.now();
        updatePriceNameImg();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDateTime = LocalDateTime.now();
        updatePriceNameImg();
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
            if (product != null) {
                recipeIngredient.price_per_gr = product.getPrice_per_gr();
                recipeIngredient.name = product.getName();
                recipeIngredient.img = product.getImg();
                recipeIngredient.unit = product.getUnit();
            }
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

        public Builder price_per_gr(double price_per_gr) {
            recipeIngredient.price_per_gr = price_per_gr;
            return this;
        }

        public Builder name(String name) {
            recipeIngredient.name = name;
            return this;
        }
        
        public Builder img(String img) {
            recipeIngredient.img = img;
            return this;
        }

        public RecipeIngredient build() {
            if (recipeIngredient.amount <= 0) {
                throw new IllegalStateException("Amount must be greater than zero.");
            }
            if (recipeIngredient.unit == null || recipeIngredient.unit.trim().isEmpty()) {
                throw new IllegalStateException("Unit cannot be empty.");
            }
            // Ensure price_per_gr is updated from product if available
            if (recipeIngredient.product != null && recipeIngredient.price_per_gr == 0) {
                recipeIngredient.price_per_gr = recipeIngredient.product.getPrice_per_gr();
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
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", price_per_gr=" + price_per_gr +
                ", img='" + img + '\'' +
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
        // Update price_per_gr when product is set
        if (product != null) {
            this.price_per_gr = product.getPrice_per_gr();
        }
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

    public double getPrice_per_gr() {
        return price_per_gr;
    }

    public void setPrice_per_gr(double price_per_gr) {
        this.price_per_gr = price_per_gr;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImg() {
        return img;
    }
    
    public void setImg(String img) {
        this.img = img;
    }



}