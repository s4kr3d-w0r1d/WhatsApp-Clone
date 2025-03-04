import "./App.css";
import Login from "./Login";
import Register from "./Register";
import ChatRoom from "./ChatRoom";
// import Navbar from "./Navbar";

import { Routes, Route } from "react-router-dom";
import bgImage from "./assets/bg2.jpg";
import PrivateRoute from "./routes/PrivateRoute";

function App() {
  // const [count, setCount] = useState(0)

  return (
    <div
      className="text-white  w-screen h-screen flex justify-center bg-cover"
      style={{
        backgroundImage: `url(${bgImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/chat" element= {<PrivateRoute> <ChatRoom/> </PrivateRoute>} />
        {/* <Route path="/navbar" element={<Navbar />} /> */}
      </Routes>
    </div>
   
     



  );
}

export default App;
