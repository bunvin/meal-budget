package naamaantoniouk.mealbudget.AppModule.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/mealbudget")
public class ProductController {
    @Autowired
    private ProductServiceImp productService;

    @PostMapping("product/{id}")
    public ProductDTO addProduct(@RequestBody ProductDTO product) throws AppException {
        return this.productService.addProduct(product);
    };

    @PutMapping("product/{id}")
    public void updateProduct(@RequestBody ProductDTO product, @PathVariable int id) throws AppException {
        this.productService.updateProduct(id, product);
    }

    @DeleteMapping("product/{id}")
    public void deleteProduct(@PathVariable int id) throws AppException {
        this.productService.deleteProduct(id);
    }

    @GetMapping("product/{id}")
    public ProductDTO getSingleProduct(@PathVariable int id) throws AppException {
        return this.productService.getSingleProduct(id);
    }

    @GetMapping("product/all")
    public List<ProductDTO> getAllProducts() {
        return this.productService.getAllProducts();
    }
}
