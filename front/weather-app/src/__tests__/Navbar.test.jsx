import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import Navbar from '../components/Navbar';

let mockNavigate;

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

describe('Navbar', () => {
  beforeEach(() => {
    mockNavigate = jest.fn();
  });

  it('should render the Navbar component', () => {
    render(<Navbar />);
    const logoButton = screen.getByRole('button', { name: /WeatherApp/ });
    const tripsButton = screen.getByRole('button', { name: /My Trips/ });

    expect(logoButton).toBeInTheDocument();
    expect(tripsButton).toBeInTheDocument();
  });

  it('should contain the correct text and icons', () => {
    render(<Navbar />);
    const logoButton = screen.getByRole('button', { name: /WeatherApp/ });

    expect(logoButton).toHaveTextContent(/WeatherApp/);

    const img = screen.getByRole('img', { name: /Sun behind the clouds in a circle/ });
    expect(img).toBeInTheDocument();
  });

  it('should navigate to home when clicking on the logo', () => {
    render(<Navbar />);
    const logoButton = screen.getByRole('button', { name: /WeatherApp/ });

    fireEvent.click(logoButton);

    expect(mockNavigate).toHaveBeenCalledWith('/');
  });

  it('should navigate to trips when clicking on My Trips', () => {
    render(<Navbar />);
    const tripsButton = screen.getByRole('button', { name: /My Trips/ });

    fireEvent.click(tripsButton);

    expect(mockNavigate).toHaveBeenCalledWith('/trips');
  });
});
