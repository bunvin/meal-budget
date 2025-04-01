import { useEffect, useState } from 'react';
import './Header.css';
import { productStore } from '../../State/ProductState';
import { NavLink } from 'react-router-dom';

function Header(): JSX.Element {

    let [ingredientNumber, setIngredientNumber] = useState<number>(productStore.getState().productList.length);

    useEffect(() => {

        const subscription = productStore.subscribe(() => {
            setIngredientNumber(productStore.getState().productList.length);
        });

        return () => subscription();

    }, []);

    return (
        <div className='Header'>
            <h1>Header - {ingredientNumber}</h1>

            <div className='menu'>

            </div>
        </div>
    );
}

export default Header;