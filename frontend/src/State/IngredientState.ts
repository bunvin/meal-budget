import { createStore } from "redux";


export class IngredientState {
    ingredientList : Ingredient[] = [];
}

export enum IngredientActionType {
    GetIngredients = 'GetIngredients',
    AddIngredient = 'AddIngredient',
    UpdateIngredient = 'UpdateIngredient',
    DeleteIngredient = 'DeleteIngredient',
}

export interface IngredientAction {
    type: IngredientActionType,
    payload: any
}


export function ingredientReducer(
    ingredientState: IngredientState = new IngredientState(), 
    action: IngredientAction): IngredientState {
        
    const newState: IngredientState = {...ingredientState};

    switch (action.type) {
        case IngredientActionType.GetIngredients:
            newState.ingredientList = action.payload;
            break;
        case IngredientActionType.AddIngredient:
            newState.ingredientList.push(action.payload);
            break;
        case IngredientActionType.UpdateIngredient:
            const idToUpdate = newState.ingredientList
            .findIndex(ingredient => +(ingredient.id)! === action.payload.id);
            newState.ingredientList[idToUpdate] = action.payload;
            break;
        case IngredientActionType.DeleteIngredient:
            const indexToDelete = newState.ingredientList.findIndex(ingredient => ingredient.id === action.payload);
            newState.ingredientList.splice(indexToDelete, 1);
            break;
    }
    return newState;
}

export const ingredientStore = createStore(ingredientReducer);