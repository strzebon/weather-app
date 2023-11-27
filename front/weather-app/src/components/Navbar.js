import "../styles/Navbar.css"
import logo from "../images/app-icon.png"

export default function Navbar() {
    return (
        <nav className="nav-container">
            <img src={logo} alt="Sun behind the clouds in a circle" className="nav-icon"/>
            <h2 className="nav-title">WeatherApp</h2>
        </nav>
    )
}