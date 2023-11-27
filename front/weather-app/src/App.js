import Form from "./components/Form";
import Navbar from "./components/Navbar";
import "./styles/App.css"

function App() {
  return (
    <>
      <Navbar />
      <div className="main-container">
        <Form/>
      </div>
    </>
  );
}

export default App;
