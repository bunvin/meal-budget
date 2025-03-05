import { Navigate, Route, Routes } from "react-router-dom";
import Main from "../Main/Main";
import IngredientsList from "../Ingredients/IngredientsList";
import IngredientsDetails from "../Ingredients/IngredientsDetails";



function Routing(): JSX.Element {
    return (
        <div>
            <Routes>
                <Route path="/main" element={<Main />}/>
                <Route path="/" element={<Navigate to="/main"/>}/>
                <Route path="/task-list" element={<IngredientsList />}/>
                <Route path="/task-details/:id?" element={<IngredientsDetails />}/>
=            </Routes>
        </div>
    );
}

export default Routing;