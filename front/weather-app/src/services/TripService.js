import axios from 'axios';

const enpointURL = 'http://127.0.0.1:8080';

const TripService = {
  getTrips: async () => {
    try {
        const response = await axios.get(`${enpointURL}/trips`);
        return response.data;
      } catch (error) {
        throw new Error("Failed to fetch trip of id:");
      }
  },

  getTripById: async (id) => {
    try {
      const response = await axios.get(`${enpointURL}/trips/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch trip of id: ${id}`);
    }
  },

  addNewTrip: async (trip) => {
    try {
        const response = await axios.post(`${enpointURL}/trips`, trip);
        return response.data;
      } catch (error) {
        throw new Error('Failed to post new Trip');
      }
  }
};

export default TripService;
