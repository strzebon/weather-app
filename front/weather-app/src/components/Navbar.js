import "../styles/Navbar.css"
import logo from "../images/app-icon.png"
import { useNavigate } from "react-router-dom"

export default function Navbar() {

    const navigate = useNavigate();

    return (
        <nav className="nav-container">
            <div className="left-nav">
                <img src={logo} alt="Sun behind the clouds in a circle" className="nav-icon"/>
                <h2 className="nav-title" onClick={(event) => navigate("/")}>WeatherApp</h2>
            </div>
            <h3 className="right-nav" onClick={(event) => navigate("/trips")}>My Trips</h3>
        </nav>
    )
}