import { useNavigate } from "react-router-dom";

const AboutUs = () => {
  const navigate = useNavigate();

  return (
    <div className="h-screen w-full bg-gray-900 text-white flex flex-col items-center pt-15 ">
      <h1 className="text-3xl font-bold">About Us</h1>
      <p className="mt-4 text-center max-w-md text-gray-400">
        Welcome to our chat app! We aim to provide a seamless messaging experience with real-time communication, file sharing, and more.
      </p>
      
      <button class="btn btn-outline btn-primary"
      onClick={() => navigate("/")}> Back to Home</button>
    </div>
  );
};

export default AboutUs;
