import { NavLink } from "react-router-dom";
import './Menu.css';

function Menu(): JSX.Element {
    return (
        <div className="Menu">
            <NavLink className={({isActive}) => isActive ? 'styled-link active' : 'styled-link'} to='/main'>Main</NavLink>
            <NavLink className={({isActive}) => isActive ? 'styled-link active' : 'styled-link'} to='/recipe-form'>Create Recipe</NavLink>
            <NavLink className={({isActive}) => isActive ? 'styled-link active' : 'styled-link'} to='products-list'>Products</NavLink>
        </div>
    )
}

export default Menu;