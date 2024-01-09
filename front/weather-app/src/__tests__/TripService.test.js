import axios from 'axios';
import TripService from '../services/TripService';

jest.mock('axios');

describe('tripService', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should fetch trips', async () => {
    expect.assertions(2);
    const mockTrips = [{ id: 1, name: 'Trip 1' }, { id: 2, name: 'Trip 2' }];
    axios.get.mockResolvedValue({ data: mockTrips });

    const trips = await TripService.getTrips();

    expect(axios.get).toHaveBeenCalledWith('http://127.0.0.1:8080/trips');
    expect(trips).toStrictEqual(mockTrips);
  });

  it('should fetch trip by id', async () => {
    expect.assertions(2);
    const mockTrip = { id: 1, name: 'Trip 1' };
    const tripId = 1;
    axios.get.mockResolvedValue({ data: mockTrip });

    const trip = await TripService.getTripById(tripId);

    expect(axios.get).toHaveBeenCalledWith(`http://127.0.0.1:8080/trips/${tripId}`);
    expect(trip).toStrictEqual(mockTrip);
  });

  it('should add new trip', async () => {
    expect.assertions(2);
    const newTrip = { name: 'New Trip' };
    const mockResponse = { success: true };
    axios.post.mockResolvedValue({ data: mockResponse });

    const response = await TripService.addNewTrip(newTrip);

    expect(axios.post).toHaveBeenCalledWith('http://127.0.0.1:8080/trips', newTrip);
    expect(response).toStrictEqual(mockResponse);
  });

  it('should delete trip by id', async () => {
    expect.assertions(2);
    const tripId = 1;
    const mockResponse = { success: true };
    axios.delete.mockResolvedValue({ data: mockResponse });

    const response = await TripService.deleteTrip(tripId);

    expect(axios.delete).toHaveBeenCalledWith(`http://127.0.0.1:8080/trips/${tripId}`);
    expect(response).toStrictEqual(mockResponse);
  });

  it('should handle errors when fetching trips', async () => {
    expect.assertions(1);
    axios.get.mockRejectedValue(new Error('Failed to fetch trips'));

    await expect(TripService.getTrips()).rejects.toThrow('Failed to fetch trips');
  });

  it('should handle network error when fetching trips', async () => {
    expect.assertions(1);
    axios.get.mockRejectedValue(new Error('Network Error'));

    await expect(TripService.getTrips()).rejects.toThrow('Failed to fetch trips');
  });

  it('should handle 404 error when fetching trip by id', async () => {
    expect.assertions(1);
    const tripId = 123;
    axios.get.mockRejectedValue({ response: { status: 404 } });

    await expect(TripService.getTripById(tripId)).rejects.toThrow('Failed to fetch trip of id: 123');
  });

  it('should handle 500 error when adding new trip', async () => {
    expect.assertions(1);
    const newTrip = { name: 'New Trip' };
    axios.post.mockRejectedValue({ response: { status: 500 } });

    await expect(TripService.addNewTrip(newTrip)).rejects.toThrow('Failed to post new Trip');
  });

  it('should handle generic error when deleting trip', async () => {
    expect.assertions(1);
    const tripId = 1;
    axios.delete.mockRejectedValue(new Error('Some error occurred'));

    await expect(TripService.deleteTrip(tripId)).rejects.toThrow('Failed to delete Trip');
  });
});
