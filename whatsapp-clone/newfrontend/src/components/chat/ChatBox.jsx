import { useRef, useEffect } from "react";
import Message from "./Message";

const ChatBox = ({ messages }) => {
  const chatRef = useRef(null);

  useEffect(() => {
    if (chatRef.current) {
      chatRef.current.scrollTop = chatRef.current.scrollHeight;
    }

    console.log(messages);
  }, [messages]);

  return (
    <div
    ref={chatRef}
    className="flex flex-col-reverse h-[calc(100vh-200px)] overflow-y-auto mt-8 w-full"
  >
    {messages
      .slice()
      .reverse()
      .map((message) => (
        <div key={message.id} className="p-2">
          <Message message={message} />
          {message.mediaUrl && (
            <div className="mt-2">
              {message.mediaType === "image" ? (
                <img
                  src={message.mediaUrl}
                  alt="Sent media"
                  className="max-w-xs rounded-lg"
                />
              ) : message.mediaType === "video" ? (
                <video controls className="max-w-xs rounded-lg">
                  <source src={message.mediaUrl} type="video/mp4" />
                </video>
              ) : null}
            </div>
          )}
        </div>
      ))}
  </div>
  );
};

export default ChatBox;
