import '../styles/Navbar.css';
import { useNavigate, React } from 'react-router-dom';
import logo from '../images/app-icon.png';

export default function Navbar() {
  const navigate = useNavigate();

  return (
    <nav className="nav-container">
      <button className="left-nav" type="submit" onClick={() => navigate('/')} onKeyDown={() => navigate('/')} tabIndex={0}>
        <img src={logo} alt="Sun behind the clouds in a circle" className="nav-icon" />
        <h2 className="nav-title">WeatherApp</h2>
      </button>
      <button type="submit" className="right-nav" onClick={() => navigate('/trips')} onKeyDown={() => navigate('/trips')} tabIndex={0}>
        <h3>My Trips</h3>
      </button>
    </nav>
  );
}
