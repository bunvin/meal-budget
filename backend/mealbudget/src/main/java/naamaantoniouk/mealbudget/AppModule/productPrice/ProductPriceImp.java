package naamaantoniouk.mealbudget.AppModule.productPrice;

import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.AppModule.product.ProductError;
import naamaantoniouk.mealbudget.AppModule.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductPriceImp implements ProductPriceService{
    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductPrice addProductPrice(ProductPrice productPrice) throws AppException {
        if (!productRepository.existsById(productPrice.getProductId())) {
            throw ProductError.PRODUCT_NOT_FOUND.createException(productPrice.getProductId());
        }
        // Ensure timestamp is set
        if (productPrice.getTimestamp() == null) {
            productPrice.setTimestamp(LocalDateTime.now());
        }
        return productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice getLatestProductPrice(int productId) throws AppException {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw ProductError.PRODUCT_NOT_FOUND.createException(productId);
        }
        // Get the latest price entry
        ProductPrice latestPrice = productPriceRepository.findTopByProductIdOrderByTimestampDesc(productId);
        if (latestPrice == null) {
            throw ProductPriceError.PRICES_NOT_FOUND.createException(productId);
        }
        return latestPrice;
    }

    @Override
    public List<ProductPrice> getProductPriceHistory(int productId, LocalDateTime startDate, LocalDateTime endDate) throws AppException {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw ProductError.PRODUCT_NOT_FOUND.createException(productId);
        }

        List<ProductPrice> priceHistory = productPriceRepository.findByProductIdAndTimestampBetweenOrderByTimestampAsc(
                productId, startDate, endDate);

        if (priceHistory.isEmpty()) {
            // Return empty list instead of throwing exception
            System.out.println("No price history found for product " + productId + " between " + startDate + " and " + endDate);
        }

        return priceHistory;
    }

    @Override
    public double getAveragePricePerGram(int productId, LocalDateTime startDate, LocalDateTime endDate) throws AppException {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw new AppException(ProductError.PRODUCT_NOT_FOUND);
        }

        List<ProductPrice> priceHistory = productPriceRepository.findByProductIdAndTimestampBetweenOrderByTimestampAsc(
                productId, startDate, endDate);

        if (priceHistory.isEmpty()) {
            throw new AppException(ProductPriceError.PRICES_NOT_FOUND_BETWEEN_DATES);
        }

        return priceHistory.stream()
                .mapToDouble(ProductPrice::getPrice_per_gr)
                .average()
                .orElse(0.0); // Default to 0 if calculation fails
    }

    @Override
    public double getPriceChangePercentage(int productId, LocalDateTime startDate, LocalDateTime endDate) throws AppException {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw new AppException(ProductError.PRODUCT_NOT_FOUND);
        }

        // Get price at start date (closest)
        ProductPrice startPrice = productPriceRepository.findTopByProductIdAndTimestampLessThanEqualOrderByTimestampDesc(
                productId, startDate);

        // Get price at end date (closest)
        ProductPrice endPrice = productPriceRepository.findTopByProductIdAndTimestampLessThanEqualOrderByTimestampDesc(
                productId, endDate);

        if (startPrice == null || endPrice == null) {
            throw new AppException(ProductPriceError.PRICES_NOT_FOUND_BETWEEN_DATES);
        }

        double initialPrice = startPrice.getPrice_per_gr();
        double finalPrice = endPrice.getPrice_per_gr();

        if (initialPrice == 0) {
            throw ProductPriceError.PRICE_CHANGE_ZERO.createException(productId);
        }

        return ((finalPrice - initialPrice) / initialPrice) * 100;
    }

    @Override
    public Map<Integer, Boolean> updatePricesFromScraper(List<Product> scrapedProducts) {
        Map<Integer, Boolean> updateResults = new HashMap<>();

        for (Product scrapedProduct : scrapedProducts) {
            try {
                // Find existing product
                Product existingProduct = productRepository.findById(scrapedProduct.getId()).orElse(null);

                if (existingProduct != null) {
                    boolean priceChanged = isPriceChanged(existingProduct, scrapedProduct);

                    // Only add a new price entry if the price has changed
                    if (priceChanged) {
                        // Create and save new price entry
                        ProductPrice newPriceEntry = ProductPrice.builder()
                                .productId(existingProduct.getId())
                                .timestamp(LocalDateTime.now())
                                .price(scrapedProduct.getPrice())
                                .price_per_gr(scrapedProduct.getPrice_per_gr())
                                .source("daily_scrape")
                                .build();

                        productPriceRepository.save(newPriceEntry);

                        // Update product with new price
                        existingProduct.setPrice(scrapedProduct.getPrice());
                        existingProduct.setPrice_per_gr(scrapedProduct.getPrice_per_gr());
                        productRepository.save(existingProduct);

                        updateResults.put(existingProduct.getId(), true);
                    } else {
                        updateResults.put(existingProduct.getId(), false);
                    }
                }
            } catch (Exception e) {
                // Log error and continue with next product
                System.err.println("Error updating price for product ID: " + scrapedProduct.getId() + " - " + e.getMessage());
                updateResults.put(scrapedProduct.getId(), false);
            }
        }

        return updateResults;
    }

    @Override
    public List<Map<String, Object>> getProductsWithSignificantPriceChanges(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        List<Map<String, Object>> result = new ArrayList<>();

        // Get all products
        List<Product> allProducts = productRepository.findAll();

        for (Product product : allProducts) {
            try {
                double changePercentage = getPriceChangePercentage(product.getId(), startDate, endDate);

                Map<String, Object> productChange = new HashMap<>();
                productChange.put("product", product);
                productChange.put("changePercentage", changePercentage);

                result.add(productChange);
            } catch (AppException e) {
                // Skip products with insufficient price data
                continue;
            }
        }

        // Sort by absolute percentage change (largest first)
        result.sort((a, b) -> {
            double changeA = Math.abs((Double) a.get("changePercentage"));
            double changeB = Math.abs((Double) b.get("changePercentage"));
            return Double.compare(changeB, changeA);
        });

        // Return top N products
        return result.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public Map<LocalDate, Double> getDailyAveragePrices(int productId, LocalDate startDate, LocalDate endDate) throws AppException {
        // Validate product exists
        if (!productRepository.existsById(productId)) {
            throw new AppException(ProductError.PRODUCT_NOT_FOUND);
        }

        // Convert LocalDate to LocalDateTime for repository query
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Get all price entries within range
        List<ProductPrice> priceHistory = productPriceRepository.findByProductIdAndTimestampBetweenOrderByTimestampAsc(
                productId, startDateTime, endDateTime);

        if (priceHistory.isEmpty()) {
            // Return empty map instead of throwing exception
            return new HashMap<>();
        }

        // Group by date and calculate average
        Map<LocalDate, Double> dailyAverages = new HashMap<>();
        Map<LocalDate, List<ProductPrice>> groupedByDate = priceHistory.stream()
                .collect(Collectors.groupingBy(price -> price.getTimestamp().toLocalDate()));

        for (Map.Entry<LocalDate, List<ProductPrice>> entry : groupedByDate.entrySet()) {
            double avgPrice = entry.getValue().stream()
                    .mapToDouble(ProductPrice::getPrice_per_gr)
                    .average()
                    .orElse(0.0);

            dailyAverages.put(entry.getKey(), avgPrice);
        }

        return dailyAverages;
    }

    // Helper method to check if price has changed between scraper data and existing product
    private boolean isPriceChanged(Product existingProduct, Product scrapedProduct) {
        // Consider a price change if price or price_per_gr has changed by more than 0.01
        double priceDifference = Math.abs(existingProduct.getPrice() - scrapedProduct.getPrice());
        double pricePerGrDifference = Math.abs(existingProduct.getPrice_per_gr() - scrapedProduct.getPrice_per_gr());

        final double EPSILON = 0.01; // Threshold for considering a price change

        return priceDifference > EPSILON || pricePerGrDifference > EPSILON;
    }
}
