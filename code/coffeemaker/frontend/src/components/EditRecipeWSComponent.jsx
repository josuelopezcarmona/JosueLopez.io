/**
 * @author Josue Lopez-Carmona
 */
import {useEffect, useState} from 'react'
import { useParams } from 'react-router-dom'
import {getRecipe} from "../services/RecipesService.js";


const EditRecipeComponent = () => {

    const [name, setName] = useState("")
    const [price, setPrice] = useState("")

    const {incomingName} = useParams()

    useEffect(() => {
        console.log(incomingName)
        if (incomingName) {
            getRecipe(incomingName).then((response) => {
                console.log("here")
                console.log(response.data)
                setName(response.data.name)
                setPrice(response.data.price)
            }).catch(error => {
                console.error(error)
            })
        } else {
            console.log("not here")
        }
    }, [incomingName])

    return (
        <div className='container'>
            <div className='row'>
                <div className='card col-md-6 offset-md-3 offset-md-3'>
                    <h2 className="text-center">Edit Recipe {incomingName}</h2>
                    <div className="card-body">
                        <form>
                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Name</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Enter Recipe Name"
                                    name='name'
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                >
                                </input>
                            </div>
                            <div className="form-group mb-2">
                                <label className="form-label">Recipe Price</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    placeholder="Enter Recipe Price"
                                    name='price'
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)}
                                >
                                </input>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default EditRecipeComponent