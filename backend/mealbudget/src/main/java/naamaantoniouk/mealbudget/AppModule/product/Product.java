package naamaantoniouk.mealbudget.AppModule.product;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(updatable=false)
    private String name;

    private double price;
    private String unit;
    private int quantity; //how many to price
    private double price_per_gr;
    private String selling_method;
    private String image_url;
    private int min_gr_unit = 100;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(updatable = false)
    private LocalDateTime createDateTime = LocalDateTime.now();
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

    //no args
    public Product() {
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDateTime = LocalDateTime.now();
    }

    // Calculate price per gram >> when building the object
    public void calculatePricePerGram() {
        if (this.unit.equals("kg")) {
            this.price_per_gr = this.price / (this.quantity * 1000);
        } else if (this.unit.equals("gr")) {
            this.price_per_gr = this.price / this.quantity;
        }
    }

    public int getMin_gr_unit() {
        return min_gr_unit;
    }

    public void setMin_gr_unit(int min_gr_unit) {
        this.min_gr_unit = min_gr_unit;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    //builder
    //getter setter toString
    // Builder pattern
    public static class Builder {
        private Product product = new Product();

        public Builder id(int id) {
            product.id = id;
            return this;
        }

        public Builder name(String name) {
            product.name = name;
            return this;
        }

        public Builder price(double price) {
            product.price = price;
            return this;
        }

        public Builder unit(String unit) {
            product.unit = unit;
            return this;
        }

        public Builder quantity(int quantity) {
            product.quantity = quantity;
            return this;
        }

        public Builder selling_method(String selling_method) {
            product.selling_method = selling_method;
            return this;
        }

        public Builder image_url(String img) {
            product.image_url = img;
            return this;
        }

        public Builder min_gr_unit(int weight){
            product.min_gr_unit = weight;
            return this;
        }

        public Builder category(Category category){
            product.category = category;
            return this;
        }

        public Product build() {
            //validate before calculation
            if (product.price <= 0) {
                throw new IllegalStateException("Price must be greater than zero.");
            }
            if (product.quantity <= 0) {
                throw new IllegalStateException("Quantity must be greater than zero.");
            }
            product.calculatePricePerGram();
            return product;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // toString method
    @Override
    public String toString() {
        return "Product{" +
                "Category: " + category +        
                ",id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", quantity=" + quantity +
                ", price_per_100gr=" + price_per_gr*100 +
                ", selling_method='" + selling_method + '\'' +
                ", image_url='" + image_url + '\'' +
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice_per_gr() {
        return price_per_gr;
    }

    public void setPrice_per_gr(double price_per_gr) {
        this.price_per_gr = price_per_gr;
    }

    public String getSelling_method() {
        return selling_method;
    }

    public void setSelling_method(String selling_method) {
        this.selling_method = selling_method;
    }

    @JsonProperty("image_url")
    public String getImg() {
        return image_url;
    }
    
    @JsonProperty("image_url")
    public void setImg(String img) {
        this.image_url = img;
    }
        
    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }
}