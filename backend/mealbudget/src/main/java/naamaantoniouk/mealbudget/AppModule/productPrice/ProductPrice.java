package naamaantoniouk.mealbudget.AppModule.productPrice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import naamaantoniouk.mealbudget.AppModule.product.Product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product_price")
@IdClass(ProductPrice.ProductPriceId.class)
public class ProductPrice {

    @Id
    @Column(name = "product_id")
    private int productId;

    @Id
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private double price;
    private double price_per_gr;
    private String source;

    // No-args constructor
    public ProductPrice() {
    }

    // Builder pattern
    public static class Builder {
        private ProductPrice productPrice = new ProductPrice();

        public Builder productId(int productId) {
            productPrice.productId = productId;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            productPrice.timestamp = timestamp;
            return this;
        }

        public Builder price(double price) {
            productPrice.price = price;
            return this;
        }

        public Builder price_per_gr(double price_per_gr) {
            productPrice.price_per_gr = price_per_gr;
            return this;
        }

        public Builder source(String source) {
            productPrice.source = source;
            return this;
        }

        public ProductPrice build() {
            // Validation logic
            if (productPrice.timestamp == null) {
                productPrice.timestamp = LocalDateTime.now();
            }

            if (productPrice.source == null || productPrice.source.trim().isEmpty()) {
                productPrice.source = "daily_scrape";
            }

            if (productPrice.price <= 0) {
                throw new IllegalStateException("Price must be greater than zero.");
            }

            return productPrice;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Id class for composite primary key
    public static class ProductPriceId implements Serializable {
        private int productId;
        private LocalDateTime timestamp;

        public ProductPriceId() {
        }

        public ProductPriceId(int productId, LocalDateTime timestamp) {
            this.productId = productId;
            this.timestamp = timestamp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductPriceId that = (ProductPriceId) o;
            return productId == that.productId &&
                    Objects.equals(timestamp, that.timestamp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, timestamp);
        }
    }

    // toString method
    @Override
    public String toString() {
        return "ProductPrice{" +
                "productId=" + productId +
                ", timestamp=" + timestamp +
                ", price=" + price +
                ", price_per_gr=" + price_per_gr +
                ", source='" + source + '\'' +
                '}';
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_per_gr() {
        return price_per_gr;
    }

    public void setPrice_per_gr(double price_per_gr) {
        this.price_per_gr = price_per_gr;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
