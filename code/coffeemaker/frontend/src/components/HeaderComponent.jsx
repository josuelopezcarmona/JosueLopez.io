import { NavLink } from 'react-router-dom'

/** Code for the Header of the webpage. */
const HeaderComponent = () => {
  return (
    <div>
        <header>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
                <div className="container-fluid">
                  <NavLink className="navbar-brand" to="/">Coffee Maker</NavLink>
                  <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarNav"
                    aria-controls="navbarNav"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                  >
                    <span className="navbar-toggler-icon"></span>
                  </button>
                  <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">
                      <li className="nav-item">
                        <NavLink className="nav-link" to="/inventory">Inventory</NavLink>
                      </li>
                      <li className="nav-item">
                        <NavLink className="nav-link" to="/recipes">Recipes</NavLink>
                      </li>
                      <li className="nav-item">
                        <NavLink className="nav-link" to="/make-recipe">Make Recipe</NavLink>
                      </li>
                    </ul>
                  </div>
                </div>
            </nav>
        </header>
    </div>
  )
}

export default HeaderComponent
