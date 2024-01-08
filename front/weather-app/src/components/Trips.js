import { useEffect, useState } from "react";
import TripService from "../services/TripService";
import TripElement from "./TripElement";


export default function Trips() {
    
    const [tripsList, setTripsList] = useState([]);
    const [tripsFetched, setTripsFetched] = useState(false);
    

    useEffect(() => {
        TripService.getTrips()
            .then(data => {
                    setTripsList(data.trips);
                    setTripsFetched(true);
                }
        ).catch(error => {
            console.error(error.message);
            setTripsFetched(true);
        });
      }
    ,[])
    
    if (!tripsFetched) {
        return (
            <></>
        )
    }
    else if (tripsFetched && !tripsList.length) {
        return (
        <div className="trips-container">
            No saved trips has been found.
        </div>
        )
    }
    else {
        return (
            <div className="trips-container">
                {tripsList.map(trip => <TripElement id={trip.id} key={trip.id} name={trip.name} />)}
            </div>
        )
    }
}