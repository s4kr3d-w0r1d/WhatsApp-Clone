import "./App.css";
import Login from "./Login";
import Register from "./Register";
import ChatRoom from "./ChatRoom";


import { Routes, Route } from "react-router-dom";
import bgImage from "./assets/bg2.jpg";
import PrivateRoute from "./routes/PrivateRoute";

function App() {


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


        <Route element={<PrivateRoute />}>
            <Route path="/chat" element={<ChatRoom />}></Route>

          </Route>


      </Routes>
    </div>
   
     



  );
}

export default App;
