import { NavLink } from 'react-router-dom';
import './Menu.css';

function Menu(): JSX.Element {
    return (
        <div className='Menu'>
            <NavLink className='styled-link' to='/main'>Main</NavLink>
            <NavLink className='styled-link' to='ingredient-list'>Ingredients</NavLink>
        </div>
    );
}

export default Menu;