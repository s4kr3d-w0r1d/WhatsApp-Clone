import { useState } from "react";
import axios from "axios";
import { FiArrowLeft } from "react-icons/fi";
import { useNavigate } from "react-router-dom";


const CreateGroup = () => {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [groupType, setGroupType] = useState("PUBLIC"); // or "PRIVATE"
  const [groupPicture, setGroupPicture] = useState(null);

  const navigate = useNavigate();

  const handleCreate = async () => {
    const loggedInUserId = sessionStorage.getItem("loggedInUserId");
    if (!name || !loggedInUserId) return;

    const formData = new FormData();
    formData.append("ownerId", loggedInUserId);
    formData.append("name", name.trim());
    formData.append("description", description.trim());
    formData.append("groupType", groupType); // adjust if your backend expects enum values
    if (groupPicture) {
      formData.append("groupPicture", groupPicture);
    }

    try {
      const res = await axios.post("http://localhost:8080/api/groups/create", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Group created successfully!");
    } catch (err) {
      console.error("Group creation failed:", err);
      alert("Error creating group.");
    }
  };

  return (
    <div className="p-6 text-white bg-gray-900 min-h-screen pt-10 pl-10">
        <button
        onClick={() => navigate(-1)}
        className="absolute top-4 left-4 text-2xl text-white hover:text-gray-400"
      >
        <FiArrowLeft />
      </button>
      <h2 className="text-2xl font-bold mb-4">Create Group</h2>
      <input
  className="w-full mb-4 p-2 rounded bg-gray-800 text-white focus:bg-white focus:text-black transition-colors duration-200"
  type="text"
  placeholder="Group Name"
  value={name}
  onChange={(e) => setName(e.target.value)}
/>

<textarea
  className="w-full mb-4 p-2 rounded bg-gray-800 text-white focus:bg-white focus:text-black transition-colors duration-200"
  placeholder="Description"
  value={description}
  onChange={(e) => setDescription(e.target.value)}
/>

<select
  className="w-full mb-4 p-2 rounded bg-gray-800 text-white focus:bg-white focus:text-black transition-colors duration-200"
  value={groupType}
  onChange={(e) => setGroupType(e.target.value)}
>
  <option value="PUBLIC">Public</option>
  <option value="PRIVATE">Private</option>
</select>

      <input
        className="w-full mb-4 p-2 rounded bg-gray-800"
        type="file"
        accept="image/*"
        onChange={(e) => setGroupPicture(e.target.files[0])}
      />
      <button className="btn btn-primary" onClick={handleCreate}>
        Create Group
      </button>
    </div>
  );
};

export default CreateGroup;
