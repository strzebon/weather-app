import React from 'react';
import { render, screen } from '@testing-library/react';
import WeatherOverview from '../components/WeatherOverview';

describe('WeatherOverview component', () => {
  const sampleData = {
    tripName: 'Trip 1',
    classNames: 'sample-class',
    img: ['img1.jpg', 'img2.jpg'],
    tempC: 25,
    condition: 'Sunny',
    locations: 'Location A',
  };

  it('renders trip name when provided', () => {
    render(<WeatherOverview {...sampleData} />);
    const tripNameElement = screen.getByText(/Trip Name:/i);
    expect(tripNameElement).toBeInTheDocument();
    expect(tripNameElement).toHaveTextContent('Trip Name:');
    expect(screen.getByText('Trip 1')).toBeInTheDocument();
  });

  it('renders weather information correctly', () => {
    render(<WeatherOverview {...sampleData} />);
    expect(screen.getByText('Location A')).toBeInTheDocument();
    expect(screen.getByText('25°C')).toBeInTheDocument();
    expect(screen.getByText('Sunny')).toBeInTheDocument();
    expect(screen.getAllByAltText('conditions')).toHaveLength(2);
  });
});
