import { useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom'
import { getInventory, updateInventory } from '../services/InventoryService'

/** Creates the page for viewing and updating the inventory. */
const InventoryComponent = () => {

    const [coffee, setCoffee] = useState("")
    const [milk, setMilk] = useState("")
    const [sugar, setSugar] = useState("")
    const [chocolate, setChocolate] = useState("")

    const [errors, setErrors] = useState({
        general: "",
        coffee: "",
        milk: "",
        sugar: "",
        chocolate: "",
    })

    useEffect(() => {
        getInventory().then((response) => {
            setCoffee(response.data.coffee)
            setMilk(response.data.milk)
            setSugar(response.data.sugar)
            setChocolate(response.data.chocolate)
        }).catch(error => {
            console.error(error)
        })
    }, [])

    function modifyInventory(e) {
        e.preventDefault()

        if (validateForm()) {
            const inventory = {coffee, milk, sugar, chocolate}
            console.log(inventory)

            updateInventory(inventory).then((response) => {
                console.log(response.data)
            }).catch(error => {
                console.error(error)
            })
        }
    }

    function validateForm() {
        let valid = true

        const errorsCopy = {... errors}
		
		const inventory = {coffee, milk, sugar, chocolate}
		console.log(inventory)

		if (coffee < 0) {
            errorsCopy.coffee = "Coffee amount must be a positive integer"
			valid = false;
        }

		if (milk < 0) {
            errorsCopy.milk = "Milk amount must be a positive integer"
			valid = false;
        }
		
		if (sugar < 0) {
            errorsCopy.sugar = "Sugar amount must be a positive integer"
			valid = false;
        }

		if (chocolate < 0) {
            errorsCopy.chocolate = "Chocolate amount must be a positive integer"
			valid = false;
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
                    <h2 className="text-center">Inventory</h2>

                    <div className="card-body">
                        { getGeneralErrors() }
                        <form>
                            <div className="form-group mb-2 d-flex align-items-center">
                                <label className="form-label mb-0 me-2">Coffee</label>
                                <span className="text-muted me-2">10</span>
                                <input 
                                    type="text"
                                    name="recipeName"
                                    placeholder="New Amount..."
                                    value={coffee}
                                    onChange={(e) => setCoffee(e.target.value)}
                                    className={`form-control ${errors.coffee ? "is-invalid":""}`}
                                    style={{ maxWidth: '200px', marginLeft: 'auto', textAlign: 'right' }}
                                >
                                </input>
								{errors.coffee && <div className="invalid-feedback">{errors.coffee}</div>}
                            </div>

                            <div className="form-group mb-2 d-flex align-items-center">
                                <label className="form-label mb-0 me-2">Splenda</label>
                                <span className="text-muted me-2">20</span>
                                <input
                                    type="text"
                                    name="recipeName"
                                    placeholder="New Amount..."
                                    value={null}
                                    onChange={(e) => null}
                                    className={`form-control ${errors.null ? "is-invalid":""}`}
                                    style={{ maxWidth: '200px', marginLeft: 'auto', textAlign: 'right' }}
                                >
                                </input>
                                {errors.null && <div className="invalid-feedback">{errors.null}</div>}
                            </div>

                            <div className="d-flex justify-content-between align-items-center">
                                <NavLink to="/add-ingredient" className="btn btn-secondary" to="/add-ingredient">Add Ingredient...</NavLink>
                                <button className="btn btn-success" onClick={(e) => modifyInventory(e)}>Update Inventory</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default InventoryComponent