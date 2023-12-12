import Form from "./components/Form";
import Navbar from "./components/Navbar";
import WeatherOverview from "./components/WeatherOverview";
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
              {/* <Route path="/" element={<Form/>}></Route> */}
              <Route path="/" element={<WeatherOverview location={"Cracow"} img_path={"day/113.png"} temp_c={5} condition={"Sunny"}/>}></Route>
              <Route path="/weather" element={<WeatherView/>}></Route>
            </Routes>
          </BrowserRouter>
        </div>
    </>
  );
}

export default App;
