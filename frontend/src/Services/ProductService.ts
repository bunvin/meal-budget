import axios from "axios"
import appConfig from "../AppConfig/AppConfig";
import { ProductActionType, productStore } from "../State/ProductState";
import Product from "../Models/Product";

class ProductService {

    isFetch: boolean = false;

    async getProductsList(isForceFetch: boolean = false): Promise<Product[]> {
        if (!this.isFetch || isForceFetch === true) {
            const response = await axios.get<Product[]>(appConfig.apiAddress + '/product/all');
            productStore.dispatch({ type: ProductActionType.GetProducts, payload: response.data });
            this.isFetch = true;
        }
        return productStore.getState().productList;
    }

    async deleteProduct(id: number): Promise<void> {
        await axios.delete<Product>(appConfig.apiAddress + '/product/' + id);
        productStore.dispatch({ type: ProductActionType.DeleteProduct, payload: id });
    }

}

const productService = new ProductService();
export default productService;