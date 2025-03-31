package naamaantoniouk.mealbudget.AppModule.product;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import naamaantoniouk.mealbudget.AppModule.productPrice.ProductPrice;
import naamaantoniouk.mealbudget.AppModule.productPrice.ProductPriceImp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductPriceImp productPriceService;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) throws AppException {
        if (this.productRepository.existsById(productDTO.getId())) {
            throw ProductError.PRODUCT_ALREADY_EXIST.createException(productDTO.getId());
        }
        Product product = modelMapper.map(productDTO, Product.class);
        product = this.productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO getSingleProduct(int id) throws AppException {
        Product productDB = this.productRepository.findById(id)
                .orElseThrow(() -> ProductError.PRODUCT_NOT_FOUND.createException(id));
        return modelMapper.map(productDB, ProductDTO.class);
    }

    @Transactional
    @Override
    public void updateProduct(int id, ProductDTO productDTO) throws AppException {
        ProductDTO productDb = this.getSingleProduct(id);
        productDTO.setId(productDb.getId());
        Product product = modelMapper.map(productDTO, Product.class);
        this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) throws AppException {
        ProductDTO productDb = this.getSingleProduct(id);
        this.productRepository.deleteById(productDb.getId());
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = this.productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> saveAllProducts(List<Product> products) {
        List<Product> fromDb = this.productRepository.saveAll(products);
        return fromDb;
    }

    @Override
    public List<Product> getProductByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public void updateDBfromJson() {
        // Read the JSON file from the classpath
        Resource resource = new ClassPathResource("shufersal_scrapping/shufersal_products.json");

        try {
            File file = resource.getFile();
            ObjectMapper mapper = new ObjectMapper();

            // Read the JSON directly as an array of Product objects
            Product[] jsonProducts = mapper.readValue(file, Product[].class);
            List<Product> newProducts = new ArrayList<>();
            List<Product> updatedProducts = new ArrayList<>();

            // Process each product from JSON
            for (Product product : jsonProducts) {
                try {
                    // Calculate price per gram
                    product.calculatePricePerGram();

                    // Find products with this name (could be multiple)
                    List<Product> existingProducts = productRepository.findByName(product.getName());

                    if (!existingProducts.isEmpty()) {
                        // Update the first matching product
                        Product existingProduct = existingProducts.get(0);
                        if (existingProducts.size() > 1) {
                            System.out.println("WARNING: " + existingProduct.getName() + " updated only one but in Json " + existingProducts.size() + " Times");
                        }

                        // Check if price has changed
                        boolean priceChanged = Math.abs(existingProduct.getPrice() - product.getPrice()) > 0.01 ||
                                Math.abs(existingProduct.getPrice_per_gr() - product.getPrice_per_gr()) > 0.01;

                        // Update existing product
                        existingProduct.setPrice(product.getPrice());
                        existingProduct.setUnit(product.getUnit());
                        existingProduct.setQuantity(product.getQuantity());
                        existingProduct.setPrice_per_gr(product.getPrice_per_gr());
                        existingProduct.setSelling_method(product.getSelling_method());
                        existingProduct.setImg(product.getImg());

                        updatedProducts.add(existingProduct);

                        // If the price has changed, record it in price history
                        if (priceChanged && productPriceService != null) {
                            ProductPrice newPriceEntry = ProductPrice.builder()
                                    .productId(existingProduct.getId())
                                    .timestamp(LocalDateTime.now())
                                    .price(product.getPrice())
                                    .price_per_gr(product.getPrice_per_gr())
                                    .source("daily_scrape")
                                    .build();

                            try {
                                productPriceService.addProductPrice(newPriceEntry);
                                System.out.println("Recorded price change for: " + existingProduct.getName());
                            } catch (AppException e) {
                                System.err.println("Error recording price history: " + e.getMessage());
                            }
                        }
                    } else {
                        // This is a new product, prepare it for insertion
                        try {
                            // Set the category enum from string
                            if (product.getCategory() == null && product.getCategory().toString() != null) {
                                try {
                                    Category category = Category.valueOf(product.getCategory().toString());
                                    product.setCategory(category);
                                } catch (IllegalArgumentException e) {
                                    // Default category if not matching
                                    product.setCategory(Category.Vegetables);
                                    System.out.println("WARNING: Unknown category for " + product.getName() + ", defaulting to Vegetables");
                                }
                            }

                            // Add to new products list
                            newProducts.add(product);
                        } catch (Exception e) {
                            System.err.println("Error preparing new product " + product.getName() + ": " + e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    // Log error and continue with next product
                    System.err.println("Error processing product " + product.getName() + ": " + e.getMessage());
                }
            }

            // Save all updated products
            if (!updatedProducts.isEmpty()) {
                List<Product> savedUpdated = this.saveAllProducts(updatedProducts);
                System.out.println("Updated " + savedUpdated.size() + " existing products");
            }

            // Save all new products
            if (!newProducts.isEmpty()) {
                List<Product> savedNew = this.saveAllProducts(newProducts);
                System.out.println("Added " + savedNew.size() + " new products");

                // Record initial price history for new products
                if (productPriceService != null) {
                    for (Product newProduct : savedNew) {
                        try {
                            ProductPrice initialPrice = ProductPrice.builder()
                                    .productId(newProduct.getId())
                                    .timestamp(LocalDateTime.now())
                                    .price(newProduct.getPrice())
                                    .price_per_gr(newProduct.getPrice_per_gr())
                                    .source("initial_price")
                                    .build();

                            productPriceService.addProductPrice(initialPrice);
                        } catch (AppException e) {
                            System.err.println("Error recording initial price for " + newProduct.getName() + ": " + e.getMessage());
                        }
                    }
                }
            }

            // Log summary information
            System.out.println("Total products in database: " + this.getAllProducts().size());

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}