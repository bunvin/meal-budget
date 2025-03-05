import axios from "axios"
import appConfig from "../AppConfig/AppConfig";
import { IngredientActionType, ingredientStore } from "../State/IngredientState";

class IngredientService {

    isFetch: boolean = false;

    async getTaskList(isForceFetch: boolean = false): Promise<Ingredient[]> {
        if (!this.isFetch || isForceFetch === true) {
            const response = await axios.get<Ingredient[]>(appConfig.apiAddress + '/product');
            ingredientStore.dispatch({ type: IngredientActionType.GetIngredients, payload: response.data });
            this.isFetch = true;
        }
        return ingredientStore.getState().ingredientList;
    }

    async addTask(ingredient: Ingredient): Promise<Ingredient> {
        const response = await axios.post<Ingredient>(appConfig.apiAddress + '/product', ingredient);
        ingredientStore.dispatch({ type: IngredientActionType.AddIngredient, payload: response.data });
        return response.data;
    }

    async deleteTask(id: number): Promise<void> {
        await axios.delete<Ingredient>(appConfig.apiAddress + '/product/' + id);
        ingredientStore.dispatch({ type: IngredientActionType.DeleteIngredient, payload: id });
    }

    async updateIngredient(ingredient: Ingredient): Promise<void> {
        await axios.put<Ingredient>(appConfig.apiAddress + '/task/' + ingredient.id!, ingredient);
        ingredientStore.dispatch({ type: IngredientActionType.UpdateIngredient, payload: ingredient });
    }


}

const ingredientService = new IngredientService();
export default ingredientService;