import {NavLink} from "react-router-dom";

const AddIngredientComponent = () => {
    return (
        <div className="container">
            <br /><br />
            <div className="row">
                <div className="card col-md-6 offset-md-3">
                    <h2 className="text-center">Add Ingredient</h2>

                    <div className="card-body">
                        <form>
                            <div className="form-group mb-3">
                                <label className="form-label">Ingredient Name</label>
                                <input
                                    type="text"
                                    name="ingredientName"
                                    placeholder="e.g. Vanilla"
                                    className="form-control"
                                />
                            </div>

                            <div className="form-group mb-3">
                                <label className="form-label">Amount</label>
                                <input
                                    type="text"
                                    name="ingredientAmount"
                                    placeholder="e.g. 67"
                                    className="form-control"
                                />
                            </div>

                            <div className="d-flex justify-content-between align-items-center">
                                <NavLink to="/inventory" className="btn btn-secondary">
                                    Cancel
                                </NavLink>
                                <button type="submit" className="btn btn-success">
                                    Add Ingredient
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default AddIngredientComponent
