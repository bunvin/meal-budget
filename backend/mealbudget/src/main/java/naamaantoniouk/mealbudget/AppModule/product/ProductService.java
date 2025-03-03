package naamaantoniouk.mealbudget.AppModule.product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product peoduct) throws Exception;
    Product getSingleProduct(int id) throws Exception;
    void updateProduct(int id, Product product) throws Exception;
    void deleteProduct(int id) throws Exception;
    List<Product>getAllProducts() throws Exception;

}
