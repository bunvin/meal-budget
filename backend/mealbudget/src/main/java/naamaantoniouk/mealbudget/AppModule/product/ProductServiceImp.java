package naamaantoniouk.mealbudget.AppModule.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import org.springframework.stereotype.Service;

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

}
