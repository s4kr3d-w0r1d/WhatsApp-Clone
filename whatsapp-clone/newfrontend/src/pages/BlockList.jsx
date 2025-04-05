import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import axios from "axios";

const BASE_URL = "http://localhost:8080";

const BlockList = () => {
  const [blockedUsers, setBlockedUsers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBlockedUsers = async () => {
      const loggedInUserId = sessionStorage.getItem("loggedInUserId");
      if (!loggedInUserId) return;

      try {
        const res = await axios.get(`${BASE_URL}/api/blocks/${loggedInUserId}`);
        console.log(`Blocked list for ${loggedInUserId}:`, res.data);

        const blockedList = res.data.map((entry) => entry.blocked); 

        const usersWithProfiles = await Promise.all(
          blockedList.map(async (user) => {
            try {
              const profileRes = await axios.get(`${BASE_URL}/profile/${user.id}`);
              return { ...user, profilePictureUrl: profileRes.data.profilePictureUrl };
            } catch (err) {
              console.error(`Error fetching profile for user ${user.id}`, err);
              return { ...user, profilePictureUrl: "/default-avatar.jpg" };
            }
          })
        );

        setBlockedUsers(usersWithProfiles);
      } catch (err) {
        console.error("Failed to fetch blocked users:", err);
      }
    };

    fetchBlockedUsers();
  }, []);

  return (
    <div className="p-6 text-white bg-gray-900 min-h-screen">
         <button
      onClick={() => navigate(-1)}
      className="text-white hover:text-gray-400 text-2xl mr-3"
    >
      <FiArrowLeft />
    </button>
      <h2 className="text-xl font-bold mb-4">Blocked Users</h2>

      {blockedUsers.length === 0 ? (
        <p className="text-gray-400">You haven’t blocked anyone.</p>
      ) : (
        <div className="space-y-4">
          {blockedUsers.map((user) => (
            <div
              key={user.id}
              className="flex items-center bg-gray-800 p-3 rounded-lg hover:bg-gray-700 cursor-pointer"
              onClick={() => navigate(`/profile/${user.id}`)}
            >
              <img
                src={user.profilePictureUrl || "/default-avatar.jpg"}
                alt={user.name}
                className="w-12 h-12 rounded-full object-cover"
                onError={(e) => (e.target.src = "/default-avatar.jpg")}
              />
              <span className="ml-4 text-lg">{user.name}</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default BlockList;
