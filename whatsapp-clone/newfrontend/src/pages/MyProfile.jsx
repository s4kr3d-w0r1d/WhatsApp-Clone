import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { FiArrowLeft, FiCamera, FiEdit } from "react-icons/fi";
import axios from "axios";

const BASE_URL = "http://localhost:8080";

const MyProfile = () => {
  const navigate = useNavigate();

  const userId = sessionStorage.getItem("loggedInUserId");
  const emailId = sessionStorage.getItem("loggedInUserEmail");
  const token = sessionStorage.getItem("token");
  const userName = sessionStorage.getItem("loggedInUserName");

  // console.log("User ID:", userId);
  // console.log("User Email:", emailId);
  // console.log("JWT Token:", token);

  const [name, setName] = useState("");
  const [bio, setBio] = useState("");
  const [status, setStatus] = useState("");
  const [profilePic, setProfilePic] = useState("");
  const [isEditingStatus, setIsEditingStatus] = useState(false);
  const [isEditingBio, setIsEditingBio] = useState(false);
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get(`${BASE_URL}/profile/${userId}`, {
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        });
        setName(userName || "John Doe");
        setBio(response.data.bio || "No bio yet...");
        setStatus(response.data.status || "Available");
        setProfilePic(response.data.profilePictureUrl || "/default-avatar.png"); // Updated fallback image
        setEmail(emailId || "Not Available");
      } catch (error) {
        console.error("Failed to fetch profile:", error);
      }
    };

    fetchProfile();
  }, []);

  const handleImageChange = async (e) => {
    const userId = sessionStorage.getItem("loggedInUserId");
    console.log("User ID:", userId);

    const file = e.target.files[0];
    console.log("Selected File:", file);
    if (file) {
      // setProfilePic(file);
      const formData = new FormData();
      formData.append("profilePicture", file);

      try {
        const response = await axios.put(
          `${BASE_URL}/profile/${userId}`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: `Bearer ${sessionStorage.getItem("token")}`,
            },
          }
        );

        console.log("Full Response:", response.data);
        setProfilePic(response.data.profilePictureUrl);
        setMessage("Profile picture updated!");
      } catch (error) {
        setMessage("Failed to update profile picture.");
      }
    }
  };

  const handleUpdateProfile = async () => {
    try {
      const payload = new FormData();
      payload.append("status", status || "");
      payload.append("bio", bio || "");
      if (profilePic instanceof File) {
        payload.append("profilePicture", profilePic);
      }

      const response = await axios.put(
        `${BASE_URL}/profile/${userId}`,
        payload,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        }
      );

      console.log("Response from backend:", response.data);
      setMessage("Profile updated successfully!");
      if (response.data.profilePictureUrl) {
        setProfilePic(response.data.profilePictureUrl);
      }
    } catch (error) {
      console.error("Error updating profile:", error);
      setMessage("Failed to update profile.");
    }
  };

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      handleUpdateProfile();
    }
  };

  return (
    <div className="h-screen w-full bg-gray-900 text-white flex flex-col items-center p-6">
      <button
        onClick={() => navigate(-1)}
        className="absolute top-4 left-4 text-2xl"
      >
        <FiArrowLeft />
      </button>

      <div className="relative w-24 h-24">
        <img
          src={
            profilePic
              ? profilePic.startsWith("/")
                ? `http://localhost:8080${profilePic}`
                : profilePic
              : "default-avatar.png"
          }
          alt="Profile"
          onError={(e) => (e.target.src = "default-avatar.png")}
          className="w-full h-full rounded-full border-2 border-gray-600"
        />

        <label className="absolute bottom-0 right-0 bg-gray-800 p-1 rounded-full cursor-pointer">
          <FiCamera />
          <input
            type="file"
            accept="image/*"
            className="hidden"
            onChange={handleImageChange}
          />
        </label>
      </div>
      <p className="text-gray-600 mt-4">{name}</p>
      <div className="flex items-center mt-4">
        {isEditingStatus ? (
          <input
            type="text"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            onBlur={() => {
              setIsEditingStatus(false);
              handleUpdateProfile();
            }}
            className="bg-transparent text-center text-xl font-semibold outline-none border-b border-white"
            autoFocus
          />
        ) : (
          <h2 className="text-xl font-semibold">{status}</h2>
        )}
        <FiEdit
          className="ml-2 cursor-pointer text-gray-400 hover:text-white"
          onClick={() => setIsEditingStatus(true)}
        />
      </div>

      <div className="flex items-center mt-2">
        {isEditingBio ? (
          <textarea
            value={bio}
            onChange={(e) => setBio(e.target.value)}
            onBlur={() => {
              setIsEditingBio(false);
              handleUpdateProfile();
            }}
            onKeyDown={handleKeyDown}
            className="bg-transparent text-center text-sm outline-none border-b border-white w-64 resize-none"
            autoFocus
          />
        ) : (
          <p className="text-sm">{bio}</p>
        )}
        <FiEdit
          className="ml-2 cursor-pointer text-gray-400 hover:text-white"
          onClick={() => setIsEditingBio(true)}
        />
      </div>

      <p className="text-gray-400 mt-4">{email}</p>
      {message && <p className="text-green-500 mt-2">{message}</p>}
    </div>
  );
};

export default MyProfile;
