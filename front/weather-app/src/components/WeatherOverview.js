import "../styles/WeatherOverview.css"

export default function WeatherOverview(props) {
    return (
    <div className="weather-overview-container">
        <h2 className="weather-city-name">{props.location}</h2>
        <img className="weather-img-present" src={require(`../images/${props.img_path}`)} alt={props.condition}/>
        <h3 className="weather-temperature">{props.temp_c}&#176;C</h3>
        <h4 className="weather-condition">{props.condition}</h4>
    </div>
    )
}