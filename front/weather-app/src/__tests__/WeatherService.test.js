import axios from 'axios';
import WeatherService from '../services/WeatherService';

jest.mock('axios');

describe('WeatherService', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should fetch weather by coordinates', async () => {
    const coordinates = { lat: 40.7128, lon: -74.006 };
    const mockWeatherData = { temperature: 20, condition: 'Sunny' };
    axios.post.mockResolvedValue({ data: mockWeatherData });

    const weather = await WeatherService.getWeatherByCoordinates(coordinates);

    expect(axios.post).toHaveBeenCalledWith('http://127.0.0.1:8080/weather', coordinates);
    expect(weather).toEqual(mockWeatherData);
  });

  it('should handle error when fetching weather by coordinates', async () => {
    const coordinates = { lat: 40.7128, lon: -74.006 };
    axios.post.mockRejectedValue(new Error('Failed to fetch weather'));

    await expect(WeatherService.getWeatherByCoordinates(coordinates)).rejects.toThrow(
      'Failed to fetch weather data by coordinates'
    );
  });

  it('should fetch current weather', async () => {
    const mockCurrentWeather = { temperature: 25, condition: 'Cloudy' };
    axios.get.mockResolvedValue({ data: mockCurrentWeather });

    const currentWeather = await WeatherService.getCurrentWeather();

    expect(axios.get).toHaveBeenCalledWith('http://127.0.0.1:8080/weather/current');
    expect(currentWeather).toEqual(mockCurrentWeather);
  });

  it('should handle error when fetching current weather', async () => {
    axios.get.mockRejectedValue(new Error('Failed to fetch current weather'));

    await expect(WeatherService.getCurrentWeather()).rejects.toThrow('Failed to fetch current weather data');
  });
});
