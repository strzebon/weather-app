import { useNavigate } from "react-router-dom"

export default function TripElement(props) {
    const navigate = useNavigate();

    const handleGoButtonClick = (event) => {
        navigate(`/trips/${props.id}`)
    }

    return (
        <div className="trip-container-element">
            <h3>{props.name}</h3>
            <button onClick={handleGoButtonClick}>Go</button>
        </div>
    )
}