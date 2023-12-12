import { useEffect, useState } from "react"
import WeatherService from "../services/WeatherService"
import WeatherOverview from "./WeatherOverview";
import "../styles/WeatherView.css"
import DataConvertService from "../services/DataConvertService";

export default function WeatherView() {
    
    const [weatherInfo, setWeatherInfo] = useState(null);

    useEffect(() => {
    const fetchData = async () => {
    try {
        const currentWeather = WeatherService.getCurrentWeather();
        currentWeather.then(data => {
            let imgArray = DataConvertService.getPrecipitation(data.precipitation);
            DataConvertService.getWind(data.isWindy, imgArray);
            let classNames = DataConvertService.getTemperature(data.temperatureLevel);
            let weatherData = {
                img: imgArray,
                classNames: classNames,
                locations: data.locations,
                tempC: data.sensedTemp,
                condition: data.temperatureLevel
            };
            setWeatherInfo(weatherData)
        })
      } catch (error) {
        console.error(error.message);
      }
    };
    fetchData();
        }, [])

    if (!weatherInfo) {
        return null
    }
    
    return (
        <div className="weather-container">
            <WeatherOverview {...weatherInfo}/>
        </div>
    )
}