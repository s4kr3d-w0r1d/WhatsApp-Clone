import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { FiArrowLeft } from "react-icons/fi";

const loggedId = sessionStorage.getItem("loggedInUserId");

const ProfilePage = () => {
  const { userId } = useParams(); // Get userId from the URL
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [isBlocked, setIsBlocked] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/profile/${userId}`
        );
        setUser(response.data);
        console.log("Profile Data:", response.data);
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    const checkBlocked = async () => {
      try {
        console.log(`Checking if user ID ${userId} is blocked by ${loggedId}`);
        const res = await axios.get(
          `http://localhost:8080/api/blocks/${loggedId}`
        );
        const blockedList = res.data.map((entry) => entry.blocked.id);
        const isBlockedUser = blockedList.includes(parseInt(userId));
        console.log(`Blocked list for ${loggedId}:`, blockedList);
        console.log(`Is user ${userId} blocked?`, isBlockedUser);

        setIsBlocked(isBlockedUser);
      } catch (err) {
        console.error("Error checking blocked users:", err);
      }
      if (!loggedId) {
        console.error("Logged-in user ID not found in sessionStorage");
        return;
      }
    };

    fetchProfile();
    checkBlocked();
  }, [userId]);

  const handleBlock = async () => {
    try {
      console.log(`Attempting to block user ID ${userId} by ${loggedId}`);
      await axios.post(
        `http://localhost:8080/api/blocks/${loggedId}/${userId}`
      );
      console.log(`Successfully blocked user ID ${userId}`);
      setIsBlocked(true);
    } catch (err) {
      console.error("Error blocking user:", err);
    }
  };

  const handleUnblock = async () => {
    try {
      console.log(`Attempting to unblock user ID ${userId} by ${loggedId}`);
      await axios.delete(
        `http://localhost:8080/api/blocks/${loggedId}/${userId}`
      );
      console.log(`Successfully unblocked user ID ${userId}`);
      setIsBlocked(false);
    } catch (err) {
      console.error("Error unblocking user:", err);
    }
  };

  if (!user) return <div>Loading...</div>;

  return (
    <div className="h-screen w-full bg-gray-900 text-white flex flex-col items-center p-6">
      {/* Back Arrow */}
      <button
        onClick={() => navigate(-1)}
        className="absolute top-4 left-4 text-2xl text-white hover:text-gray-400"
      >
        <FiArrowLeft />
      </button>
      <h2 className="text-2xl font-semibold">{user.name}</h2>
      <img
        src={user.profilePictureUrl || "/default-avatar.jpg"}
        alt={user.name}
        className="w-32 h-32 rounded-full mt-4"
      />

      <p className="text-4xl font-bold text-gray-100 mt-4">{user.user.name}</p>
      <h2 className="text-xl font-semibold">
        {user.status || "No status available."}
      </h2>
      <p className="text-sm">{user.bio || "No bio available."}</p>
      <p className="text-gray-400 mt-4">
        {user.user.email || "No email available."}
      </p>

      <button
        onClick={isBlocked ? handleUnblock : handleBlock}
        className={`mt-6 px-4 py-2 rounded-lg text-white ${
          isBlocked
            ? "bg-green-600 hover:bg-green-700"
            : "bg-red-600 hover:bg-red-700"
        }`}
      >
        {isBlocked ? "Unblock User" : "Block User"}
      </button>
    </div>
  );
};

export default ProfilePage;
