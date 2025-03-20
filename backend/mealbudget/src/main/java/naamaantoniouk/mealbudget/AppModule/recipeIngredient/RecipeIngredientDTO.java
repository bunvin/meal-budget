package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

public class RecipeIngredientDTO {
    private int id;
    private String name;
    private int recipeId;
    private int productId;
    private int amount;
    private String unit;

    // No-args constructor
    public RecipeIngredientDTO() {
    }
    
    // All-args constructor
    public RecipeIngredientDTO(int id, String name, int recipeId, int productId, int amount, String unit) {
        this.id = id;
        this.name = name;
        this.recipeId = recipeId;
        this.productId = productId;
        this.amount = amount;
        this.unit = unit;
    }

    // Builder pattern
    public static class Builder {
        private RecipeIngredientDTO recipeIngredientDTO = new RecipeIngredientDTO();

        public Builder id(int id) {
            recipeIngredientDTO.id = id;
            return this;
        }

        public Builder name(String name) {
            recipeIngredientDTO.name = name;
            return this;
        }

        public Builder recipeId(int recipeId) {
            recipeIngredientDTO.recipeId = recipeId;
            return this;
        }

        public Builder productId(int productId) {
            recipeIngredientDTO.productId = productId;
            return this;
        }

        public Builder amount(int amount) {
            recipeIngredientDTO.amount = amount;
            return this;
        }

        public Builder unit(String unit) {
            recipeIngredientDTO.unit = unit;
            return this;
        }

        public RecipeIngredientDTO build() {
            return recipeIngredientDTO;
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

    public int getRecipeId() {
        return recipeId;
    }

    public int getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    // toString method
    @Override
    public String toString() {
        return "RecipeIngredientDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", recipeId=" + recipeId +
                ", productId=" + productId +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                '}';
    }
}