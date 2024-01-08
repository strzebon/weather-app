import "../styles/WeatherOverview.css"

export default function WeatherOverview(props) {
    return (
        <>
        {props.tripName !== "" && <div className="trip-name">Trip Name: <h2>{props.tripName}</h2></div>}
    <div className="weather-overview-container">
        
        <div className="thermometer-container">
            <div className="logo">
                <div className={`bar ${props.classNames}-bar`}></div>
                <div className={`circle ${props.classNames}-circle`}></div>
            </div>
        </div>
        <div className="information-container">
            <h2 className="weather-city-name">{props.locations}</h2>
            <span className="weather-img-container">{props.img.map(img => <img src={img} alt="conditions" className="weather-img-present"/>)}</span>
            <h3 className="weather-temperature">{props.tempC}&#176;C</h3>
            <h4 className="weather-condition">{props.condition}</h4>
        </div>
    </div>
    </>
    )
}