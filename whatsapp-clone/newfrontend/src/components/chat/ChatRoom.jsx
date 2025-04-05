import { useEffect, useState } from "react";
import ChatBox from "./ChatBox";
import SendMessage from "./SendMessage";
import Navbar from "../layout/Navbar";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useNavigate } from "react-router-dom";

const ChatRoom = ({ selectedChat }) => {
  const navigate = useNavigate();

  const [chatMessages, setChatMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
 // const [selectedFileType, setSelectedFileType] = useState("");

  const senderId = Number(sessionStorage.getItem("loggedInUserId"));
  const recipientId = Number(selectedChat?.id);

  useEffect(() => {
    if (!selectedChat || !senderId || !recipientId) {
      console.log("Missing sender or recipient ID!");
      return;
    }

    console.log("Sender ID:", senderId);
    console.log("Recipient ID:", recipientId);

    fetch(
      `http://localhost:8080/api/messages/chat-history?userId1=${senderId}&userId2=${recipientId}`
    )
      .then((response) => response.json())
      .then((data) =>  setChatMessages((prev) => ({ ...prev, [recipientId]: data })))
      .catch((error) => console.error("Error fetching chat history:", error));

    console.log("Sender ID is", senderId);
    console.log("Recipient ID is", recipientId);

    if (stompClient) {
      console.log("Disconnecting previous WebSocket...");
      stompClient.deactivate(); // Ensure the old connection is closed
    }

    // WebSocket setup
    const socket = new SockJS("http://localhost:8080/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: (frame) => {
        console.log("Connected to WebSocket", frame);

        // Subscribe to private messages for recipientId
        client.subscribe(`/topic/messages/${senderId}`, (message) => {
          const newMessage = JSON.parse(message.body);
          const chatId = newMessage.sender.id === senderId ? newMessage.recipient.id : newMessage.sender.id;
          console.log(`Subscribed to /topic/messages/${senderId}`);

          console.log("Received message:", JSON.parse(message.body));
          console.log("Received message is", newMessage);

          // setMessages((prev) => [...prev, newMessage]);
          setChatMessages((prev) => ({
            ...prev,
            [chatId]: [...(prev[chatId] || []), newMessage],
          }));

        });
      },
      onDisconnect: () => console.log("Disconnected from WebSocket"),
    });

    client.activate();
    setStompClient(client);
    console.log("Attempting to connect to WebSocket...");

    return () => {
      // client.deactivate();
      // if (stompClient && stompClient.active) {
      //   stompClient.deactivate();
      //   console.log("WebSocket Disconnected!");
      // }
      if (client) {
        console.log("Cleaning up WebSocket...");
        client.deactivate();
      }
    };
  }, [selectedChat]);

  // **upload file**
  const uploadFile = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(
        "http://localhost:8080/api/messages/upload-media",
        {
          method: "POST",
          body: formData,
        }
      );

      if (!response.ok) throw new Error("Failed to upload file");

      return await response.text();
    } catch (error) {
      console.error("Error uploading file:", error);
      return null;
    }
  };

  const sendMessage = async (text, file) => {
    if (!senderId || !recipientId) {
      console.error("Sender or recipient ID is missing!");
      return;
    }
    let mediaUrl = null;
    let mediaType = null;

    if (file) {
      setSelectedFile(file);
      mediaUrl = await uploadFile(file);
      if (!mediaUrl) {
        console.error("Failed to upload file");
        return;
      }
      mediaType = file.type.split("/")[0]; // "image", "video", "application", etc.
    }

    console.log("Before sending message:");
    console.log("Content:", text);
    console.log("Media URL:", mediaUrl ? mediaUrl : "No media attached");
    console.log("Media Type:", mediaType ? mediaType : "No media type");

    const chatMessage = {
      senderId,
      recipientId,
      content: text || "",
      mediaUrl,
      mediaType,
    };

    const destination = mediaUrl
      ? "/app/send-media-message"
      : "/app/send-message";

    // **Optimistically update UI before sending message**
    const newMessage = {
      id: Date.now(),
      sender: { id: senderId },
      recipient: { id: recipientId },
      content: text,
      mediaUrl,
      mediaType,
      timestamp: new Date().toISOString(), // Ensures proper date format
    };

    console.log("File URL before sending:", newMessage.mediaUrl);
    console.log("Message Payload:", newMessage);
    console.log("Destination:", destination);

    //setMessages((prev) => [...prev, { ...newMessage, id: Date.now() }]);
    setChatMessages((prev) => ({
      ...prev,
      [recipientId]: [...(prev[recipientId] || []), newMessage],
    }));

    // Send message via WebSocket
    if (stompClient && stompClient.connected) {
      stompClient.publish({
        destination: destination,
        body: JSON.stringify(chatMessage),
      });
      console.log("Message sent via WebSocket", chatMessage);
    } else {
      console.error("WebSocket is not connected!");
    }
  };

  if (!selectedChat) {
    return (
      <div className="flex-1 flex items-center justify-center h-screen text-gray-400">
        <Navbar />
        <p className="text-xl">Select a chat to start messaging</p>
      </div>
    );
  }

  return (
    <div>
      <Navbar />
      <div
        key={selectedChat.id}
        className="p-4 ursor-pointer hover:bg-gray-700 border-b border-gray-700 bg-gray-800 text-white pt-11"
        onClick={() => navigate(`/profile/${selectedChat.id}`)}
      >
        <h2 className="text-lg font-semibold">{selectedChat?.name}</h2>
      </div>
      <ChatBox messages={chatMessages[recipientId] || []} />

      <SendMessage addMessage={sendMessage} />
    </div>
  );
};

export default ChatRoom;
