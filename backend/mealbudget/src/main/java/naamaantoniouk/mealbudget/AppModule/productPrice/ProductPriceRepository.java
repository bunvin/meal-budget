package naamaantoniouk.mealbudget.AppModule.productPrice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, ProductPrice.ProductPriceId> {

    //most recent Product price
    ProductPrice findTopByProductIdOrderByTimestampDesc(int productId);

    // price for a product within dates, ordered by timestamp
    List<ProductPrice> findByProductIdAndTimestampBetweenOrderByTimestampAsc(
            int productId, LocalDateTime startDate, LocalDateTime endDate);

    // past price closest to date
    ProductPrice findTopByProductIdAndTimestampLessThanEqualOrderByTimestampDesc(
            int productId, LocalDateTime timestamp);

    // all product prices
    List<ProductPrice> findByProductIdOrderByTimestampAsc(int productId);

    // all product prices after date
    List<ProductPrice> findByProductIdAndTimestampAfterOrderByTimestampAsc(
            int productId, LocalDateTime timestamp);

    // count existing prices
    long countByProductId(int productId);

    //delete all history by product ID
    void deleteByProductId(int productId);
}