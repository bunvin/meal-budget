import Ingredient from '../../Models/Ingredient';
import ingredientService from '../../Services/IngredientService';
import './IngredientsCard.css';

interface IngredientProps {
    ingredient: Ingredient,
    onDelete: (id: number) => void
}

function IngredientsCard(ingredientProps:IngredientProps): JSX.Element {

    async function onRemove(id: number): Promise<void> {
        const isRemove: boolean = window.confirm('Are you sure you want to remove this task?');
        if (isRemove) {
            await ingredientService.deleteIngredient(id);
            ingredientProps.onDelete(id);
        }
    }

    return (
        <div className='IngredientsCard'>
            <div className='card'>
                <p>Name: {ingredientProps.ingredient.name} #{ingredientProps.ingredient.id}</p>
                <p>Category: {ingredientProps.ingredient.category}</p>
                <p>unit: {ingredientProps.ingredient.unit}</p>
                <p>price per 100gr: {(ingredientProps.ingredient.price_per_gr * ingredientProps.ingredient.min_gr_unit).toFixed(2)}</p>
                <img src={ingredientProps.ingredient.image_url} alt="Ingredient" /> <button className='remove' onClick={() => onRemove(ingredientProps.ingredient.id!)}>remove</button>
            </div>
        </div>
    );
}

export default IngredientsCard;