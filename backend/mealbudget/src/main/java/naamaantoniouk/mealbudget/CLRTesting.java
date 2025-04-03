package naamaantoniouk.mealbudget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import naamaantoniouk.mealbudget.AppModule.product.ProductServiceImp;

@Component
public class CLRTesting implements CommandLineRunner {
    // @Autowired
    // private RestTemplate restTemplate;
    // private String url = "http://localhost:8080/api/mealbudget";

    @Autowired
    private ProductServiceImp productService;
    @Autowired
    private WebscrapperScheduler webscrapperScheduler;



    @Override
    public void run(String... args) throws Exception {
        //webscrapperScheduler.runPythonScriptAndUpdateDB();
        //productService.updateDBfromJson();
    }
}