import { useEffect, useState } from "react";
import TripService from "../services/TripService";
import TripElement from "./TripElement";
import "../styles/Trips.css"


export default function Trips() {
    
    const [tripsList, setTripsList] = useState([]);
    const [tripsFetched, setTripsFetched] = useState(false);
    
    const [refreshComponent, setRefreshComponent] = useState(false);

    useEffect(() => {
        TripService.getTrips()
            .then(data => {
                    setTripsList(data);
                    setTripsFetched(true);
                }
        ).catch(error => {
            console.error(error.message);
            setTripsFetched(true);
        });
      }
    ,[refreshComponent])

    const refreshParent = () => {
        setRefreshComponent(!refreshComponent);
    }
    
    if (!tripsFetched) {
        return (
            <></>
        )
    }
    else if (tripsFetched && !tripsList.length) {
        return (
        <div className="main-container">
            <div className="trips-container">
                No saved trips has been found.
            </div>
        </div>
        )
    }
    else {
        return (
            <div className="main-container">
                <div className="trips-container">
                    {tripsList.map(trip => <TripElement id={trip.id} key={trip.id} name={trip.name} refreshParent={refreshParent}/>)}
                </div>
            </div>
        )
    }
}