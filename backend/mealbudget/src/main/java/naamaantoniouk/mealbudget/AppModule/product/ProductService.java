package naamaantoniouk.mealbudget.AppModule.product;

import java.util.List;

import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public interface ProductService {
    ProductDTO addProduct(ProductDTO product) throws AppException;
    ProductDTO getSingleProduct(int id) throws AppException;
    void updateProduct(int id, ProductDTO product) throws AppException;
    void deleteProduct(int id) throws AppException;
    List<ProductDTO>getAllProducts() throws AppException;

    //filters get allproducts by category name

    public List<Product> saveAllProducts(List<Product> products);
    public List<Product> getProductByName(String name);

    public void updateDBfromJson();
}
