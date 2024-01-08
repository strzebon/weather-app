import { useEffect, useState, React } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import WeatherService from '../services/WeatherService';
import '../styles/WeatherView.css';
import DataConvertService from '../services/DataConvertService';
import TripService from '../services/TripService';
import WeatherOverview from './WeatherOverview';

export default function WeatherView() {
  const [weatherInfo, setWeatherInfo] = useState(null);
  const [weatherTripName, setWeatherTripName] = useState('');
  const [savedTrip, setSavedTrip] = useState(false);

  const [tripName, setTripName] = useState('');
  const [tripNameError, setTripNameError] = useState(false);

  const location = useLocation();
  const { id } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (location.pathname.includes('trips')) {
          TripService.getTripById(id)
            .then((data) => {
              setWeatherTripName(data.name);
              setSavedTrip(true);
              WeatherService.getWeatherByCoordinates(data.locations)
                .then((weatherData) => {
                  setWeatherInfo(DataConvertService.getWeatherInfo(weatherData));
                }).catch((error) => console.error(error.message));
            }).catch((error) => console.error(error.message));
        } else {
          WeatherService.getCurrentWeather()
            .then((data) => {
              setWeatherInfo(DataConvertService.getWeatherInfo(data));
            }).catch((error) => console.error(error.message));
        }
      } catch (error) {
        console.error(error.message);
      }
    };
    fetchData();
  }, [location, id]);

  const handleNameChange = (event) => {
    setTripName(event.target.value);
    setTripNameError(false);
  };

  const handleSaveButton = () => {
    if (tripName === '') {
      setTripNameError(true);
    } else {
      TripService.addNewTrip({ locations: JSON.parse(sessionStorage.lastTrip), name: tripName })
        .then((data) => { navigate(`/trips/${data.id}`); })
        .catch((error) => console.error(error.message));
    }
  };

  const handleDeleteButton = () => {
    TripService.deleteTrip(id)
      .then(() => navigate('/trips'))
      .catch((error) => console.error(error));
  };

  if (!weatherInfo) {
    return null;
  }

  return (
    <div className="main-container">
      <div className="weather-container">
        {weatherInfo && (
        <WeatherOverview
          classNames={weatherInfo.classNames}
          img={weatherInfo.img}
          tempC={weatherInfo.tempC}
          condition={weatherInfo.condition}
          locations={weatherInfo.locations}
          tripName={weatherTripName}
        />
        )}
        {!savedTrip
                && (
                <div className="save-trip-input">
                  <label htmlFor="trip-name">
                    Trip name:
                    <input id="trip-name" name="trip-name" onChange={handleNameChange} />
                  </label>
                  <button type="submit" onClick={handleSaveButton}>Save</button>
                </div>
                )}
        {savedTrip
                && <button type="submit" className="delete-button" onClick={handleDeleteButton}>Delete</button>}
        {tripNameError && <p className="form-error">* Invalid trip name</p>}
      </div>
    </div>
  );
}
