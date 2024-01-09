import axios from 'axios';

const enpointURL = 'http://127.0.0.1:8080';

const WeatherService = {
  getWeatherByCoordinates: async (coordinates) => {
    try {
      const response = await axios.post(`${enpointURL}/weather`, coordinates);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch weather data by coordinates');
    }
  },

  getCurrentWeather: async () => {
    try {
      const response = await axios.get(`${enpointURL}/weather/current`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch current weather data');
    }
  },
};

export default WeatherService;
