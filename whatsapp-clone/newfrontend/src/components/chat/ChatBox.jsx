import { useRef, useEffect } from "react";
import Message from "./Message";

const ChatBox = ({ messages }) => {
  const chatRef = useRef(null);

  useEffect(() => {
    if (chatRef.current) {
      chatRef.current.scrollTop = chatRef.current.scrollHeight;
    }
  }, [messages]);

  return (
    <div
      ref={chatRef}
      className="flex flex-col-reverse h-[calc(100vh-200px)] overflow-y-auto mt-0 w-full"
    >
      {messages.slice().reverse().map((message) => ( 
        <Message key={message.id} message={message} />
      ))}
    </div>
  );
};

export default ChatBox;
