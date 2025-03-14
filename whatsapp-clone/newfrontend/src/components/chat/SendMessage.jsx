import { useState } from "react";

const SendMessage = ({ addMessage }) => {
  const [value, setValue] = useState("");

  const handleSendMessage = (e) => {
    e.preventDefault();
    if (!value.trim()) return; // Prevent sending empty messages

    addMessage(value); // Call function from `ChatRoom` to add message
    setValue(""); // Clear input
  };

  return (
    <div className="bg-gray-200 fixed bottom-0 left-[33.5%] w-[66.5%]  py-4 shadow-lg z-50">
      <form
        onSubmit={handleSendMessage}
        className="flex items-center px-4 bg-gray-200"
      >
        <input
          value={value}
          onChange={(e) => setValue(e.target.value)}
          className="w-full px-3 py-2 rounded-l-lg focus:outline-none bg-gray-100 text-gray-600"
          type="text"
          placeholder="Type a message..."
        />
        <button
          type="submit"
          className="bg-gray-500 text-white px-4 py-2 rounded-r-lg"
        >
          Send
        </button>
      </form>
    </div>
  );
};

export default SendMessage;
