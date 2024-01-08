import Form from "./components/Form";
import Navbar from "./components/Navbar";
import Trips from "./components/Trips";
import WeatherView from "./components/WeatherView";
import "./styles/App.css"
import {BrowserRouter, Routes, Route} from "react-router-dom"

function App() {
  return (
    <>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<><Navbar /><Form/></>}></Route>
            <Route path="/weather" element={<><Navbar /><WeatherView/></>}></Route>
            <Route path="trips" element={<><Navbar /><Trips /></>}></Route>
            <Route path="trips/:id" element={<><Navbar /><WeatherView/></>}></Route>
          </Routes>
        </BrowserRouter>
    </>
  );
}

export default App;
