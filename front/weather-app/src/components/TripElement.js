import { useNavigate } from "react-router-dom"
import TripService from "../services/TripService";
import "../styles/TripElement.css"

export default function TripElement(props) {
    const navigate = useNavigate();

    const handleShowButtonClick = (event) => {
        navigate(`/trips/${props.id}`)
    }

    const handleDeleteButtonClick = (event) => {
        TripService.deleteTrip(props.id)
            .then(() => props.refreshParent())
            .catch(error => console.error(error));
    }

    return (
        <div className="trip-container-element">
            <h3>{props.name}</h3>
            <div className="trip-container-buttons">
                <button className="trip-container-element-delete" onClick={handleDeleteButtonClick}>Delete</button>
                <button className="trip-container-element-show" onClick={handleShowButtonClick}>Show</button>
            </div>
        </div>
    )
}