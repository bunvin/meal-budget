import Product from '../../Models/Product';
import productService from '../../Services/ProductService';
import './ProductCard.css';

interface ProductProps {
    product: Product,
    onDelete: (id: number) => void
}

function ProductCard(productProps:ProductProps): JSX.Element {

    async function onRemove(id: number): Promise<void> {
        const isRemove: boolean = window.confirm('Are you sure you want to remove this task?');
        if (isRemove) {
            await productService.deleteProduct(id);
            productProps.onDelete(id);
        }
    }

    return (
        <div className='IngredientsCard'>
            <div className='card'>
                <p>Name: {productProps.product.name} #{productProps.product.id}</p>
                <p>Category: {productProps.product.category}</p>
                <p>unit: {productProps.product.unit}</p>
                <p>price per 100gr: {(productProps.product.price_per_gr * productProps.product.min_gr_unit).toFixed(2)}</p>
                <img src={productProps.product.image_url} alt="Ingredient" /> <button className='remove' onClick={() => onRemove(productProps.product.id!)}>remove</button>
            </div>
        </div>
    );
}

export default ProductCard;