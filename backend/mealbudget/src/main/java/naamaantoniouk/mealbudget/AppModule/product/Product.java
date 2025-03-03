package naamaantoniouk.mealbudget.AppModule.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private String img;
    
    //no args
    public Product() {
    }

    // Calculate price per gram >> when building the object
    public void calculatePricePerGram() {
        if (this.unit.equals("kg")) {
            this.price_per_gr = this.price / (this.quantity * 1000);
        } else if (this.unit.equals("gr")) {
            this.price_per_gr = this.price / this.quantity;
        }
    }

    //builder
    //getter setter toString

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
        
        public Builder img(String img) {
            product.img = img;
            return this;
        }
        
        public Product build() {
            // Calculate price per gram when building the object
            product.calculatePricePerGram();
            return product;
        }
    }
    
    public static Builder builder() {
        return new Builder();
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
            // Recalculate price per gram when price changes
            calculatePricePerGram();
        }
    
        public String getUnit() {
            return unit;
        }
    
        public void setUnit(String unit) {
            this.unit = unit;
            // Recalculate price per gram when unit changes
            calculatePricePerGram();
        }
    
        public int getQuantity() {
            return quantity;
        }
    
        public void setQuantity(int quantity) {
            this.quantity = quantity;
            // Recalculate price per gram when quantity changes
            calculatePricePerGram();
        }

        public double getPriceForOneGram() {
            if (this.price_per_gr == 0) {
                calculatePricePerGram();
            }
            return this.price_per_gr;
        }
    
        public double getPrice_per_gr() {
            return price_per_gr;
        }
    
        public void setPrice_per_gr(double price_per_gr) {
            this.price_per_gr = price_per_gr;
        }
    
        public String getImg() {
            return img;
        }
    
        public void setImg(String img) {
            this.img = img;
        }
        
        // toString method
        @Override
        public String toString() {
            return "Product{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    ", unit='" + unit + '\'' +
                    ", quantity=" + quantity +
                    ", price_per_gr=" + price_per_gr +
                    ", img='" + img + '\'' +
                    '}';
        }



}
