package naamaantoniouk.mealbudget.AppModule.recipe;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredient;
import naamaantoniouk.mealbudget.AppModule.recipeIngredient.RecipeIngredientError;
import naamaantoniouk.mealbudget.ErrorHandeling.AppException;

@Service
public class RecipeServiceImp implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RecipeDTO addRecipe(RecipeDTO recipeDTO) throws AppException {
        if(this.recipeRepository.existsById(recipeDTO.getId())){
            throw new AppException(RecipeError.RECIPE_ALREADY_EXIST);
        }
        Recipe recipe = modelMapper.map(recipeDTO, Recipe.class);
        recipe = this.recipeRepository.save(recipe);
        return modelMapper.map(recipe,RecipeDTO.class);
    }

    @Override
    public RecipeDTO getSingleRecipe(int id) throws AppException {
        Recipe recipe = this.recipeRepository.findById(id)
                                                .orElseThrow(() -> new AppException(RecipeError.RECIPE_NOT_FOUND));
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
            throw new AppException(RecipeIngredientError.RECIPE_INGREDIENT_NOT_FOUND);
        }
        
        double totalSum = 0;
        for (RecipeIngredient ingredient : ingredients){
            totalSum += ingredient.getAmount()* ingredient.getPrice_per_gr();
        };
        return totalSum;
    }

}
