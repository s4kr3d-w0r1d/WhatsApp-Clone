import { useState } from "react";

const SendMessage = ({ addMessage }) => {
  const [value, setValue] = useState("");
  const [file, setFile] = useState(null);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleSendMessage = (e) => {
    e.preventDefault();
    if (!value.trim() && !file) return;

    let mediaUrl = null;
    let mediaType = null;

    if (file) {
      mediaUrl = URL.createObjectURL(file);
      mediaType = file.type.split("/")[0];
    }

    addMessage(value, file);
    setValue(""); // Clear input
    setFile(null);
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
        <input
          type="file"
          onChange={handleFileChange}
          className="ml-2 bg-gray-200 text-gray-600 rounded-lg p-1"
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
