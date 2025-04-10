package naamaantoniouk.mealbudget.AppModule.productPrice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public interface ProductPriceService {
    ProductPrice addProductPrice(ProductPrice productPrice) throws AppException;
    ProductPrice getLatestProductPrice(int productId) throws AppException;
    List<ProductPrice> getProductPriceHistory(int productId, LocalDateTime startDate, LocalDateTime endDate) throws AppException;
    double getAveragePricePerGram(int productId, LocalDateTime startDate, LocalDateTime endDate) throws AppException;
    double getPriceChangePercentage(int productId, LocalDateTime startDate, LocalDateTime endDate) throws AppException;
    
    List<Map<String, Object>> getProductsWithSignificantPriceChanges(LocalDateTime startDate, LocalDateTime endDate, int limit);
    Map<LocalDate, Double> getDailyAveragePrices(int productId, LocalDate startDate, LocalDate endDate) throws AppException;

    Map<Integer, Boolean> updatePricesFromScraper(List<Product> scrapedProducts);

}
