import { createStore } from "redux";
import Ingredient from "../Models/Product";
import Product from "../Models/Product";


export class ProductState {
    productList : Product[] = [];
}

export enum ProductActionType {
    GetProducts = 'GetProducts',
    AddProduct = 'AddProduct',
    UpdateProduct = 'UpdateProduct',
    DeleteProduct = 'DeleteProduct',
}

export interface ProductAction {
    type: ProductActionType,
    payload: any
}


export function productReducer(
    productState: ProductState = new ProductState(), 
    action: ProductAction): ProductState {
        
    const newState: ProductState = {...productState};

    switch (action.type) {
        case ProductActionType.GetProducts:
            newState.productList = action.payload;
            break;
        case ProductActionType.AddProduct:
            newState.productList.push(action.payload);
            break;
        case ProductActionType.UpdateProduct:
            const idToUpdate = newState.productList
            .findIndex(product => +(product.id)! === action.payload.id);
            newState.productList[idToUpdate] = action.payload;
            break;
        case ProductActionType.DeleteProduct:
            const indexToDelete = newState.productList.findIndex(product => product.id === action.payload);
            newState.productList.splice(indexToDelete, 1);
            break;
    }
    return newState;
}

export const productStore = createStore(productReducer);