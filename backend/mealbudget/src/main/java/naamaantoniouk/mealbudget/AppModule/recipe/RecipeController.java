package naamaantoniouk.mealbudget.AppModule.recipe;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredientDTO;
import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredientService;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@RestController
@RequestMapping("/api/mealbudget/recipes")
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {
    
    private final RecipeService recipeService;
    private final RecipeIngredientService recipeIngredientService;
    
    @Autowired
    public RecipeController(RecipeService recipeService, RecipeIngredientService recipeIngredientService) {
        this.recipeService = recipeService;
        this.recipeIngredientService = recipeIngredientService;
    }

    //ADD RECIPE
    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeCreateRequestDTO request) throws AppException {
        // save the recipe
        RecipeDTO newRecipe = recipeService.addRecipe(request.getRecipe());
        
        // add ingredients
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            for (RecipeIngredientDTO ingredient : request.getIngredients()) {
                // Set the correct recipeId
                ingredient.setRecipeId(newRecipe.getId());
                recipeIngredientService.addRecipeIngredient(
                    newRecipe.getId(),
                    ingredient.getProductId(),
                    ingredient.getAmount()
                );
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.getSingleRecipe(newRecipe.getId()));
    }

    //UPDATE RECIPE
    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(
            @PathVariable int id,
            @RequestBody RecipeCreateRequestDTO request) throws AppException {
        
        // Update the recipe details
        RecipeDTO recipeDTO = request.getRecipe();
        recipeDTO.setId(id);
        recipeService.updateRecipe(id, recipeDTO);
//to check if ingredients has being updated?
        // Remove existing ingredients
        List<RecipeIngredientDTO> existingIngredients = recipeIngredientService.recipeIngredients(id);
        for (RecipeIngredientDTO existing : existingIngredients) {
            recipeIngredientService.deleteRecipeIngredient(existing.getId());
        }      
        // Add all new ingredients
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            for (RecipeIngredientDTO ingredient : request.getIngredients()) {
                recipeIngredientService.addRecipeIngredient(
                    id,
                    ingredient.getProductId(),
                    ingredient.getAmount()
                );
            }
        }
        return ResponseEntity.ok(recipeService.getSingleRecipe(id));
    }

    //ADD SINGLE INGREDIENT
    @PostMapping("/{recipeId}/ingredients")
    public ResponseEntity<RecipeIngredientDTO> addIngredient(
            @PathVariable int recipeId,
            @RequestBody RecipeIngredientDTO ingredient) throws AppException {
        
        RecipeIngredientDTO savedIngredient = recipeIngredientService.addRecipeIngredient(
            recipeId,
            ingredient.getProductId(),
            ingredient.getAmount()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIngredient);
    }

    //GET RECIPE
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable int id) throws AppException {
        return ResponseEntity.ok(recipeService.getSingleRecipe(id));
    }

    //UPDATE INGREDIENT AMOUNT
    @PutMapping("/ingredients/{id}/amount/{amount}")
    public ResponseEntity<RecipeIngredientDTO> updateIngredientAmount(
            @PathVariable int id,
            @PathVariable int amount) throws AppException {
        
        recipeIngredientService.updateRecipeIngredient(id, amount);
        return ResponseEntity.ok(recipeIngredientService.getSingleRecipeIngredient(id));
    }

    //DELETE BY ID
    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable int id) throws AppException {
        recipeIngredientService.deleteRecipeIngredient(id);
        return ResponseEntity.noContent().build();
    }

    //GET RECIPE PRICE HISTORY
    @GetMapping("/{id}/price-history")
    public ResponseEntity<Map<LocalDate, Double>> getRecipePriceHistory(@PathVariable int id) throws AppException {
        Map<LocalDate, Double> priceHistory = recipeService.getRecipePriceHistory(id);
        return ResponseEntity.ok(priceHistory);
    }
}
