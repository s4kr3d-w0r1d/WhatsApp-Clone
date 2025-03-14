import { useState } from "react";
import ChatBox from "./ChatBox";
import SendMessage from "./SendMessage";
import Navbar from "../layout/Navbar";

const ChatRoom = ({ selectedChat }) => {
  const [messages, setMessages] = useState([
    { id:1, sender: "Friend", text: "Hey!", time: "10:00 AM" },
    { id:2,  sender: "You", text: "Hello!", time: "10:01 AM" },
  ]);

 

  const addMessage = (text) => {
    const newMessage = {
      // id: messages.length + 1,
      id: Date.now(),
      text,
      sender: "You", // Hardcoded for now, update based on user login
      time: new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      }),
    };
    setMessages([...messages, newMessage]); // Append new message
  };

  if (!selectedChat) {
    return (
      <div className="flex-1 flex items-center justify-center text-gray-400">
        Select a chat to start messaging
      </div>
    );
  }

  return (
    <>
      <Navbar />
      <div className="p-4 border-b border-gray-700 bg-gray-900 text-white pt-20">
        <h2 className="text-lg font-semibold">{selectedChat.name}</h2>
      </div>
      <ChatBox messages={messages} />
      <SendMessage addMessage={addMessage} />
    </>
   
    
  );
};

export default ChatRoom;
