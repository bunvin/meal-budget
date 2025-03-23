package naamaantoniouk.mealbudget.AppModule.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) throws AppException {
        if(this.productRepository.existsById(productDTO.getId())){
            throw new AppException(ProductError.PRODUCT_ALREADY_EXIST);
        }
        Product product = modelMapper.map(productDTO, Product.class);
        product = this.productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO getSingleProduct(int id) throws AppException {
        Product productDB = this.productRepository.findById(id)
                                                        .orElseThrow(() -> new AppException(ProductError.PRODUCT_NOT_FOUND));
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