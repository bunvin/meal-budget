package naamaantoniouk.mealbudget.AppModule.recipe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.AppModule.productPrice.ProductPrice;
import naamaantoniouk.mealbudget.AppModule.productPrice.ProductPriceImp;
import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredient;
import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredientError;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public class RecipeServiceImp implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductPriceImp productPriceService;

    @Override
    public RecipeDTO addRecipe(RecipeDTO recipeDTO) throws AppException {
        if(this.recipeRepository.existsById(recipeDTO.getId())){
            throw RecipeError.RECIPE_ALREADY_EXIST.createException(recipeDTO.getId());
        }
        Recipe recipe = modelMapper.map(recipeDTO, Recipe.class);
        recipe = this.recipeRepository.save(recipe);
        return modelMapper.map(recipe,RecipeDTO.class);
    }

    @Override
    public RecipeDTO getSingleRecipe(int id) throws AppException {
        Recipe recipe = this.recipeRepository.findById(id)
                                                .orElseThrow(() -> RecipeError.RECIPE_NOT_FOUND.createException(id));
        return modelMapper.map(recipe, RecipeDTO.class);
        }

    @Override
    public void updateRecipe(int id, RecipeDTO recipeDTO) throws AppException {
        RecipeDTO recipeDB = this.getSingleRecipe(id);
        recipeDB.setId(id);
        Recipe recipe = modelMapper.map(recipeDTO, Recipe.class);
        this.recipeRepository.save(recipe);        
    }

    @Override
    public void deleteRecipe(int id) throws AppException {
        RecipeDTO recipeDB = this.getSingleRecipe(id);
        this.recipeRepository.deleteById(id);
    }

    @Override
    public List<RecipeDTO> getAllRecipies() throws AppException {
        List<Recipe> recipes = this.recipeRepository.findAll();
        return recipes.stream()
                            .map(recipe -> modelMapper.map(recipe, RecipeDTO.class))
                            .collect(Collectors.toList());    
        }

    @Override
    public double calcTotalNIS(int recipeId) throws AppException {
        List<RecipeIngredient> ingredients = this.getSingleRecipe(recipeId).getIngredients();
        if (ingredients == null){
            throw RecipeIngredientError.RECIPE_INGREDIENTS_NOT_FOUND.createException(recipeId);
        }
        
        double totalSum = 0;
        for (RecipeIngredient ingredient : ingredients){
            totalSum += ingredient.getAmount()* ingredient.getPrice_per_gr();
        };
        return totalSum;
    }


    public Map<LocalDate, Double> getRecipePriceHistory(int recipeId) throws AppException {
        // Get the recipe with ingredients
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> RecipeError.RECIPE_NOT_FOUND.createException(recipeId));

        // Map to store aggregated recipe costs by date
        Map<LocalDate, Double> recipeCostByDate = new TreeMap<>(); // TreeMap maintains date order
        
        // Set to track all dates across all ingredients
        Set<LocalDate> allDates = new HashSet<>();
        
        // Track earliest and latest dates we've found prices for
        LocalDateTime earliestDate = null;
        LocalDateTime latestDate = null;
        
        // For each ingredient in the recipe
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            int productId = ingredient.getProduct().getId();
            double amountInGrams = ingredient.getAmount();
            try {
                // get the earliest price for this product
                LocalDateTime oldestPossibleDate = LocalDateTime.of(2024, 1, 1, 0, 0);
                LocalDateTime currentDate = LocalDateTime.now();
                // Get all price history for this product
                List<ProductPrice> priceHistory = productPriceService.getProductPriceHistory(
                        productId, oldestPossibleDate, currentDate);
                if (priceHistory.isEmpty()) {
                    continue;
                }
                // start = "initial_price"
                ProductPrice initialPriceRecord = null;
                for (ProductPrice price : priceHistory) {
                    if ("initial_price".equals(price.getSource())) {
                        initialPriceRecord = price;
                        break;
                    }
                }
                
                // Update earliest and latest dates
                LocalDateTime firstTimestamp = initialPriceRecord.getTimestamp();
                if (earliestDate == null || firstTimestamp.isBefore(earliestDate)) {
                    earliestDate = firstTimestamp;
                }
                if (!priceHistory.isEmpty()) {
                    LocalDateTime lastTimestamp = priceHistory.get(priceHistory.size() - 1).getTimestamp();
                    if (latestDate == null || lastTimestamp.isAfter(latestDate)) {
                        latestDate = lastTimestamp;
                    }
                }
                
                // Process each price point
                for (ProductPrice price : priceHistory) {
                    LocalDate date = price.getTimestamp().toLocalDate();
                    double ingredientCost = price.getPrice_per_gr() * amountInGrams;
                    // Track all dates
                    allDates.add(date);
                    
                    // Add to the daily total
                    recipeCostByDate.putIfAbsent(date, 0.0); // default
                    recipeCostByDate.put(date, recipeCostByDate.get(date) + ingredientCost);
                }
            } catch (AppException e) {
                // Log error and continue with next ingredient
                System.err.println("Error getting price history for product " + productId + ": " + e.getMessage());
            }
        }
        
        // If we have sparse data, interpolate missing dates
        if (!recipeCostByDate.isEmpty() && allDates.size() > 1) {
            //check if all dates are calculated
            LocalDate minDate = Collections.min(allDates);
            LocalDate maxDate = Collections.max(allDates);
            long totalDaysInRange = ChronoUnit.DAYS.between(minDate, maxDate) + 1; // +1 to include the end date
            
            // Only interpolate if we're missing dates in the range
            if (allDates.size() != totalDaysInRange) {
                interpolateMissingDates(recipeCostByDate, allDates);
            }
        }
        
        return recipeCostByDate;
    }


    private void interpolateMissingDates(Map<LocalDate, Double> costByDate, Set<LocalDate> allDatesWithPrice) {
        LocalDate startDate = Collections.min(allDatesWithPrice);
        LocalDate endDate = Collections.max(allDatesWithPrice);
    
        // Create a complete set of dates in the range
        LocalDate currentDate = startDate;
        List<LocalDate> allDates = new ArrayList<>();
        
        while (!currentDate.isAfter(endDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        
        // days with existing data
        List<LocalDate> existingDates = new ArrayList<>(costByDate.keySet());
        Collections.sort(existingDates);
        
        if (existingDates.isEmpty()) {
            return;
        }
        
        // For each date in the range
        for (LocalDate date : allDates) {
            if (!costByDate.containsKey(date)) {
                // Find closest dates before and after
                LocalDate closestBefore = findClosestBefore(existingDates, date);
                LocalDate closestAfter = findClosestAfter(existingDates, date);
                
                if (closestBefore != null && closestAfter != null) {
                    // Interpolate between the two values
                    double valueBefore = costByDate.get(closestBefore);
                    double valueAfter = costByDate.get(closestAfter);
                    long daysBetween = ChronoUnit.DAYS.between(closestBefore, closestAfter);
                    long daysFromBefore = ChronoUnit.DAYS.between(closestBefore, date);
                    
                    if (daysBetween > 0) {
                        double interpolatedValue = valueBefore + 
                                (valueAfter - valueBefore) * ((double) daysFromBefore / daysBetween);
                        costByDate.put(date, interpolatedValue);
                    }
                } else if (closestBefore != null) {
                    // Use the last known value
                    costByDate.put(date, costByDate.get(closestBefore));
                } else if (closestAfter != null) {
                    // Use the next known value
                    costByDate.put(date, costByDate.get(closestAfter));
                }
            }
        }
    }


    private LocalDate findClosestBefore(List<LocalDate> dates, LocalDate target) {
        LocalDate result = null;
        for (LocalDate date : dates) {
            if (date.isBefore(target) && (result == null || date.isAfter(result))) {
                result = date;
            }
        }
        return result;
    }
    
    private LocalDate findClosestAfter(List<LocalDate> dates, LocalDate target) {
        LocalDate result = null;
        for (LocalDate date : dates) {
            if (date.isAfter(target) && (result == null || date.isBefore(result))) {
                result = date;
            }
        }
        return result;
    }
    
}