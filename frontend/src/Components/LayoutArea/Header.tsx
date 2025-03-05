import { useEffect, useState } from 'react';
import './Header.css';
import { ingredientStore } from '../../State/IngredientState';

function Header(): JSX.Element {

    let [ingredientNumber, setIngredientNumber] = useState<number>(ingredientStore.getState().ingredientList.length);

    useEffect(() => {

        const subscription = ingredientStore.subscribe(() => {
            setIngredientNumber(ingredientStore.getState().ingredientList.length);
        });

        return () => subscription();

    }, []);

    return (
        <div className='Header'>
            <h1>Header - {ingredientNumber}</h1>
        </div>
    );
}

export default Header;