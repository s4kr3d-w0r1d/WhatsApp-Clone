import { useState, useEffect } from "react";
import { FaSearch, FaEllipsisV } from "react-icons/fa";
import { BsChatDotsFill } from "react-icons/bs";
import { useNavigate } from "react-router-dom";

const ChatListSidebar = ({ onSelectChat }) => {
  const [search, setSearch] = useState("");
  const [chats, setChats] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    if (search.trim() === "") {
      setChats([]);
      return;
    }

    const fetchUsers = async () => {
      try {
        const response = await fetch(
          `http://localhost:8080/profile/search?name=${search}`
        );
        if (!response.ok) throw new Error("Failed to fetch users");
        const data = await response.json();
        setChats(data);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };

    const delayDebounce = setTimeout(fetchUsers, 300);

    return () => clearTimeout(delayDebounce);
  }, [search]);

  return (
    <div className="w-1/3 h-screen bg-gray-900 text-white flex flex-col pt-7">
      <div className="p-4 flex justify-between items-center border-b border-gray-700">
        <h2 className="text-lg font-semibold">Chats</h2>
        <div className="flex gap-3">
          <BsChatDotsFill className="text-xl cursor-pointer" />
          <FaEllipsisV className="text-xl cursor-pointer" />
        </div>
      </div>

      <div className="p-3">
        <div className="flex items-center bg-gray-800 px-3 py-2 rounded-lg">
          <FaSearch className="text-gray-400" />
          <input
            type="text"
            placeholder="Search"
            className="bg-transparent ml-2 text-white outline-none w-full"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
      </div>

      <div className="flex-1 overflow-y-auto">
        {chats.length === 0 ? (
          <p className="text-center text-gray-400 mt-4">No users found</p>
        ) : (
          chats.map((chat) => (
            <div
              key={chat.id}
              className="flex items-center p-3 border-b border-gray-700 hover:bg-gray-800 cursor-pointer"
              onClick={() => {
                onSelectChat(chat);
                navigate("/chat");
              }}
            >
              <img
                src={chat.img || "https://via.placeholder.com/50"}
                alt={chat.name}
                className="w-12 h-12 rounded-full"
              />
              <div className="ml-3 flex-1">
                <h3 className="text-sm font-semibold">{chat.name}</h3>
                <p className="text-xs text-gray-400 truncate">
                  {chat.lastMessage || "No messages yet"}
                </p>
              </div>
              <div className="text-xs text-gray-400">
                <span>{chat.time || ""}</span>
                {chat.unread > 0 && (
                  <span className="bg-green-500 text-white px-2 py-1 rounded-full text-xs ml-2">
                    {chat.unread}
                  </span>
                )}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ChatListSidebar;
