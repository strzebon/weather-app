import { useState } from "react"


export default function FormInput(props) {
    const [lattitude, setLattitude] = useState("");
    const [longitude, setLongitude] = useState("");
    const [showErrorLattitude, setShowErrorLattitude] = useState(false);
    const [showErrorLongitude, setShowErrorLongitude] = useState(false);


    const lattitudeValidation = (event) => {
        let val = event.target.value;
        setLattitude(val);
        console.log(val);
        if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
            setShowErrorLattitude(true);
            setInputInfo();
            return;
        }
        let floatVal = parseFloat(val); 
        if (floatVal < 90 && floatVal > -90) {
            setShowErrorLattitude(false);
        } else {
            setShowErrorLattitude(true);
        }
        setInputInfo();
    }

    const longitudeValidation = (event) => {
        let val = event.target.value;
        setLongitude(val);
        if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
            setShowErrorLongitude(true);
            setInputInfo();
            return;
        }
        let floatVal = parseFloat(val); 
        if (floatVal < 180 && floatVal > -180) {
            setShowErrorLongitude(false);
        } else {
            setShowErrorLongitude(true);
        }
        setInputInfo();
    }

    const setInputInfo = () => {
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
    }
    
    return (
        <>
            <label htmlFor="lattitude">Lattitude</label>
            <input onChange={lattitudeValidation} name="lattitude" type="text"></input>
            {showErrorLattitude && <p className="form-error">* Lattitude must be between -90 and 90</p>}
            <label htmlFor="longitude">Longitude</label>
            <input onChange={longitudeValidation} name="longitude" type="text"></input>
            {showErrorLongitude && <p className="form-error">* Longitude must be between -180 and 180</p>}
        </>
    )
}