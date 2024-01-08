import React from 'react';
import '../styles/WeatherOverview.css';

export default function WeatherOverview({
  tripName, classNames, img, tempC, condition, locations,
}) {
  return (
    <>
      {tripName !== '' && (
        <div className="trip-name">
          Trip Name:
          {' '}
          <h2>{tripName}</h2>
        </div>
      )}
      <div className="weather-overview-container">

        <div className="thermometer-container">
          <div className="logo">
            <div className={`bar ${classNames}-bar`} />
            <div className={`circle ${classNames}-circle`} />
          </div>
        </div>
        <div className="information-container">
          <h2 className="weather-city-name">{locations}</h2>
          <span className="weather-img-container">
            {img.map((weatherImg) => <img src={weatherImg} alt="conditions" className="weather-img-present" />)}
          </span>
          <h3 className="weather-temperature">
            {tempC}
            &#176;C
          </h3>
          <h4 className="weather-condition">{condition}</h4>
        </div>
      </div>
    </>
  );
}
