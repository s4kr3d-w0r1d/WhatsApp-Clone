import { useEffect, useState } from "react";
import ChatBox from "./ChatBox";
import SendMessage from "./SendMessage";
import Navbar from "../layout/Navbar";
import axios from "axios";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const ChatRoom = ({ selectedChat }) => {
  const [messages, setMessages] = useState([]);
  const senderId = sessionStorage.getItem("loggedInUserId");
  const recipientId = selectedChat?.id;
  const [stompClient, setStompClient] = useState(null);

  const addMessage = async (text) => {
    if (!senderId || !recipientId) {
      console.error("Sender or recipient ID is missing!");
      return;
    }

    try {
      const response = await axios.post(
        `http://localhost:8080/messages?senderId=${senderId}&recipientId=${recipientId}&content=${encodeURIComponent(
          text
        )}`
      );

      const data = { recipientId, message: text };

      setMessages((prevMessages) => [...prevMessages, response.data]);

      if (stompClient && stompClient.connected) {
        stompClient.publish({
          destination: `/app/private/${recipientId}`,
          body: JSON.stringify(data),
        });

        console.log("message sent on socket");
      }
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  useEffect(() => {
    if (!selectedChat || !senderId || !recipientId) return;

    const fetchMessages = async () => {
      try {
        const response = await axios.get("http://localhost:8080/messages", {
          params: { userId1: senderId, userId2: recipientId },
        });
        setMessages(response.data);
      } catch (error) {
        console.error("Error fetching messages:", error);
      }
    };

    fetchMessages();

    // WebSocket setup
    const socket = new SockJS("http://localhost:8080/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("Connected to WebSocket");

        // Subscribe to private messages
        
        client.subscribe(`/user/${recipientId}/queue/messages`, (message) => {
          const newMessage = JSON.parse(message.body);
          if (newMessage) {
            setMessages((prev) => [...prev, newMessage]);
          }
        });
      },
      onDisconnect: () => console.log("Disconnected from WebSocket"),
    });

    client.activate();
    setStompClient(client);

    return () => {
      if (client) {
        client.deactivate();
      }
    };
  });

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
      <div className="p-4 border-b border-gray-700 bg-gray-900 text-white pt-11">
        <h2 className="text-lg font-semibold">{selectedChat?.name}</h2>
      </div>
      <ChatBox messages={messages} />
      <SendMessage addMessage={addMessage} />
    </>
  );
};

export default ChatRoom;

