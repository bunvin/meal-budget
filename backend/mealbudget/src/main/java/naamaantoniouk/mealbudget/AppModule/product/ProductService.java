package naamaantoniouk.mealbudget.AppModule.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product addProduct(Product product) throws Exception;
    Product getSingleProduct(int id) throws Exception;
    void updateProduct(int id, Product product) throws Exception;
    void deleteProduct(int id) throws Exception;
    List<Product>getAllProducts() throws Exception;

}
