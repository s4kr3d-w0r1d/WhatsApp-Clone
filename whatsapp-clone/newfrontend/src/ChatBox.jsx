import Message from "./Message";

const ChatBox = ({ messages }) => {
  return (
    <div className="pb-44 pt-20 w-full ">
      {messages.map((message) => (
        <Message key={message.id} message={message} />
      ))}
    </div>
  );
};

export default ChatBox;
