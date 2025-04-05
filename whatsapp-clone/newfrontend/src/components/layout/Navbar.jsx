import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiMenu, FiX, FiUser, FiSettings, FiLogOut } from "react-icons/fi";

const Navbar = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();

  const logout = async () => {
    const confirmLogout = window.confirm("Are you sure you want to logout?");
    if (!confirmLogout) {
      console.log("Logout cancelled by user.");
      return;
    }

    try {
      const token = sessionStorage.getItem("token"); // Get JWT from session storage
      console.log(token);
      if (!token) {
        console.log("No token found, user already logged out.");
        navigate("/login");
        return;
      }

      console.log("Logging out...");

      // Call backend logout API
      const response = await fetch("http://localhost:8080/auth/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      const data = await response.text();
      console.log("Logout response:", data);

      // Clear session storage & redirect to login
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("user");

      console.log("User logged out successfully. Redirecting to login...");
      navigate("/login");
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  return (
    <div className="navbar bg-neutral text-neutral-content w-full fixed top-0 left-0 flex justify-between h-10 min-h-0 px-4">
      <button className="btn btn-ghost text-xl pl-8">Whatsapp</button>
      
      {/* Top Navbar Logout */}
      <button className="btn btn-sm " onClick={logout}>
        Logout
      </button>

      {/* Sidebar Toggle Button */}
      <button
        className="text-white text-2xl absolute left"
        onClick={() => setSidebarOpen(true)}
      >
        <FiMenu />
      </button>

      {/* Sidebar */}
      <div
        className={`fixed top-0 left-0 h-full w-64 bg-gray-900 text-white p-6 transition-transform duration-300 z-50 ${
          isSidebarOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <button
          className="text-white text-2xl absolute top-4 right-4"
          onClick={() => setSidebarOpen(false)}
        >
          <FiX />
        </button>

        <h2 className="text-lg font-semibold mb-6">Menu</h2>

        <ul className="space-y-4">
          <li
            className="flex items-center space-x-2 cursor-pointer hover:text-gray-400"
            onClick={() => {
              console.log("Navigating to profile...");
              navigate("/profile");
              setSidebarOpen(false);
            }}
          >
            <FiUser />
            <span>My Profile</span>
          </li>

          <li className="flex items-center space-x-2 cursor-pointer hover:text-gray-400">
            <FiSettings />
            <span>Settings</span>
          </li>

          {/* Sidebar Logout Button */}
          <li
            className="flex items-center space-x-2 cursor-pointer hover:text-gray-400 text-red-500"
            onClick={logout}
          >
            <FiLogOut />
            <span>Logout</span>
          </li>
        </ul>
      </div>

      {/* Overlay when sidebar is open */}
      {isSidebarOpen && (
        <div
          className="fixed inset-0 bg-black opacity-70 z-40"
          onClick={() => setSidebarOpen(false)}
        ></div>
      )}
    </div>
  );
};

export default Navbar;
