import DataConvertService from '../services/DataConvertService';

describe('dataConvertService', () => {
  describe('getTemperature', () => {
    it('should convert temperature to lowercase', () => {
      expect.assertions(1);
      const result = DataConvertService.getTemperature('TEMPERATURE');
      expect(result).toBe('temperature');
    });

    it('should handle empty input', () => {
      expect.assertions(1);
      const result = DataConvertService.getTemperature('');
      expect(result).toBe('');
    });
  });

  describe('getPrecipitation', () => {
    it('should convert RAIN to rainLogo', () => {
      expect.assertions(1);
      const result = DataConvertService.getPrecipitation(['RAIN']);
      expect(result).toStrictEqual(['rain.png']);
    });

    it('should convert SNOW to snowLogo', () => {
      expect.assertions(1);
      const result = DataConvertService.getPrecipitation(['SNOW']);
      expect(result).toStrictEqual(['snow.png']);
    });

    it('should handle empty precipitation', () => {
      expect.assertions(1);
      const result = DataConvertService.getPrecipitation([]);
      expect(result).toStrictEqual(['sunny.png']);
    });

    it('should ignore other precipitation types', () => {
      expect.assertions(1);
      const result = DataConvertService.getPrecipitation(['BAD']);
      expect(result).toStrictEqual(['sunny.png']);
    });
  });

  describe('getWind', () => {
    it('should add windLogo when windy', () => {
      expect.assertions(1);
      const imgArray = [];
      DataConvertService.getWind(true, imgArray);
      expect(imgArray).toStrictEqual(['wind.png']);
    });

    it('should not add windLogo when not windy', () => {
      expect.assertions(1);
      const imgArray = [];
      DataConvertService.getWind(false, imgArray);
      expect(imgArray).toStrictEqual([]);
    });
  });

  describe('getMud', () => {
    it('should add mudLogo when muddy', () => {
      expect.assertions(1);
      const imgArray = [];
      DataConvertService.getMud(true, imgArray);
      expect(imgArray).toStrictEqual(['mud.png']);
    });

    it('should not add mudLogo when not muddy', () => {
      expect.assertions(1);
      const imgArray = [];
      DataConvertService.getMud(false, imgArray);
      expect(imgArray).toStrictEqual([]);
    });
  });

  describe('getLocations', () => {
    it('should join locations with comma', () => {
      expect.assertions(1);
      const locations = ['Location A', 'Location B', 'Location C'];
      const result = DataConvertService.getLocations(locations);
      expect(result).toBe('Location A, Location B, Location C');
    });

    it('should handle single location', () => {
      expect.assertions(1);
      const locations = ['Location A'];
      const result = DataConvertService.getLocations(locations);
      expect(result).toBe('Location A');
    });

    it('should handle empty locations array', () => {
      expect.assertions(1);
      const locations = [];
      const result = DataConvertService.getLocations(locations);
      expect(result).toBe('');
    });
  });

  describe('getWeatherInfo', () => {
    it('should return weather info object', () => {
      expect.assertions(5);
      const mockData = {
        precipitation: ['RAIN'],
        isWindy: true,
        isMuddy: false,
        temperatureLevel: 'TEMPERATURE',
        locations: ['Location A', 'Location B'],
        sensedTemp: 20.5,
      };

      const result = DataConvertService.getWeatherInfo(mockData);

      expect(result.img).toStrictEqual(['rain.png', 'wind.png']);
      expect(result.classNames).toBe('temperature');
      expect(result.locations).toBe('Location A, Location B');
      expect(result.tempC).toBe(21);
      expect(result.condition).toBe('TEMPERATURE');
    });
  });
});
