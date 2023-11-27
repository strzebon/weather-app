import "../styles/Form.css"
import { useState } from "react"
import WeatherService from "../services/WeatherService";
import { useNavigate } from "react-router-dom";

export default function Form() {
    const [showErrorLattitude, setShowErrorLattitude] = useState(false);
    const [showErrorLongitude, setShowErrorLongitude] = useState(false);

    const [showSubmitError, setShowSubmitError] = useState(false);

    const [lattitude, setLattitude] = useState("");
    const [longitude, setLongitude] = useState("");

    const [timeoutId, setTimeoutId] = useState(undefined);

    const navigate = useNavigate();

    const lattitudeValidation = (event) => {
        let val = event.target.value;
        setLattitude(val);
        console.log(val);
        if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
            setShowErrorLattitude(true);
            return;
        }
        let floatVal = parseFloat(val); 
        if (floatVal < 90 && floatVal > -90) {
            setShowErrorLattitude(false);
        } else {
            setShowErrorLattitude(true);
        }
    }

    const longitudeValidation = (event) => {
        let val = event.target.value;
        setLongitude(val);
        if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
            setShowErrorLongitude(true);
            return;
        }
        let floatVal = parseFloat(val); 
        if (floatVal < 180 && floatVal > -180) {
            setShowErrorLongitude(false);
        } else {
            setShowErrorLongitude(true);
        }
    }

    const formWeatherRequest = async (event) => {
        event.preventDefault();
        if (showErrorLattitude || showErrorLongitude || !longitude || !lattitude) {
            clearTimeout(timeoutId);
            setShowSubmitError(true);
            setTimeoutId(setTimeout(() => {setShowSubmitError(false)}, 3000))
        } else {
            try {
                await WeatherService.getWeatherByCoordinates(lattitude, longitude);
            } catch (error) {
                console.error(error.message);
            }
            navigate("/weather");
        }
    }

    return (
        <form className="latlong-form">
            <h1>Weather Form</h1>
            <label htmlFor="lattitude">Lattitude</label>
            <input onChange={lattitudeValidation} name="lattitude" type="text"></input>
            {showErrorLattitude && <p className="form-error">* Lattitude must be between -90 and 90</p>}
            <label htmlFor="longitude">Longitude</label>
            <input onChange={longitudeValidation} name="longitude" type="text"></input>
            {showErrorLongitude && <p className="form-error">* Longitude must be between -180 and 180</p>}
            <button onClick={formWeatherRequest}>Get Weather ⛅</button>
            {showSubmitError && <p className="form-error">* Invalid data in the form</p>}
        </form>
    )
}