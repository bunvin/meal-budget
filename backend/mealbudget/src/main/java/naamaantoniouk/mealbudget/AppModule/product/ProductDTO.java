package naamaantoniouk.mealbudget.AppModule.product;

public class ProductDTO {
    private int id;
    private String name;
    private Category category;
    private double price_per_gr;
    private String unit;
    private int min_gr_unit = 100;
    private String image_url;

        // No-args constructor
        public ProductDTO() {
        }
        
        // All-args constructor
        public ProductDTO(int id, String name, Category category, double price_per_gr, String unit, int min_gr_unit, String image_url) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price_per_gr = price_per_gr;
            this.unit = unit;
            this.min_gr_unit = min_gr_unit;
            this.image_url = image_url;
        }
    
        // Builder pattern
        public static class Builder {
            private ProductDTO productDTO = new ProductDTO();
    
            public Builder id(int id) {
                productDTO.id = id;
                return this;
            }
    
            public Builder name(String name) {
                productDTO.name = name;
                return this;
            }
    
            public Builder category(Category category) {
                productDTO.category = category;
                return this;
            }
    
            public Builder price_per_gr(double price_per_gr) {
                productDTO.price_per_gr = price_per_gr;
                return this;
            }
    
            public Builder unit(String unit) {
                productDTO.unit = unit;
                return this;
            }
    
            public Builder min_gr_unit(int min_gr_unit) {
                productDTO.min_gr_unit = min_gr_unit;
                return this;
            }
    
            public Builder image_url(String image_url) {
                productDTO.image_url = image_url;
                return this;
            }
    
            public ProductDTO build() {
                return productDTO;
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
    
        public Category getCategory() {
            return category;
        }
    
        public double getPrice_per_gr() {
            return price_per_gr;
        }
    
        public String getUnit() {
            return unit;
        }
    
        public int getMin_gr_unit() {
            return min_gr_unit;
        }
    
        public String getImage_url() {
            return image_url;
        }
        
        // Setters
        public void setId(int id) {
            this.id = id;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public void setCategory(Category category) {
            this.category = category;
        }
    
        public void setPrice_per_gr(double price_per_gr) {
            this.price_per_gr = price_per_gr;
        }
    
        public void setUnit(String unit) {
            this.unit = unit;
        }
    
        public void setMin_gr_unit(int min_gr_unit) {
            this.min_gr_unit = min_gr_unit;
        }
    
        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
        
    // toString method
    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price_per_gr=" + price_per_gr +
                ", unit='" + unit + '\'' +
                ", min_gr_unit=" + min_gr_unit +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
