import { useState } from 'react'
import { createRecipe } from '../services/RecipesService'
import { useNavigate } from 'react-router-dom'

/** Form to create a new recipe. */
const RecipeComponent = () => {

    const [name, setName] = useState("")
    const [price, setPrice] = useState("")
    const [coffee, setCoffee] = useState("")
    const [milk, setMilk] = useState("")
    const [sugar, setSugar] = useState("")
    const [chocolate, setChocolate] = useState("")

    const navigator = useNavigate()
    const [errors, setErrors] = useState({
        general: "",
        name: "",
        price: "",
        coffee: "",
        milk: "",
        sugar: "",
        chocolate: "",
    })

    function saveRecipe(e) {
        e.preventDefault();

        if (validateForm()) {
            const recipe = {name, price, coffee, milk, sugar, chocolate}
            console.log(recipe)

            createRecipe(recipe).then((response) => {
                console.log(response.data)
                navigator("/recipes")
            }).catch(error => {
                console.error(error)
                const errorsCopy = {... errors}
                if (error.response.status == 507) {
                    errorsCopy.general = "Recipe list is at capacity."
                } 
                if (error.response.status == 409) {
                    errorsCopy.general = "Duplicate recipe name."
                }

                setErrors(errorsCopy)
            })
        }
    }

    function validateForm() {
        let valid = true
        
        const errorsCopy = {... errors}

        if (name.trim()) {
            errorsCopy.name = ""
        } else {
            errorsCopy.name = "Name is required."
            valid = false
        }

        

        setErrors(errorsCopy)

        return valid
    }

    function getGeneralErrors() {
        if (errors.general) {
            return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
        }
    }

    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-6 offset-md-3">
                    <h2 className="text-center">Add Recipe</h2>

                    <div className="card-body">
                        { getGeneralErrors() }
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Name</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Recipe Name"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    className={`form-control ${errors.name ? "is-invalid":""}`}
                                >
                                </input>
                                {errors.name && <div className="invalid-feedback">{errors.name}</div>}
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Price</label>
                                <input 
                                    type="text"
                                    name="recipePrice"
                                    placeholder="Enter Recipe Price (as an integer)"
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)}
                                    className="form-control"
                                >
                                </input>
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Coffee</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Coffee"
                                    value={coffee}
                                    onChange={(e) => setCoffee(e.target.value)}
                                    className="form-control"
                                >
                                </input>
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Milk</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Milk"
                                    value={milk}
                                    onChange={(e) => setMilk(e.target.value)}
                                    className="form-control"
                                >
                                </input>
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Sugar</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Sugar"
                                    value={sugar}
                                    onChange={(e) => setSugar(e.target.value)}
                                    className="form-control"
                                >
                                </input>
                            </div>

                            <div className="form-group mb-2">
                                <label className="form-label">Amount Chocolate</label>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="Enter Amount Chocolate"
                                    value={chocolate}
                                    onChange={(e) => setChocolate(e.target.value)}
                                    className="form-control"
                                >
                                </input>
                            </div>

                            <button className="btn btn-success" onClick={(e) => saveRecipe(e)}>Submit</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )

}

export default RecipeComponent