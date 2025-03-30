import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiMenu, FiX, FiUser, FiSettings, FiLogOut } from "react-icons/fi";
import { FaDotCircle } from "react-icons/fa"; 

const Navbar = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate(); 
  return (
      <div className="navbar bg-neutral text-neutral-content w-full fixed top-0 left-0 flex justify-between h-10 min-h-0 px-4">
        <button className="btn btn-ghost text-xl pl-8">Whatsapp</button>
        <button className="btn btn-sm">Logout</button>
      
        <button
          className="text-white text-2xl absolute left"
          onClick={() => setSidebarOpen(true)}
        >
          <FiMenu />
        </button>
      
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
    <li className="flex items-center space-x-2 cursor-pointer hover:text-gray-400"  onClick={() => { console.log("Navigating to profile..."); navigate("/profile"); setSidebarOpen(false); }}>
      <FiUser />
      <span>My Profile</span>
    </li>
    <li className="flex items-center space-x-2 cursor-pointer hover:text-gray-400">
      <FaDotCircle />
      <span>Status</span>
    </li>
    <li className="flex items-center space-x-2 cursor-pointer hover:text-gray-400">
      <FiSettings />
      <span>Settings</span>
    </li>
    <li className="flex items-center space-x-2 cursor-pointer hover:text-gray-400">
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

