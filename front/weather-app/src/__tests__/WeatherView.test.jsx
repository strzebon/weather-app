import React from 'react';
import {
  render, fireEvent, waitFor, screen, act,
} from '@testing-library/react';
import * as ReactRouterDom from 'react-router-dom';
import WeatherView from '../components/WeatherView';
import WeatherService from '../services/WeatherService';
import DataConvertService from '../services/DataConvertService';
import TripService from '../services/TripService';

jest.mock('../services/WeatherService');
jest.mock('../services/DataConvertService');
jest.mock('../services/TripService');
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useLocation: jest.fn(),
  useNavigate: jest.fn(),
  useParams: jest.fn(),
}));

describe('WeatherView component', () => {
  beforeEach(() => {
    WeatherService.getCurrentWeather.mockResolvedValue({
      precipitation: ['RAIN'],
      isWindy: true,
      isMuddy: false,
      temperatureLevel: 'TEMPERATURE',
      locations: ['Location A', 'Location B'],
      sensedTemp: 20.5,
    });
    TripService.getTripById.mockResolvedValue({
      tripName: 'TEST',
      precipitation: ['RAIN'],
      isWindy: true,
      isMuddy: false,
      temperatureLevel: 'TEMPERATURE',
      locations: ['Location A', 'Location B'],
      sensedTemp: 20.5,
    });
    DataConvertService.getWeatherInfo.mockReturnValue({
    });
    ReactRouterDom.useLocation.mockReturnValue({ pathname: '/trips' });
    ReactRouterDom.useParams.mockReturnValue({ id: '123' });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('renders trip weather when in trip view', async () => {
    ReactRouterDom.useLocation.mockReturnValue({ pathname: '/trips/123' });
    render(<WeatherView />);
    await waitFor(() => {
      expect(TripService.getTripById).toHaveBeenCalledWith('123');
      expect(WeatherService.getWeatherByCoordinates).toHaveBeenCalled();
    });
  });
});
