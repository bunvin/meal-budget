package naamaantoniouk.mealbudget.AppModule.recipeIngredient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Integer>{
    boolean existsByRecipe_IdAndProduct_Id(int recipeId, int productId);
    List<RecipeIngredient> findByRecipe_Id(int recipeId);

}
