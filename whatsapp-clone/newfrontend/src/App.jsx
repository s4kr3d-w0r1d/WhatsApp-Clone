import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import ChatRoom from "./components/chat/ChatRoom";
import Home from "./pages/Home";
import { Routes, Route } from "react-router-dom";
import PrivateRoute from "./routes/PrivateRoute";
import ChatListSidebar from "./components/chat/ChatListSidebar";
import { useState } from "react";

function App() {
  const [selectedChat, setSelectedChat] = useState(null);
  return (
    <div className="flex h-screen">
      <Routes>
        <Route
          path="/chat"
          element={<ChatListSidebar onSelectChat={setSelectedChat} />}
        />
      </Routes>
      <div className="flex-1">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route element={<PrivateRoute />}>
            <Route
              path="/chat"
              element={<ChatRoom selectedChat={selectedChat} />}
            />
          </Route>
        </Routes>
      </div>
    </div>
  );
}
export default App;
