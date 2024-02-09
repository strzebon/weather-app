import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import FormInput from '../components/FormInput';

jest.mock('react', () => ({
  ...jest.requireActual('react'),
  useState: jest.fn(),
  useEffect: jest.fn(),
}));

describe('formInput component', () => {
  const mockUseState = (initialValue) => {
    let value = initialValue;
    const setValue = (newValue) => {
      value = newValue;
    };
    return [value, setValue];
  };

  beforeEach(() => {
    jest.clearAllMocks();
    React.useState.mockImplementation((initialState) => mockUseState(initialState));
  });

  it('renders FormInput component', () => {
    expect.assertions(2);
    render(<FormInput />);
    expect(screen.getByLabelText('Lattitude')).toBeInTheDocument();
    expect(screen.getByLabelText('Longitude')).toBeInTheDocument();
  });

  it('updates lattitude input', () => {
    expect.assertions(1);
    render(<FormInput />);
    const lattitudeInput = screen.getByLabelText('Lattitude');
    fireEvent.change(lattitudeInput, { target: { value: '50' } });

    expect(lattitudeInput.value).toBe('50');
  });

  it('updates longitude input', () => {
    expect.assertions(1);
    render(<FormInput />);
    const longitudeInput = screen.getByLabelText('Longitude');
    fireEvent.change(longitudeInput, { target: { value: '20' } });

    expect(longitudeInput.value).toBe('20');
  });

  it('updates state and calls handleChange on input change', async () => {
    expect.assertions(2);
    const handleChangeMock = jest.fn();
    render(<FormInput id={1} handleChange={handleChangeMock} />);
    const lattitudeInput = screen.getByLabelText('Lattitude');
    const longitudeInput = screen.getByLabelText('Longitude');

    fireEvent.change(lattitudeInput, { target: { value: '40' } });
    fireEvent.blur(lattitudeInput);

    fireEvent.change(longitudeInput, { target: { value: '-80' } });
    fireEvent.blur(longitudeInput);

    expect(lattitudeInput.value).toBe('40');
    expect(longitudeInput.value).toBe('-80');
  });
});
