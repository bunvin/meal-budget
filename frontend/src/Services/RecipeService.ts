import axios from "axios";
import appConfig from "../AppConfig/AppConfig";
import Recipe from "../Models/Recipe";
import { RecipeActionType, recipeStore } from "../State/RecipeState";
import { RecipeCreateRequest } from "../Models/RecipeCreateRequest";

class RecipeService {
    isFetch: boolean = false;

    // Get all recipes
    async getRecipesList(isForceFetch: boolean = false): Promise<Recipe[]> {
        if (!this.isFetch || isForceFetch === true) {
            try {
                const response = await axios.get<Recipe[]>(`${appConfig.apiAddress}/recipes`);
                recipeStore.dispatch({ type: RecipeActionType.GetRecipes, payload: response.data });
                this.isFetch = true;
            } catch (error) {
                console.error("Error fetching recipes:", error);
                throw error;
            }
        }
        return recipeStore.getState().recipeList;
    }

    // Get a single recipe by ID
    async getRecipe(id: number): Promise<Recipe> {
        try {
            const response = await axios.get<Recipe>(`${appConfig.apiAddress}/recipes/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching recipe with ID ${id}:`, error);
            throw error;
        }
    }

    // Create a new recipe
    async createRecipe(recipeData: RecipeCreateRequest): Promise<Recipe> {
        try {
            const response = await axios.post<Recipe>(`${appConfig.apiAddress}/recipes`, recipeData);
            const newRecipe = response.data;
            recipeStore.dispatch({ type: RecipeActionType.AddRecipe, payload: newRecipe });
            return newRecipe;
        } catch (error) {
            console.error("Error creating recipe:", error);
            throw error;
        }
    }

    // Update an existing recipe
    async updateRecipe(id: number, recipeData: RecipeCreateRequest): Promise<Recipe> {
        try {
            const response = await axios.put<Recipe>(`${appConfig.apiAddress}/recipes/${id}`, recipeData);
            const updatedRecipe = response.data;
            recipeStore.dispatch({ type: RecipeActionType.UpdateRecipe, payload: updatedRecipe });
            return updatedRecipe;
        } catch (error) {
            console.error(`Error updating recipe with ID ${id}:`, error);
            throw error;
        }
    }

    // Delete a recipe
    async deleteRecipe(id: number): Promise<void> {
        try {
            await axios.delete(`${appConfig.apiAddress}/recipes/${id}`);
            recipeStore.dispatch({ type: RecipeActionType.DeleteRecipe, payload: id });
        } catch (error) {
            console.error(`Error deleting recipe with ID ${id}:`, error);
            throw error;
        }
    }

    // Get recipe price history
    async getRecipePriceHistory(id: number): Promise<Record<string, number>> {
        try {
            const response = await axios.get<Record<string, number>>(`${appConfig.apiAddress}/recipes/${id}/price-history`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching price history for recipe with ID ${id}:`, error);
            throw error;
        }
    }

    // Add an ingredient to a recipe
    async addIngredientToRecipe(recipeId: number, productId: number, amount: number) {
        try {
            const response = await axios.post(`${appConfig.apiAddress}/recipes/${recipeId}/ingredients`, {
                productId,
                amount
            });
            return response.data;
        } catch (error) {
            console.error(`Error adding ingredient to recipe with ID ${recipeId}:`, error);
            throw error;
        }
    }

    // Update ingredient amount
    async updateIngredientAmount(ingredientId: number, amount: number) {
        try {
            const response = await axios.put(`${appConfig.apiAddress}/recipes/ingredients/${ingredientId}/amount/${amount}`);
            return response.data;
        } catch (error) {
            console.error(`Error updating ingredient amount for ingredient with ID ${ingredientId}:`, error);
            throw error;
        }
    }

    // Delete an ingredient from a recipe
    async deleteIngredient(ingredientId: number): Promise<void> {
        try {
            await axios.delete(`${appConfig.apiAddress}/recipes/ingredients/${ingredientId}`);
        } catch (error) {
            console.error(`Error deleting ingredient with ID ${ingredientId}:`, error);
            throw error;
        }
    }
}

const recipeService = new RecipeService();
export default recipeService;