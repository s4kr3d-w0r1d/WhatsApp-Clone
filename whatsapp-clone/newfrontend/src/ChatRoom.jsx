import { useState } from "react";
import ChatBox from "./ChatBox";
import SendMessage from "./SendMessage";
import Navbar from "./Navbar";

const ChatRoom = () => {
  const [messages, setMessages] = useState([
    { sender: "Friend", text: "Hey!", time: "10:00 AM" },
    { sender: "You", text: "Hello!", time: "10:01 AM" },
  ]);

  const addMessage = (text) => {
    const newMessage = {
      id: messages.length + 1,
      text,
      sender: "You", // Hardcoded for now, update based on user login
      time: new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      }),
    };
    setMessages([...messages, newMessage]); // Append new message
  };

  return (
    <>
      <Navbar />
      <ChatBox messages={messages} />
      <SendMessage addMessage={addMessage} />
    </>
  );
};

export default ChatRoom;
