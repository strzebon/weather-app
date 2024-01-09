import DataConvertService from '../services/DataConvertService';

describe('DataConvertService', () => {
  describe('getTemperature', () => {
    it('should convert temperature to lowercase', () => {
      const result = DataConvertService.getTemperature('TEMPERATURE');
      expect(result).toBe('temperature');
    });

    it('should handle empty input', () => {
      const result = DataConvertService.getTemperature('');
      expect(result).toBe('');
    });
  });

  describe('getPrecipitation', () => {
    it('should convert RAIN to rainLogo', () => {
      const result = DataConvertService.getPrecipitation(['RAIN']);
      expect(result).toEqual(['rain.png']);
    });

    it('should convert SNOW to snowLogo', () => {
      const result = DataConvertService.getPrecipitation(['SNOW']);
      expect(result).toEqual(['snow.png']);
    });

    it('should handle empty precipitation', () => {
      const result = DataConvertService.getPrecipitation([]);
      expect(result).toEqual(['sunny.png']);
    });

    it('should ignore other precipitation types', () => {
      const result = DataConvertService.getPrecipitation(['BAD']);
      expect(result).toEqual(['sunny.png']);
    });
  });

  describe('getWind', () => {
    it('should add windLogo when windy', () => {
      const imgArray = [];
      DataConvertService.getWind(true, imgArray);
      expect(imgArray).toEqual(['wind.png']);
    });

    it('should not add windLogo when not windy', () => {
      const imgArray = [];
      DataConvertService.getWind(false, imgArray);
      expect(imgArray).toEqual([]);
    });
  });

  describe('getMud', () => {
    it('should add mudLogo when muddy', () => {
      const imgArray = [];
      DataConvertService.getMud(true, imgArray);
      expect(imgArray).toEqual(['mud.png']);
    });

    it('should not add mudLogo when not muddy', () => {
      const imgArray = [];
      DataConvertService.getMud(false, imgArray);
      expect(imgArray).toEqual([]);
    });
  });

  describe('getLocations', () => {
    it('should join locations with comma', () => {
      const locations = ['Location A', 'Location B', 'Location C'];
      const result = DataConvertService.getLocations(locations);
      expect(result).toBe('Location A, Location B, Location C');
    });

    it('should handle single location', () => {
      const locations = ['Location A'];
      const result = DataConvertService.getLocations(locations);
      expect(result).toBe('Location A');
    });

    it('should handle empty locations array', () => {
      const locations = [];
      const result = DataConvertService.getLocations(locations);
      expect(result).toBe('');
    });
  });

  describe('getWeatherInfo', () => {
    it('should return weather info object', () => {
      const mockData = {
        precipitation: ['RAIN'],
        isWindy: true,
        isMuddy: false,
        temperatureLevel: 'TEMPERATURE',
        locations: ['Location A', 'Location B'],
        sensedTemp: 20.5,
      };

      const result = DataConvertService.getWeatherInfo(mockData);

      expect(result.img).toEqual(['rain.png', 'wind.png']);
      expect(result.classNames).toBe('temperature');
      expect(result.locations).toBe('Location A, Location B');
      expect(result.tempC).toBe(21);
      expect(result.condition).toBe('TEMPERATURE');
    });
  });
});
