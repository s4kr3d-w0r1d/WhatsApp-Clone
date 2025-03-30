import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { FiArrowLeft } from "react-icons/fi"; 

const ProfilePage = () => {
  const { userId } = useParams(); // Get userId from the URL
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/profile/${userId}`);
        setUser(response.data);
        console.log("Profile Data:", response.data); 
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    fetchProfile();
  }, [userId]);

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
      <h2 className="text-xl font-semibold">{user.status || "No status available."}</h2>
      <p className="text-sm">{user.bio || "No bio available."}</p>
      <p className="text-gray-400 mt-4">{user.user.email || "No email available."}</p>
    </div>
  );
};

export default ProfilePage;
