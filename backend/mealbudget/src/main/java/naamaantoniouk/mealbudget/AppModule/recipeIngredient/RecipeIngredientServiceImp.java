package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.AppModule.product.Product;
import naamaantoniouk.mealbudget.AppModule.product.ProductDTO;
import naamaantoniouk.mealbudget.AppModule.product.ProductServiceImp;
import naamaantoniouk.mealbudget.AppModule.recipe.Recipe;
import naamaantoniouk.mealbudget.AppModule.recipe.RecipeDTO;
import naamaantoniouk.mealbudget.AppModule.recipe.RecipeServiceImp;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public class RecipeIngredientServiceImp implements RecipeIngredientService {
    @Autowired
    RecipeIngredientRepository ingredientRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    @Lazy
    RecipeServiceImp recipeService;
    @Autowired
    @Lazy
    ProductServiceImp productService;

    @Override
    public RecipeIngredientDTO addRecipeIngredient(int recipeId, int productId, int amount) throws AppException {
        //check if ingredient in recipe
        //check if anount is valid >1
        //check recipe and ingredient exist
        if (amount < 1){
            throw new AppException(RecipeIngredientError.RECIPE_INGREDIENT_AMOUNT_INVALID);
        }
        if (this.ingredientRepository.existsByRecipe_IdAndProduct_Id(recipeId, productId)){
            throw new AppException(RecipeIngredientError.RECIPE_INGREDIENT_IN_RECIPE);
        }
        ProductDTO productDTO = this.productService.getSingleProduct(productId);
        Product product = modelMapper.map(productDTO, Product.class);

        RecipeDTO recipeDTO = this.recipeService.getSingleRecipe(recipeId);
        Recipe recipe = modelMapper.map(recipeDTO,Recipe.class);

        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
        .recipe(recipe)
        .product(product)
        .amount(amount)
        .build();

        recipeIngredient = this.ingredientRepository.save(recipeIngredient);
        return modelMapper.map(recipeIngredient, RecipeIngredientDTO.class);
    }

    @Override
    public RecipeIngredientDTO getSingleRecipeIngredient(int ingredientId) throws AppException {
        RecipeIngredient ingredientDB = this.ingredientRepository.findById(ingredientId)
        .orElseThrow(() -> new AppException(RecipeIngredientError.RECIPE_INGREDIENT_NOT_FOUND));
        return modelMapper.map(ingredientDB, RecipeIngredientDTO.class);
    }

    @Override
    public void updateRecipeIngredient(int ingredientId, int amount) throws AppException {
        RecipeIngredientDTO ingredientDB = this.getSingleRecipeIngredient(ingredientId); 
        ingredientDB.setAmount(amount);
        RecipeIngredient ingredient = modelMapper.map(ingredientDB, RecipeIngredient.class);
        this.ingredientRepository.save(ingredient);
    }

    @Override
    public void deleteRecipeIngredient(int ingredientId) throws AppException {
        RecipeIngredientDTO ingredient = this.getSingleRecipeIngredient(ingredientId);
        this.ingredientRepository.deleteById(ingredientId);
        }

    @Override
    public List<RecipeIngredientDTO> recipeIngredients(int recipeId) {
        List<RecipeIngredient> ingredients = this.ingredientRepository.findByRecipe_Id(recipeId);
        return ingredients.stream().map(ingredient -> modelMapper.map(ingredient, RecipeIngredientDTO.class))
        .collect(Collectors.toList());
    }

}
