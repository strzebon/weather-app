import rainLogo from '../images/rain.png';
import sunLogo from '../images/sunny.png';
import windLogo from '../images/wind.png';
import snowLogo from '../images/snow.png';
import mudLogo from '../images/mud.png';

const DataConvertService = {
  getTemperature: (sensedTemp) => sensedTemp.toLowerCase(),
  getPrecipitation: (precipitation) => {
    const imgArray = [];
    precipitation.forEach((element) => {
      if (element === 'RAIN') {
        imgArray.push(rainLogo);
      } else if (element === 'SNOW') {
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
  getMud: (mud, imgArray) => {
    if (mud) {
      imgArray.push(mudLogo);
    }
  },
  getLocations: (locations) => locations.join(', '),
  getWeatherInfo: (data) => {
    const imgArray = DataConvertService.getPrecipitation(data.precipitation);
    DataConvertService.getWind(data.isWindy, imgArray);
    DataConvertService.getMud(data.isMuddy, imgArray);
    const classNames = DataConvertService.getTemperature(data.temperatureLevel);
    const weatherData = {
      img: imgArray,
      classNames,
      locations: DataConvertService.getLocations(data.locations),
      tempC: Math.round(data.sensedTemp),
      condition: data.temperatureLevel,
    };
    return weatherData;
  },
};

export default DataConvertService;
