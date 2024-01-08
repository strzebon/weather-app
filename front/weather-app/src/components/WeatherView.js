import { useEffect, useState } from "react"
import WeatherService from "../services/WeatherService"
import WeatherOverview from "./WeatherOverview";
import "../styles/WeatherView.css"
import DataConvertService from "../services/DataConvertService";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import TripService from "../services/TripService";

export default function WeatherView() {
    
    const [weatherInfo, setWeatherInfo] = useState(null);
    const [weatherTripName, setWeatherTripName] = useState("");
    const [savedTrip, setSavedTrip] = useState(false);

    const [tripName, setTripName] = useState("");
    const [tripNameError, setTripNameError] = useState(false);

    const location = useLocation();
    const { id } = useParams();

    const navigate = useNavigate();

    useEffect(() => {
    const fetchData = async () => {
    try {
        if (location.pathname.includes("trips")) {
            TripService.getTripById(id)
                    .then(data => {
                        setWeatherTripName(data.name);
                        console.log(data.name);
                        setSavedTrip(true);
                        WeatherService.getWeatherByCoordinates(data.locations)
                            .then(data => {
                                setWeatherInfo(DataConvertService.getWeatherInfo(data))
                            }).catch(error => console.error(error.message));
                }).catch(error => console.error(error.message));
        }
        else {
            WeatherService.getCurrentWeather()
                .then(data => {
                    setWeatherInfo(DataConvertService.getWeatherInfo(data))
            }).catch(error => console.error(error.message));
        }
      } catch (error) {
        console.error(error.message);
      }
    };
    fetchData();
        }, [location, id])


    const handleNameChange = (event) => {
        setTripName(event.target.value);
        setTripNameError(false);
    }


    const handleSaveButton = (event) => {
        if (tripName === "") {
            setTripNameError(true);
        }
        else {
            TripService.addNewTrip({locations: JSON.parse(sessionStorage.lastTrip), name: tripName})
                .then(data => {navigate(`/trips/${data.id}`)})
                .catch(error => console.error(error.message));
        }
    }

    const handleDeleteButton = (event) => {
        TripService.deleteTrip(id)
            .then(data => navigate("/trips"))
            .catch(error => console.error(error));
    }


    if (!weatherInfo) {
        return null
    }
    
    return (
        <div className="main-container">
            <div className="weather-container">
                {weatherInfo && <WeatherOverview {...weatherInfo} tripName={weatherTripName}/>}
                {!savedTrip && 
                <div className="save-trip-input">
                    <label htmlFor="trip-name">Trip name:</label>
                    <input name="trip-name" onChange={handleNameChange}/>
                    <button onClick={handleSaveButton}>Save</button>
                </div>}
                {savedTrip && 
                <button className="delete-button" onClick={handleDeleteButton}>Delete</button>
                }
                {tripNameError && <p className="form-error">* Invalid trip name</p>}
            </div>
        </div>
    )
}