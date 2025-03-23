interface Ingredient {
    id: number,
    name: string,
    recipeId: number,
    productId: number,
    amount: number,
    unit: string,
    price_per_gr: number,
    img: string
}

export default Ingredient;