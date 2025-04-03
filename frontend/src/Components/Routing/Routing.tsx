import { Navigate, Route, Routes } from "react-router-dom";
import Main from "../Main/Main";
import ProductsList from "../Product/ProductsList";
import RecipeForm from "../Recipe/RecipeForm";



function Routing(): JSX.Element {
    return (
        <div>
            <Routes>
                <Route path="/main" element={<Main />}/>
                <Route path="/" element={<Navigate to="/main"/>}/>
                <Route path="/products-list" element={<ProductsList />}/>
                <Route path="/recipe-form" element={<RecipeForm />}/>

=            </Routes>
        </div>
    );
}

export default Routing;