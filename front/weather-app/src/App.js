import Form from "./components/Form";
import Navbar from "./components/Navbar";
import WeatherView from "./components/WeatherView";
import "./styles/App.css"
import {BrowserRouter, Routes, Route} from "react-router-dom"

function App() {
  return (
    <>
      <Navbar />
        <div className="main-container">
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<Form/>}></Route>
              <Route path="/weather" element={<WeatherView/>}></Route>
            </Routes>
          </BrowserRouter>
        </div>
    </>
  );
}

export default App;
