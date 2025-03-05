import Ingredient from '../../Models/Ingredient';
import './IngredientsCard.css';

function IngredientsCard(ingredient:Ingredient): JSX.Element {
    return (
        <div className='IngredientsCard'>
            <div className='card'>
                <p>Name: {ingredient.name}</p>
                <p>Price: {ingredient.price}</p>
                <p>unit: {ingredient.id}</p>
                <p>price per gr: {ingredient.price_per_gr}</p>
                
            </div>
        </div>
    );
}

export default IngredientsCard;