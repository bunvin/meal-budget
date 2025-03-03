package naamaantoniouk.mealbudget;

import com.fasterxml.jackson.databind.ObjectMapper;
import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.AppModule.product.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class TestingDBfromJSON implements CommandLineRunner {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductServiceImp productService;

    private String url = "http://localhost:8080/api/mealbudget";

    @Override
    public void run(String... args) throws Exception {
        boolean isUpdated = true;
        if (productService.getAllProducts().size() < 1) {
            isUpdated = false;
        }

        while (!isUpdated) {

            // Read the JSON file from the classpath
            Resource resource = new ClassPathResource("shufersal_scrapping/shufersal_products.json");

            try {
                // Convert the resource to a File
                File file = resource.getFile();

                // Configure ObjectMapper
                ObjectMapper mapper = new ObjectMapper();

                // Read the JSON directly as an array of Product objects
                Product[] products = mapper.readValue(file, Product[].class);

                // Convert array to list
                List<Product> productList = Arrays.asList(products);

                // Log the number of products loaded
                System.out.println("Loaded " + productList.size() + " products from shufersal_products.json");

                // Process products as needed
                // For example, save them to your repository
                productService.saveAllProducts(productList);

                // For testing, print the first few products
                for (int i = 0; i < Math.min(5, productList.size()); i++) {
                    System.out.println(productList.get(i));
                }

            } catch (IOException e) {
                System.err.println("Error reading JSON file: " + e.getMessage());
                e.printStackTrace();
            }
            isUpdated = true;
        }
    }
}