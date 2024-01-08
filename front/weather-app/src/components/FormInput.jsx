import { useState, useEffect, React } from 'react';
import '../styles/FormInput.css';

export default function FormInput({ handleChange, id }) {
  const [lattitude, setLattitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [showErrorLattitude, setShowErrorLattitude] = useState(true);
  const [showErrorLongitude, setShowErrorLongitude] = useState(true);

  useEffect(() => {
    handleChange(
      {
        coordinates: {
          lat: lattitude,
          lng: longitude,
        },
        validData: !(showErrorLattitude || showErrorLongitude),
      },
      id,
    );
  }, [lattitude, longitude, showErrorLattitude, showErrorLongitude]);

  const lattitudeValidation = (event) => {
    const val = event.target.value;
    setLattitude(val);
    if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
      setShowErrorLattitude(true);
    }
    const floatVal = parseFloat(val);
    if (floatVal < 90 && floatVal > -90) {
      setShowErrorLattitude(false);
    } else {
      setShowErrorLattitude(true);
    }
  };

  const longitudeValidation = (event) => {
    const val = event.target.value;
    setLongitude(val);
    if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
      setShowErrorLongitude(true);
    }
    const floatVal = parseFloat(val);
    if (floatVal < 180 && floatVal > -180) {
      setShowErrorLongitude(false);
    } else {
      setShowErrorLongitude(true);
    }
  };

  return (
    <>
      <h3>
        Location #
        {id + 1}
      </h3>
      <label htmlFor="lattitude">
        Lattitude
        <input onChange={lattitudeValidation} name="lattitude" id="lattitude" type="text" className="latlong-form-input" />
      </label>
      {showErrorLattitude && lattitude !== '' && <p className="form-error">* Lattitude must be between -90 and 90</p>}
      <label htmlFor="longitude">
        Longitude
        <input onChange={longitudeValidation} name="longitude" id="longitude" type="text" className="latlong-form-input" />
      </label>
      {showErrorLongitude && longitude !== '' && <p className="form-error">* Longitude must be between -180 and 180</p>}
    </>
  );
}
