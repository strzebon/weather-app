import "../styles/Form.css"
import { useState } from "react"
import WeatherService from "../services/WeatherService";
import { useNavigate } from "react-router-dom";
import FormInput from "./FormInput";

export default function Form() {

    const handleInputChange = (coordinatesData, id) => {
        setData(prevData => prevData.map((item, index) => (index === id ? coordinatesData : item)));
    }

    const [inputComponents, setInputComponents] = useState([<FormInput key={0} id={0} handleChange={handleInputChange}/>]);

    const [inputCount, setInputCount] = useState(1);
    const [data, setData] = useState([{coordinates: {lat: "", lng: ""}, validData: false}])

    const [showSubmitError, setShowSubmitError] = useState(false);
    const [timeoutId, setTimeoutId] = useState(undefined);

    const navigate = useNavigate();


    const handleAddClick = (event) => {
        event.preventDefault()
        if (inputCount < 5) {
            setInputComponents(arr => [...arr, <FormInput key={inputCount} id={inputCount} handleChange={handleInputChange}/>])
            setData(arr => [...arr, {coordinates: {lat: "", lng: ""}, validData: false}]);
            setInputCount(inputCount => inputCount + 1);
        }
    }

    const handleRemoveClick = (event) => {
        event.preventDefault()
        if (inputCount > 1) {
            setInputComponents(arr => arr.slice(0, -1));
            setData(arr => arr.slice(0, -1));
            setInputCount(inputCount => inputCount - 1)
        }
    }

    const checkData = () => {
        return data.find(element => element.validData === false);
    }

    const formWeatherRequest = (event) => {
        event.preventDefault();
        if (checkData()) {
            clearTimeout(timeoutId);
            setShowSubmitError(true);
            setTimeoutId(setTimeout(() => {setShowSubmitError(false)}, 3000))
        } else {
            WeatherService.getWeatherByCoordinates(data.map(element => element.coordinates))
                .then(navigate("/weather"))
                .catch(error => console.log(error));
        }
    }

    return (
        <form className="latlong-form">
            <h1>Weather Form</h1>
            {inputComponents}
            <div className="latlong-form-buttons">
                <button onClick={handleAddClick} className="latlong-form-add material-symbols-outlined">add_circle</button>
                <button onClick={handleRemoveClick} className="latlong-form-remove material-symbols-outlined">cancel</button>
            </div>
            <button onClick={formWeatherRequest} className="latlong-form-submit">Get Weather ⛅</button>
            {showSubmitError && <p className="form-error">* Invalid data in the form</p>}
        </form>
    )
}