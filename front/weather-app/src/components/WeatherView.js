import { useEffect, useState } from "react"
import WeatherService from "../services/WeatherService"
import WeatherOverview from "./WeatherOverview";
import "../styles/WeatherView.css"

export default function WeatherView() {
    
    const [weatherInfo, setWeatherInfo] = useState(null);

    useEffect(() => {
    const fetchData = async () => {
    try {
        const currentWeather = await WeatherService.getCurrentWeather();
        setWeatherInfo(currentWeather)
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