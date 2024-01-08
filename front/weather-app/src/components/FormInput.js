import { useState, useEffect } from "react"
import "../styles/FormInput.css"


export default function FormInput(props) {
    const [lattitude, setLattitude] = useState("");
    const [longitude, setLongitude] = useState("");
    const [showErrorLattitude, setShowErrorLattitude] = useState(true);
    const [showErrorLongitude, setShowErrorLongitude] = useState(true);

    useEffect(() => {
        props.handleChange(
            {
                coordinates: {
                    lat: lattitude,
                    lng: longitude
                },
                validData: !(showErrorLattitude || showErrorLongitude)
            },
            props.id
        )
    }, [lattitude, longitude, showErrorLattitude, showErrorLongitude, props]);


    const lattitudeValidation = (event) => {
        let val = event.target.value;
        setLattitude(val);
        if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
            setShowErrorLattitude(true);
        }
        let floatVal = parseFloat(val); 
        if (floatVal < 90 && floatVal > -90) {
            setShowErrorLattitude(false);
        } else {
            setShowErrorLattitude(true);
        }
    }

    const longitudeValidation = (event) => {
        let val = event.target.value;
        setLongitude(val);
        if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
            setShowErrorLongitude(true);
        }
        let floatVal = parseFloat(val); 
        if (floatVal < 180 && floatVal > -180) {
            setShowErrorLongitude(false);
        } else {
            setShowErrorLongitude(true);
        }
    }
    
    return (
        <>
            <h3>Location #{props.id + 1}</h3>
            <label htmlFor="lattitude">Lattitude</label>
            <input onChange={lattitudeValidation} name="lattitude" type="text"></input>
            {showErrorLattitude && lattitude !== "" && <p className="form-error">* Lattitude must be between -90 and 90</p>}
            <label htmlFor="longitude">Longitude</label>
            <input onChange={longitudeValidation} name="longitude" type="text"></input>
            {showErrorLongitude && longitude !== "" && <p className="form-error">* Longitude must be between -180 and 180</p>}
        </>
    )
}