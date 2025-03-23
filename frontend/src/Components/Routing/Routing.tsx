import { Navigate, Route, Routes } from "react-router-dom";
import Main from "../Main/Main";
import ProductsList from "../Product/ProductsList";



function Routing(): JSX.Element {
    return (
        <div>
            <Routes>
                <Route path="/main" element={<Main />}/>
                <Route path="/" element={<Navigate to="/main"/>}/>
                <Route path="/products-list" element={<ProductsList />}/>
=            </Routes>
        </div>
    );
}

export default Routing;