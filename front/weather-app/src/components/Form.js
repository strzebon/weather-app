import "../styles/Form.css"
import { useState } from "react"

export default function Form() {
    const [showErrorLattitude, setShowErrorLattitude] = useState(false);
    const [showErrorLongitude, setShowErrorLongitude] = useState(false);

    const [showSubmitError, setShowSubmitError] = useState(false);

    const [lattitude, setLattitude] = useState("");
    const [longitude, setLongitude] = useState("");

    const [timeoutId, setTimeoutId] = useState(undefined);

    const lattitudeValidation = (event) => {
        let val = event.target.value;
        setLattitude(val);
        if (/^\d+\.\d+$/.test(val)) {
            setShowErrorLattitude(true);
        } else if (val < 90 && val > -90) {
            setShowErrorLattitude(false);
        } else {
            setShowErrorLattitude(true);
        }
    }

    const longitudeValidation = (event) => {
        let val = event.target.value;
        setLongitude(val);
        if (/^\d+\.\d+$/.test(val)) {
            setShowErrorLongitude(true);
        } else if (val < 180 && val > -180) {
            setShowErrorLongitude(false);
        } else {
            setShowErrorLongitude(true);
        }
    }

    const formWeatherRequest = (event) => {
        event.preventDefault();
        if (showErrorLattitude || showErrorLongitude || !longitude || !lattitude) {
            clearTimeout(timeoutId);
            setShowSubmitError(true);
            setTimeoutId(setTimeout(() => {setShowSubmitError(false)}, 3000))
        } else {
            // Get data to service
            // and route to weather view
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