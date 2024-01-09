import React from 'react';
import {
  render, screen, fireEvent,
} from '@testing-library/react';
import Form from '../components/Form';

jest.mock('../services/TripService', () => ({
  addNewTrip: jest.fn().mockResolvedValue({ id: 1 }),
}));

jest.mock('../services/WeatherService', () => ({
  getWeatherByCoordinates: jest.fn().mockResolvedValue({}),
}));

const mockNavigate = jest.fn();

jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    ...originalModule,
    useNavigate: () => mockNavigate,
  };
});

describe('form component', () => {
  it('renders form elements correctly', () => {
    expect.assertions(2);
    render(<Form />);
    expect(screen.getByText('Weather Form')).toBeInTheDocument();
    expect(screen.getByText('Get Weather ⛅')).toBeInTheDocument();
  });

  it('handles trip name input change', () => {
    expect.assertions(1);
    render(<Form />);
    const tripNameInput = screen.getByPlaceholderText('trip name...');
    fireEvent.change(tripNameInput, { target: { value: 'My Trip' } });
    expect(tripNameInput.value).toBe('My Trip');
  });
});
