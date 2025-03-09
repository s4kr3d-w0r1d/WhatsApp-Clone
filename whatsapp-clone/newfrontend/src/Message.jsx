const Message = ({ message }) => {
  const isMyMessage = message.sender === "You";
  return (
    <div>
      <div
        className={`chat ${isMyMessage ? "chat-end pr-0 mr-0" : "chat-start"}`}
      >
        <div className="chat-image avatar">
          <div className="w-10 rounded-full">
            <img
              alt="Tailwind CSS chat bubble component"
              src="https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.webp"
            />
          </div>
        </div>
        <div className="chat-header">
          {message.sender}
          <time className="text-xs opacity-50">{message.time}</time>
        </div>
        <div className="chat-bubble">{message.text}</div>
        <div className="chat-footer opacity-50">Delivered</div>
      </div>
    </div>
  );
};

export default Message;
