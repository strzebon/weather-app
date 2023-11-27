import axios from 'axios';

const enpointURL = 'http://127.0.0.1:8000';

const WeatherService = {
  getWeatherByCoordinates: async (lat, long) => {
    try {
      const response = await axios.get(`${enpointURL}/weather`, {
        params: {
          lat,
          long,
        },
      });
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch weather data by coordinates');
    }
  },

  getCurrentWeather: async () => {
    try {
      const response = await axios.get(`${enpointURL}/current`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch current weather data');
    }
  },
};

export default WeatherService;
