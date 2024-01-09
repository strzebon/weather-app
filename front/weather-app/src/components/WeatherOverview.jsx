import React from 'react';
import PropTypes from 'prop-types';
import '../styles/WeatherOverview.css';

function WeatherOverview({
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
            {img.map((weatherImg) => (
              <img key={weatherImg} src={weatherImg} alt="conditions" className="weather-img-present" />
            ))}

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

WeatherOverview.propTypes = {
  tripName: PropTypes.string.isRequired,
  classNames: PropTypes.string.isRequired,
  img: PropTypes.arrayOf(PropTypes.string).isRequired,
  tempC: PropTypes.number.isRequired,
  condition: PropTypes.string.isRequired,
  locations: PropTypes.string.isRequired,
};

export default WeatherOverview;
