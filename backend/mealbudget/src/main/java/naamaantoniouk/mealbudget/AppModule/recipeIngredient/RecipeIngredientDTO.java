package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

public class RecipeIngredientDTO {
    private int id;
    private String name;
    private int recipeId;
    private int productId;
    private int amount;
    private String unit;
    private double price_per_gr;
    private String img;
    
    // No-args constructor
    public RecipeIngredientDTO() {
    }
    
    // All-args constructor
    public RecipeIngredientDTO(int id, String name, int recipeId, int productId, int amount, String unit, double price_per_gr, String img) {
        this.id = id;
        this.name = name;
        this.recipeId = recipeId;
        this.productId = productId;
        this.amount = amount;
        this.unit = unit;
        this.price_per_gr = price_per_gr;
        this.img = img;
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
        
        public Builder price_per_gr(double price_per_gr) {
            recipeIngredientDTO.price_per_gr = price_per_gr;
            return this;
        }
        
        public Builder img(String img) {
            recipeIngredientDTO.img = img;
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
    
    public double getPrice_per_gr() {
        return price_per_gr;
    }
    
    public String getImg() {
        return img;
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
    
    public void setPrice_per_gr(double price_per_gr) {
        this.price_per_gr = price_per_gr;
    }
    
    public void setImg(String img) {
        this.img = img;
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
                ", price_per_gr=" + price_per_gr +
                ", img='" + img + '\'' +
                '}';
    }
}