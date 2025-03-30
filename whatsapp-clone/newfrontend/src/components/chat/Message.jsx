import { useState, useEffect } from "react";
import axios from "axios";

const Message = ({ message }) => {
  const loggedInUserId = sessionStorage.getItem("loggedInUserId");
  const loggedInUserProfilePic =
    sessionStorage.getItem("loggedInUserProfilePic") || "/default-avatar.jpg";

  const isMyMessage = message.sender.id == loggedInUserId;

  const [senderProfilePic, setSenderProfilePic] = useState(
    "/default-avatar.jpg"
  );

  useEffect(() => {
    if (!isMyMessage) {
      axios
        .get(`http://localhost:8080/profile/${message.sender.id}`)
        .then((response) => {
          if (response.data.profilePictureUrl) {
            setSenderProfilePic(response.data.profilePictureUrl);
          }
        })
        .catch((error) => {
          console.error("Error fetching sender's profile:", error);
        });
    }
  }, [message.sender.id, isMyMessage]);

  // Use sender's profile picture for received messages
  const profilePic = isMyMessage ? loggedInUserProfilePic : senderProfilePic;

  return (
    <div>
      <div
        className={`chat ${isMyMessage ? "chat-end pr-0 mr-0" : "chat-start"}`}
      >
        <div className="chat-image avatar">
          <div className="w-10 rounded-full">
            <img
              alt="User Profile"
              src={profilePic}
              onError={(e) => (e.target.src = "/default-avatar.jpg")}
            />
          </div>
        </div>
        <div className="chat-header">
          {isMyMessage ? "You" : message.sender?.name || "Unknown User"}
          <time className="text-xs opacity-50">{message.time}</time>
        </div>

        {/* Display message content or media */}
        <div className="chat-bubble">
          {message?.content && <p>{message.content}</p>}
          {message?.mediaUrl && (
            <div className="mt-2">
              {message.mediaType === "image" ? (
                <img
                  src={message.mediaUrl}
                  alt="Sent Image"
                  className="max-w-xs rounded-lg"
                />
              ) : message.mediaType === "video" ? (
                <video controls className="max-w-xs rounded-lg">
                  <source src={message.mediaUrl} type="video/mp4" />
                </video>
              ) : (
                <a
                  href={message.mediaUrl}
                  className="text-blue-500 underline"
                  download
                >
                  Download File
                </a>
              )}
            </div>
          )}
        </div>

        <div className="chat-footer opacity-50 flex justify-end text-xs">
          <time>
            {new Date(message.timestamp).toLocaleTimeString("en-GB", {
              hour: "2-digit",
              minute: "2-digit",
              hour12: false,
            })}
          </time>
          <span className="ml-1">• Delivered</span>
        </div>
      </div>
    </div>
  );
};

export default Message;
