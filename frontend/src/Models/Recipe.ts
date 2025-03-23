import Ingredient from "./Ingredient";

interface Recipe {
    id: number,
    name: string,
    servings: number,
    difficulty: string,
    ingredients: Ingredient[]
}
export default Recipe;