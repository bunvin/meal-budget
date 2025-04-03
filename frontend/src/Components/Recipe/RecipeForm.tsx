import { useNavigate } from 'react-router-dom';
import './RecipeForm.css';
import { useForm } from 'react-hook-form';
import Recipe from '../../Models/Recipe';
import { useEffect, useState } from 'react';
import Ingredient from '../../Models/Ingredient';
import { productStore } from '../../State/ProductState';
import recipeService from '../../Services/RecipeService';
import { RecipeCreateRequest } from '../../Models/RecipeCreateRequest';
import productService from '../../Services/ProductService';
import Product from '../../Models/Product';

function RecipeForm(): JSX.Element {
    const navigate = useNavigate();
    const { register, handleSubmit, formState, reset } = useForm<Recipe>();
    const [ingredients, setIngredients] = useState<Ingredient[]>([]);
    const [selectedProduct, setSelectedProduct] = useState<number>(0);
    const [amount, setAmount] = useState<number>(0);
    const [isSubmitting, setIsSubmitting] = useState<boolean>(false);
    const [productList, setProductList] = useState<Product[]>([]);
    const [isLoading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        // Check if products are already in the store
        const storeProducts = productStore.getState().productList;
        
        if (storeProducts && storeProducts.length > 0) {
            // Use products from store and sort by name
            const sortedProducts = [...storeProducts].sort((a, b) => 
                a.name.localeCompare(b.name)
            );
            setProductList(sortedProducts);
            setLoading(false);
        } else {
            // Load products if store is empty
            const loadProducts = async () => {
                try {
                    const products = await productService.getProductsList();
                    // Sort products by name
                    const sortedProducts = [...products].sort((a, b) => 
                        a.name.localeCompare(b.name)
                    );
                    setProductList(sortedProducts);
                } catch (error) {
                    console.error("Error loading products:", error);
                } finally {
                    setLoading(false);
                }
            };
            loadProducts();
        }
        
        // Subscribe to product store changes
        const subscription = productStore.subscribe(() => {
            // Get products from store and sort by name
            const products = [...productStore.getState().productList];
            const sortedProducts = products.sort((a, b) => 
                a.name.localeCompare(b.name)
            );
            setProductList(sortedProducts);
        });
        
        return () => subscription(); // Unsubscribe on component unmount
    }, []);

    // Add ingredient to the list
    const addIngredient = () => {
        if (selectedProduct && amount > 0) {
            const product = productList.find(p => p.id === selectedProduct);
            if (product) {
                const newIngredient: Ingredient = {
                    id: 0, // Will be set by backend
                    name: product.name,
                    recipeId: 0, // Will be set by backend
                    productId: product.id,
                    amount: amount,
                    unit: product.unit,
                    price_per_gr: product.price_per_gr,
                    img: product.image_url
                };
                setIngredients([...ingredients, newIngredient]);
                setSelectedProduct(0);
                setAmount(0);
            }
        }
    };

    // Remove ingredient from the list
    const removeIngredient = (index: number) => {
        const newIngredients = [...ingredients];
        newIngredients.splice(index, 1);
        setIngredients(newIngredients);
    };

    // Calculate total recipe cost
    const calculateTotalCost = () => {
        return ingredients.reduce((total, ingredient) => {
            return total + (ingredient.price_per_gr * ingredient.amount);
        }, 0).toFixed(2);
    };

    // Handle form submission
    const onSubmit = async (data: Recipe) => {
        // Validate that we have ingredients
        if (ingredients.length === 0) {
            return;
        }
        
        setIsSubmitting(true);
        try {
            // Prepare the recipe request object
            const recipeRequest: RecipeCreateRequest = {
                recipe: {
                    ...data,
                    id: 0, // ID will be assigned by the backend
                    ingredients: [] // This will be populated from our ingredients state
                },
                ingredients: ingredients.map(ingredient => ({
                    ...ingredient,
                    id: 0, // ID will be assigned by the backend
                    recipeId: 0 // Will be assigned by the backend
                }))
            };
            
            // Send the create request
            const newRecipe = await recipeService.createRecipe(recipeRequest);
            
            // Show success message
            alert(`Recipe "${newRecipe.name}" created successfully!`);
            
            // Reset form and navigate back to recipes list
            reset();
            setIngredients([]);
            navigate('/recipes');
        } catch (error) {
            console.error("Error creating recipe:", error);
            alert("Failed to create recipe. Please try again.");
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="recipe-form-container">
            <h2>Create New Recipe</h2>
            {isLoading ? (
                <div className="loading">Loading products...</div>
            ) : (
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className="form-section">
                        <h3>Recipe Details</h3>
                        <div className="form-group">
                            <label htmlFor="name">Recipe Name</label>
                            <input 
                                type="text" 
                                id="name" 
                                placeholder="Enter recipe name"
                                {...register("name", {
                                    required: { value: true, message: 'Recipe name is required' },
                                    minLength: { value: 2, message: 'Must contain at least 2 characters' }
                                })} 
                            />
                            {formState.errors.name && 
                                <span className="error-message">{formState.errors.name.message}</span>
                            }
                        </div>

                        <div className="form-group">
                            <label htmlFor="servings">Servings</label>
                            <input 
                                type="number" 
                                id="servings" 
                                placeholder="Number of servings"
                                min="1"
                                {...register("servings", {
                                    required: { value: true, message: 'Number of servings is required' },
                                    min: { value: 1, message: 'Minimum 1 serving' }
                                })} 
                            />
                            {formState.errors.servings && 
                                <span className="error-message">{formState.errors.servings.message}</span>
                            }
                        </div>

                        <div className="form-group">
                            <label htmlFor="difficulty">Difficulty</label>
                            <select 
                                id="difficulty"
                                {...register("difficulty", {
                                    required: { value: true, message: 'Difficulty is required' }
                                })}
                            >
                                <option value="">Select difficulty</option>
                                <option value="EASY">Easy</option>
                                <option value="MODERATE">Moderate</option>
                                <option value="HARD">Hard</option>
                            </select>
                            {formState.errors.difficulty && 
                                <span className="error-message">{formState.errors.difficulty.message}</span>
                            }
                        </div>
                    </div>

                    <div className="form-section">
                        <h3>Ingredients</h3>
                        <div className="ingredients-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="product">Product</label>
                                    <select 
                                        id="product"
                                        value={selectedProduct}
                                        onChange={(e) => setSelectedProduct(Number(e.target.value))}
                                    >
                                        <option value="0">Select product</option>
                                        {productList.map(product => (
                                            <option key={product.id} value={product.id}>
                                                {product.name}
                                                {/* {product.name} ({product.price_per_gr * 100} NIS/100{product.unit}) */}
                                            </option>
                                        ))}
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label htmlFor="amount">Amount (g/ml)</label>
                                    <input 
                                        type="number" 
                                        id="amount"
                                        min="1"
                                        value={amount}
                                        onChange={(e) => setAmount(Number(e.target.value))}
                                    />
                                </div>

                                <button 
                                    type="button" 
                                    className="add-ingredient-btn"
                                    onClick={addIngredient}
                                >
                                    Add
                                </button>
                            </div>
                        </div>

                        {ingredients.length > 0 && (
                            <div className="ingredients-list">
                                <h4>Added Ingredients</h4>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Product</th>
                                            <th>Amount</th>
                                            <th>Cost</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {ingredients.map((ingredient, index) => (
                                            <tr key={index}>
                                                <td>{ingredient.name}</td>
                                                <td>{ingredient.amount}</td>
                                                <td>{(ingredient.price_per_gr * ingredient.amount).toFixed(2)} NIS</td>
                                                <td>
                                                    <button 
                                                        type="button" 
                                                        className="remove-btn"
                                                        onClick={() => removeIngredient(index)}
                                                    >
                                                        Remove
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colSpan={2}>Total Cost:</td>
                                            <td>{calculateTotalCost()} NIS</td>
                                            <td></td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        )}
                    </div>

                    <div className="form-actions">
                        <button 
                            type="submit" 
                            className="submit-btn" 
                            disabled={isSubmitting || ingredients.length === 0}
                        >
                            {isSubmitting ? 'Creating...' : 'Create Recipe'}
                        </button>
                        <button 
                            type="button" 
                            className="cancel-btn" 
                            onClick={() => navigate('/recipes')}
                            disabled={isSubmitting}
                        >
                            Cancel
                        </button>
                    </div>
                    
                    {ingredients.length === 0 && formState.isSubmitted && (
                        <div className="error-message form-error">
                            Please add at least one ingredient to your recipe.
                        </div>
                    )}
                </form>
            )}
        </div>
    );
}

export default RecipeForm;