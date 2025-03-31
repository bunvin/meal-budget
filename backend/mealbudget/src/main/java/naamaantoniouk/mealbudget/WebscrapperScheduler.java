package naamaantoniouk.mealbudget;

import com.fasterxml.jackson.databind.ObjectMapper;
import naamaantoniouk.mealbudget.AppModule.product.Product;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class WebscrapperScheduler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron = "0 0 0 * * *")  // Runs at 00:00 every day
    public void runPythonScript() {
        try {
            // Python script location
            String pythonScriptPath = "shufersal_scrapping\\shuffersal.py";
            String pythonExePath = "C:\\Users\\Danil\\AppData\\Local\\Programs\\Python\\Python312\\python.exe";
            String jsonFilePath = "shufersal_scrapping\\shufersal_products.json";

            // Execute the script
            ProcessBuilder processBuilder = new ProcessBuilder(pythonExePath, pythonScriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            System.out.println("after waiting");

            // String jsonData = output.toString().trim();

            List<Product> products = objectMapper.readValue(new File(jsonFilePath),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Product[].class));

//            for (Product product : products) {
//                System.out.println(product.getCompany() + " - " + stock.getPrice());
//            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}