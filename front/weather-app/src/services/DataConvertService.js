import rainLogo from "../images/rain.png"
import sunLogo from "../images/sunny.png"
import windLogo from "../images/wind.png"
import snowLogo from "../images/snow.png"

const DataConvertService = {
    getTemperature: (sensedTemp) => {
        return sensedTemp.toLowerCase()
    },
    getPrecipitation: (precipitation) => {
        let imgArray = [];
        precipitation.forEach(element => {
            if (element === "RAIN") {
                imgArray.push(rainLogo);
            }
            else if (element === "SNOW") {
                imgArray.push(snowLogo);
            }
        });
        if (imgArray.length === 0) {
            imgArray.push(sunLogo);
        }
        return imgArray;
    },
    getWind: (wind, imgArray) => {
        if (wind) {
            imgArray.push(windLogo);
        }
    },
    getLocations: (locations) => {
        return locations.join(", ");
    }
}

export default DataConvertService;