import React from 'react';
import { render, fireEvent, screen, act } from '@testing-library/react';
import TripElement from '../components/TripElement';
import TripService from '../services/TripService';

jest.mock('react-router-dom', () => ({
  useNavigate: () => jest.fn(),
}));

describe('TripElement', () => {
  afterEach(() => {
    jest.restoreAllMocks();
  });

  it('should delete trip on button click', async () => {
    const mockRefreshParent = jest.fn();
    const mockTripId = '1';
    const mockTripName = 'Test Trip';

    jest.spyOn(TripService, 'deleteTrip').mockResolvedValue();
    
    render(
      <TripElement id={mockTripId} name={mockTripName} refreshParent={mockRefreshParent} />
    );
    
    const deleteButton = screen.getByRole('button', {
      name: 'Delete',
    });

    fireEvent.click(deleteButton);

    await act(async () => {
      await Promise.resolve();
    });

    expect(TripService.deleteTrip).toHaveBeenCalledWith(mockTripId);
    expect(mockRefreshParent).toHaveBeenCalled();
  });

  it('should handle delete error', async () => {
    const mockRefreshParent = jest.fn();
    const mockTripId = '1';
    const mockTripName = 'Test Trip';

    jest.spyOn(TripService, 'deleteTrip').mockRejectedValue(new Error('Failed to delete Trip'));

    render(
      <TripElement id={mockTripId} name={mockTripName} refreshParent={mockRefreshParent} />
    );
    
    const deleteButton = screen.getByRole('button', {
      name: 'Delete',
    });

    fireEvent.click(deleteButton);

    expect(TripService.deleteTrip).toHaveBeenCalledWith(mockTripId);
    expect(mockRefreshParent).not.toHaveBeenCalled();
  });
});
