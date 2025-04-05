import { Routes, Route, useLocation } from "react-router-dom";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import ChatRoom from "./components/chat/ChatRoom";
import Home from "./pages/Home";
import MyProfile from "./pages/MyProfile";
import ProfilePage from "./pages/ProfilePage";
import PrivateRoute from "./routes/PrivateRoute";
import ChatListSidebar from "./components/chat/ChatListSidebar";
import { useState } from "react";
import AboutUs from "./pages/AboutUs";
import BlockList from "./pages/BlockList";
import CreateGroup from "./pages/CreateGroup";

function App() {
  const [selectedChat, setSelectedChat] = useState(null);
  const location = useLocation();

  return (
    <div className="flex h-screen">
      {/* Show Sidebar only on the /chat route */}
      {location.pathname.startsWith("/chat") && (
        <ChatListSidebar onSelectChat={setSelectedChat} />
      )}

      <div className="flex-1">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about-us" element={<AboutUs />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/profile" element={<MyProfile />} />
          <Route path="/profile/:userId" element={<ProfilePage />} />
          <Route path="/blocklist" element={<BlockList />} />

          <Route path="/create-group" element={<CreateGroup />} />

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
