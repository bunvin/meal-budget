package naamaantoniouk.mealbudget.AppModule.product;

import naamaantoniouk.mealbudget.ErrorHandeling.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/mealbudget")
public class ProductController {
    @Autowired
    private ProductServiceImp productService;

    @PostMapping("/id")
    public Product addProduct(@RequestBody Product product) throws AppException {
        return this.productService.addProduct(product);
    };

    @PutMapping("/{id}")
    public void updateProduct(@RequestBody Product product, @PathVariable int id) throws AppException {
        this.productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) throws AppException {
        this.productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    public Product getSingleProduct(@PathVariable int id) throws AppException {
        return this.productService.getSingleProduct(id);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return this.productService.getAllProducts();
    }





}
