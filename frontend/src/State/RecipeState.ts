import { createStore } from "redux";
import Recipe from "../Models/Recipe";

export class RecipeState {
    recipeList: Recipe[] = [];
}

export enum RecipeActionType {
    GetRecipes = 'GetRecipes',
    AddRecipe = 'AddRecipe',
    UpdateRecipe = 'UpdateRecipe',
    DeleteRecipe = 'DeleteRecipe',
}

export interface RecipeAction {
    type: RecipeActionType,
    payload: any
}

export function recipeReducer(
    recipeState: RecipeState = new RecipeState(), 
    action: RecipeAction): RecipeState {
        
    const newState: RecipeState = {...recipeState};

    switch (action.type) {
        case RecipeActionType.GetRecipes:
            newState.recipeList = action.payload;
            break;
        case RecipeActionType.AddRecipe:
            newState.recipeList.push(action.payload);
            break;
        case RecipeActionType.UpdateRecipe:
            const idToUpdate = newState.recipeList
                .findIndex(recipe => recipe.id === action.payload.id);
            if (idToUpdate !== -1) {
                newState.recipeList[idToUpdate] = action.payload;
            }
            break;
        case RecipeActionType.DeleteRecipe:
            const indexToDelete = newState.recipeList.findIndex(recipe => recipe.id === action.payload);
            if (indexToDelete !== -1) {
                newState.recipeList.splice(indexToDelete, 1);
            }
            break;
    }
    return newState;
}

export const recipeStore = createStore(recipeReducer);