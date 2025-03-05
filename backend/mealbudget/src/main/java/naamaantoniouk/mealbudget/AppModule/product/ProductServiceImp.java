package naamaantoniouk.mealbudget.AppModule.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) throws AppException {
        if(this.productRepository.existsById(product.getId())){
            throw new AppException(ProductError.PRODUCT_ALREADY_EXIST);
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product getSingleProduct(int id) throws AppException {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new AppException(ProductError.PRODUCT_NOT_FOUND));
    }

    @Override
    public void updateProduct(int id, Product product) throws AppException {
        Product productDb = this.getSingleProduct(id);
        product.setId(productDb.getId());
        this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) throws AppException {
        Product productDb = this.getSingleProduct(id);
        this.productRepository.deleteById(productDb.getId());
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
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
                // Calculate price per gram
                product.calculatePricePerGram();
    
                try {
                    // Find products with this name (could be multiple)
                    List<Product> existingProducts = productRepository.findByName(product.getName());
    
                    if (!existingProducts.isEmpty()) {
                        // Update the first matching product
                        Product existingProduct = existingProducts.get(0);
                        if (existingProducts.size() > 1){
                            System.out.println("WARNING: "+existingProduct.getName()+" updated only one but in Json "+existingProducts.size()+" Times");
                        }
                        existingProduct.setPrice(product.getPrice());
                        existingProduct.setUnit(product.getUnit());
                        existingProduct.setQuantity(product.getQuantity());
                        existingProduct.setPrice_per_gr(product.getPrice_per_gr());
                        existingProduct.setSelling_method(product.getSelling_method());
                        existingProduct.setImg(product.getImg());
                        
                        updatedProducts.add(existingProduct);
                    } else {
                        // Add as new product if not already in DB
                        newProducts.add(product);
                    }
                } catch (Exception e) {
                    // Log error and continue with next product
                    System.err.println("Error processing product " + product.getName() + ": " + e.getMessage());
                }
            }
    
            // Save all new products
            if (!newProducts.isEmpty()) {
                this.saveAllProducts(newProducts);
            }
    
            // Save all updated products
            if (!updatedProducts.isEmpty()) {
                this.saveAllProducts(updatedProducts);
            }
    
            // Log summary information
            System.out.println("Total products in database: " + this.getAllProducts().size());
            System.out.println("Updated " + updatedProducts.size() + " existing products");
            System.out.println("Added " + newProducts.size() + " new products");
    
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}