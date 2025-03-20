import { useEffect, useState } from 'react';
import './IngredientsList.css';
import ingredientService from '../../Services/IngredientService';
import Ingredient from '../../Models/Ingredient';
import { ingredientStore } from '../../State/IngredientState';
import IngredientsCard from './IngredientsCard';

function IngredientsList(): JSX.Element {
    const [isLoading, setLoading] = useState<boolean>(true);

    let [ingredientList, setingredientList] = useState<Ingredient[]>([]);

    useEffect(() => {
        (async function getIngredientList() {
            const ingredientList: Ingredient[] = await ingredientService.getIngredientList();
            setingredientList(ingredientList.slice(0,20));
            setLoading(false);
            console.log("Done")
        })()
        const subscription = ingredientStore.subscribe(() => {
            setingredientList([...ingredientStore.getState().ingredientList]);
        });
        return () => subscription();//unsubscribe pre-destroy
    },[]);

    function onDelete(id:number){ //get List updated state from Store 
        setingredientList([...ingredientStore.getState().ingredientList]);
    }

    return (
        <div className='IngredientsList'>
        
            {isLoading && <h1>Loading...</h1>}
            {!isLoading &&
            ingredientList.map(ingredient =>
            <div key = {ingredient.id}><IngredientsCard ingredient={ingredient} onDelete = {onDelete} />
            </div>)}
    
        </div>
    );
}

export default IngredientsList;