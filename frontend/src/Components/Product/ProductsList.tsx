import { useEffect, useState } from 'react';
import './ProductsList.css';
import { productStore } from '../../State/ProductState';
import Product from '../../Models/Product';
import ProductCard from './ProductCard';
import productService from '../../Services/ProductService';

function ProductsList(): JSX.Element {
    const [isLoading, setLoading] = useState<boolean>(true);

    let [productList, setProductList] = useState<Product[]>([]);

    useEffect(() => {
        (async function getProductsList() {
            const productList: Product[] = await productService.getProductsList();
            // setProductList(productList.slice(0,20));
            setProductList([...productList]);
            setLoading(false);
            console.log("Done")
        })()
        const subscription = productStore.subscribe(() => {
            setProductList([...productStore.getState().productList]);
        });
        return () => subscription();//unsubscribe pre-destroy
    },[]);

    function onDelete(id:number){ //get List updated state from Store 
        setProductList([...productStore.getState().productList]);
    }

    return (
        <div className='ProductsList'>
        
            {isLoading && <h1>Loading...</h1>}
            {!isLoading &&
            productList.map(product =>
            <div key = {product.id}><ProductCard product={product} onDelete = {onDelete} />
            </div>)}
    
        </div>
    );
}

export default ProductsList;