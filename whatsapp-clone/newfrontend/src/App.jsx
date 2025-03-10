import "./App.css";
import Login from "./Login";
import Register from "./Register";
import ChatRoom from "./ChatRoom";
import Home from "./Home";

import { Routes, Route } from "react-router-dom";
import bgImage from "./assets/bg3.jpg";
import PrivateRoute from "./routes/PrivateRoute";

function App() {
  return (
    <div
      className="text-white  w-screen h-screen flex  bg-cover"
      style={{
        backgroundImage: `url(${bgImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route element={<PrivateRoute />}>
          <Route path="/chat" element={<ChatRoom />}></Route>
        </Route>
      </Routes>
    </div>
  );
}

export default App;
