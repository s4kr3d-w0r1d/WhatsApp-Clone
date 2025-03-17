
import { useState } from "react";
 import { useNavigate } from "react-router-dom"; 
 import { FiArrowLeft, FiCamera, FiEdit } from "react-icons/fi";

 const MyProfile = () => {
   const navigate = useNavigate(); 
   const [name, setName] = useState("John");
   const [bio, setBio] = useState("living");
   const [profilePic, setProfilePic] = useState("https://via.placeholder.com/100");
   const [isEditingName, setIsEditingName] = useState(false);
   const [isEditingBio, setIsEditingBio] = useState(false);
 
   const handleImageChange = (e) => {
     const file = e.target.files[0];
     if (file) {
       const imageUrl = URL.createObjectURL(file);
       setProfilePic(imageUrl);
     }
   };
 
   const handleBlur = (setEditing) => {
     setTimeout(() => setEditing(false), 200); 
   };
 
   return (
     <div className="h-screen w-full bg-gray-900 text-white flex flex-col items-center p-6">
      
       <button onClick={() => navigate(-1)} className="absolute top-4 left-4 text-2xl">
         <FiArrowLeft />
       </button>
 
       {/* Profile Picture */}
       <div className="relative w-24 h-24">
         <img src={profilePic} alt="Profile" className="w-full h-full rounded-full border-2 border-gray-600" />
         <label className="absolute bottom-0 right-0 bg-gray-800 p-1 rounded-full cursor-pointer">
           <FiCamera />
           <input type="file" accept="image/*" className="hidden" onChange={handleImageChange} />
         </label>
       </div>
 
       {/* Name */}
       <div className="flex items-center mt-4">
         {isEditingName ? (
           <input
             type="text"
             value={name}
             onChange={(e) => setName(e.target.value)}
             onBlur={() => handleBlur(setIsEditingName)}
             className="bg-transparent text-center text-xl font-semibold outline-none border-b border-white"
             autoFocus
           />
         ) : (
           <h2 className="text-xl font-semibold">{name}</h2>
         )}
         <FiEdit
           className="ml-2 cursor-pointer text-gray-400 hover:text-white"
           onClick={() => setIsEditingName(true)}
         />
       </div>
 
       {/* Bio */}
       <div className="flex items-center mt-2">
         {isEditingBio ? (
           <textarea
             value={bio}
             onChange={(e) => setBio(e.target.value)}
             onBlur={() => handleBlur(setIsEditingBio)}
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
 
     
       <p className="text-gray-400 mt-4">v@gmail.com</p>
     </div>
   );
 };
 
 export default MyProfile;



// import { useState , useEffect} from "react";
//  import { useNavigate } from "react-router-dom"; 
//  import { FiArrowLeft, FiCamera, FiEdit } from "react-icons/fi";
//  import axios from "axios";
// const Profile = () => {
//   const navigate = useNavigate();
//   const [name, setName] = useState(""); 
//   const [bio, setBio] = useState("");
//   const [profilePic, setProfilePic] = useState("https://via.placeholder.com/100");
//   const [isEditingName, setIsEditingName] = useState(false);
//   const [isEditingBio, setIsEditingBio] = useState(false);

//   // Get token and userId from localStorage (after login)
//   const token = localStorage.getItem("token");
//   const userId = localStorage.getItem("loggedInUserId"); // Store this when login is successful

//   // Fetch user profile from backend
//   useEffect(() => {
//     if (!userId || !token) return;

//     axios
//       .get(`http://localhost:8080/profile/${userId}`, {
//         headers: { Authorization: `Bearer ${token}` }, // Send token in headers
//       })
//       .then((response) => {
//         console.log("User Profile:", response.data);
//         setName(response.data.user.name); 
//         setBio(response.data.bio || "Hey there! I am using WhatsApp.");
//         setProfilePic(response.data.profilePictureUrl || "https://via.placeholder.com/100");
//       })
//       .catch((error) => {
//         console.error("Error fetching profile:", error);
//       });
//   }, [userId, token]);

//   return (
//     <div className="h-screen w-full bg-gray-900 text-white flex flex-col items-center p-6">
//       <button onClick={() => navigate(-1)} className="absolute top-4 left-4 text-2xl">
//         <FiArrowLeft />
//       </button>

//       {/* Profile Picture */}
//       <div className="relative w-24 h-24">
//         <img src={profilePic} alt="Profile" className="w-full h-full rounded-full border-2 border-gray-600" />
//       </div>

//       {/* Name */}
//       <div className="flex items-center mt-4">
//         <h2 className="text-xl font-semibold">{name}</h2>
//       </div>

//       {/* Bio */}
//       <div className="flex items-center mt-2">
//         <p className="text-sm">{bio}</p>
//       </div>
//     </div>
//   );
// };

// export default Profile;
