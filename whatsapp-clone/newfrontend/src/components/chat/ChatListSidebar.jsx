import { useState } from "react";
import { FaSearch, FaEllipsisV } from "react-icons/fa";
import { BsChatDotsFill } from "react-icons/bs";
import { useNavigate } from "react-router-dom";

const chats = [
  {
    id: 1,
    name: "yo",
    lastMessage: "hi ✨",
    time: "7:27 pm",
    unread: 7,
    img: "https://via.placeholder.com/50",
  },
  {
    id: 2,
    name: "Buy and Sell",
    lastMessage: "Jldi bhej wo intezar kar rha h",
    time: "7:17 pm",
    unread: 9,
    img: "https://via.placeholder.com/50",
  },
  {
    id: 3,
    name: " Family",
    lastMessage: "📹 Video call",
    time: "7:05 pm",
    unread: 0,
    img: "https://via.placeholder.com/50",
  },
];

const ChatListSidebar = ({ onSelectChat }) => {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  return (
    <div className="w-1/3 h-screen bg-gray-900 text-white flex flex-col pt-16">
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
        {chats
          .filter((chat) =>
            chat.name.toLowerCase().includes(search.toLowerCase())
          )
          .map((chat) => (
            <div
              key={chat.id}
              className="flex items-center p-3 border-b border-gray-700 hover:bg-gray-800 cursor-pointer"
              onClick={() => {
                onSelectChat(chat);
                navigate("/chat");
              }}
            >
              <img src={chat.img} alt={chat.name} className="w-12 h-12 rounded-full" />
              <div className="ml-3 flex-1">
                <h3 className="text-sm font-semibold">{chat.name}</h3>
                <p className="text-xs text-gray-400 truncate">{chat.lastMessage}</p>
              </div>
              <div className="text-xs text-gray-400">
                <span>{chat.time}</span>
                {chat.unread > 0 && (
                  <span className="bg-green-500 text-white px-2 py-1 rounded-full text-xs ml-2">
                    {chat.unread}
                  </span>
                )}
              </div>
            </div>
          ))}
      </div>
    </div>
  );
};

export default ChatListSidebar;
