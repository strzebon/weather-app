import React from 'react';
import { useNavigate } from 'react-router-dom';
import TripService from '../services/TripService';
import '../styles/TripElement.css';

export default function TripElement({ id, name, refreshParent }) {
  const navigate = useNavigate();

  const handleShowButtonClick = () => {
    navigate(`/trips/${id}`);
  };

  const handleDeleteButtonClick = () => {
    TripService.deleteTrip(id)
      .then(() => {
        refreshParent();
      })
      .catch((error) => console.error(error));
  };

  return (
    <div className="trip-container-element">
      <h3>{name}</h3>
      <div className="trip-container-buttons">
        <button type="submit" className="trip-container-element-delete" onClick={handleDeleteButtonClick}>Delete</button>
        <button type="submit" className="trip-container-element-show" onClick={handleShowButtonClick}>Show</button>
      </div>
    </div>
  );
}
