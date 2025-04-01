package naamaantoniouk.mealbudget;

import com.fasterxml.jackson.databind.ObjectMapper;
import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.AppModule.product.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebscrapperScheduler {
    @Autowired
    private ProductServiceImp productService;

    @Value("${python.executable.path:python}")
    private String pythonExePath;

    @Value("${python.script.path:backend/mealbudget/src/main/resources/shufersal_scrapping/shuffersal.py}")
    private String pythonScriptPath;

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Scheduled(cron = "0 0 0 * * *")  // Runs at 00:00 every day
    @Scheduled(cron = "0 20 11 * * *")
    public void runPythonScriptAndUpdateDB() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        boolean scriptSuccess = runPythonScript();

        if (scriptSuccess) {
            System.out.println("Python script completed successfully. Updating database...");

            try {
                // Update the database with the scraped data
                productService.updateDBfromJson();
                System.out.println("Database update completed successfully");
            } catch (Exception e) {
                System.err.println("Error updating database: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Python script failed, skipping database update");
        }

        endTime = LocalDateTime.now();
        System.out.println("Total time: " + java.time.Duration.between(startTime, endTime).toMinutes() + " minutes");
    }

    private boolean runPythonScript() {
        try {
            // Execute the script
            ProcessBuilder processBuilder = new ProcessBuilder(pythonExePath, pythonScriptPath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read and log output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Python] " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exit code: " + exitCode);

            return exitCode == 0;
        } catch (Exception e) {
            System.err.println("Error running Python script: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}