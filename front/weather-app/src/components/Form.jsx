import '../styles/Form.css';
import { useState, React } from 'react';
import { useNavigate } from 'react-router-dom';
import WeatherService from '../services/WeatherService';
import FormInput from './FormInput';
import TripService from '../services/TripService';

export default function Form() {
  const [data, setData] = useState([{ coordinates: { lat: '', lng: '' }, validData: false }]);

  const handleInputChange = (coordinatesData, id) => {
    setData((prevData) => prevData.map((item, index) => (index === id ? coordinatesData : item)));
  };

  const [inputComponents, setInputComponents] = useState(
    [<FormInput key={0} id={0} handleChange={handleInputChange} />],
  );

  const [inputCount, setInputCount] = useState(1);

  const [showSubmitError, setShowSubmitError] = useState(false);
  const [timeoutId, setTimeoutId] = useState(undefined);

  const [saveTrip, setSaveTrip] = useState(false);
  const [tripName, setTripName] = useState('');

  const navigate = useNavigate();

  const handleAddClick = (event) => {
    event.preventDefault();
    if (inputCount < 5) {
      setInputComponents([...inputComponents,
        <FormInput key={inputCount} id={inputCount} handleChange={handleInputChange} />]);
      setData((arr) => [...arr, { coordinates: { lat: '', lng: '' }, validData: false }]);
      setInputCount(inputCount + 1);
    }
  };

  const handleRemoveClick = (event) => {
    event.preventDefault();
    if (inputCount > 1) {
      setInputComponents((arr) => arr.slice(0, -1));
      setData((arr) => arr.slice(0, -1));
      setInputCount(inputCount - 1);
    }
  };

  const handleCheckboxClick = () => {
    setSaveTrip(!saveTrip);
  };

  const handleNameInputChange = (event) => {
    setTripName(event.target.value);
  };

  const checkData = () => data.find((element) => element.validData === false) || (saveTrip && tripName === '');

  const formWeatherRequest = (event) => {
    event.preventDefault();
    if (checkData()) {
      clearTimeout(timeoutId);
      setShowSubmitError(true);
      setTimeoutId(setTimeout(() => { setShowSubmitError(false); }, 3000));
    } else if (saveTrip) {
      TripService.addNewTrip(
        { name: tripName, locations: data.map((element) => element.coordinates) },
      )
        .then((responseData) => { navigate(`/trips/${responseData.id}`); })
        .catch((error) => console.log(error));
    } else {
      sessionStorage.setItem('lastTrip', JSON.stringify(data.map((element) => element.coordinates)));
      WeatherService.getWeatherByCoordinates(data.map((element) => element.coordinates))
        .then(() => {
          navigate('/weather');
        })
        .catch((error) => console.log(error));
    }
  };

  return (
    <div className="main-container">
      <form className="latlong-form">
        <h1>Weather Form</h1>
        {inputComponents}
        <div className="latlong-form-buttons">
          <button type="submit" onClick={handleAddClick} className={`latlong-form-${inputCount === 5 ? 'disabled' : 'add'} material-symbols-outlined`}>add_circle</button>
          <button type="submit" onClick={handleRemoveClick} className={`latlong-form-${inputCount === 1 ? 'disabled' : 'remove'} material-symbols-outlined`}>cancel</button>
        </div>
        <div className="latlong-save-trip-container">
          <label htmlFor="save-checkbox">
            Save trip
            <input type="checkbox" id="save-checkbox" onChange={handleCheckboxClick} className="latlong-save-checkbox" />
          </label>
          <input disabled={!saveTrip} onChange={handleNameInputChange} className="latlong-save-input" placeholder="trip name..." />
        </div>
        {saveTrip && tripName === '' && <p className="form-error">* Invalid trip name</p>}
        <button type="submit" onClick={formWeatherRequest} className="latlong-form-submit">Get Weather ⛅</button>
        {showSubmitError && <p className="form-error">* Invalid data in the form</p>}
      </form>
    </div>
  );
}
