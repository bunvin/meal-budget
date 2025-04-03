import Recipe from "./Recipe";
import Ingredient from "./Ingredient";

export interface RecipeCreateRequest {
    recipe: Recipe;
    ingredients: Ingredient[];
}