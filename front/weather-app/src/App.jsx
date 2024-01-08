import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Form from './components/Form';
import Navbar from './components/Navbar';
import Trips from './components/Trips';
import WeatherView from './components/WeatherView';
import './styles/App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/"
          element={(
            <>
              <Navbar />
              <Form />
            </>
)}
        />
        <Route
          path="/weather"
          element={(
            <>
              <Navbar />
              <WeatherView />
            </>
)}
        />
        <Route
          path="trips"
          element={(
            <>
              <Navbar />
              <Trips />
            </>
)}
        />
        <Route
          path="trips/:id"
          element={(
            <>
              <Navbar />
              <WeatherView />
            </>
)}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
