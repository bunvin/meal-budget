package naamaantoniouk.mealbudget.AppModule.product;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product addProduct(Product product) throws AppException;
    Product getSingleProduct(int id) throws AppException;
    void updateProduct(int id, Product product) throws AppException;
    void deleteProduct(int id) throws AppException;
    List<Product>getAllProducts() throws AppException;

    public List<Product> saveAllProducts(List<Product> products);

}
