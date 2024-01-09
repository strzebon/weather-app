import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import Trips from '../components/Trips';
import TripService from '../services/TripService';

jest.mock('../services/TripService', () => ({
  getTrips: jest.fn(),
}));

jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    ...originalModule,
    useNavigate: jest.fn(),
  };
});

describe('Trips component', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });


  it('renders no saved trips message when no trips fetched', async () => {
    TripService.getTrips.mockResolvedValue([]);
    render(<Trips />);
    await waitFor(() => {
      expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
      expect(screen.getByText('No saved trips has been found.')).toBeInTheDocument();
    });
  });
});
