import { Link, useNavigate } from "react-router-dom";
import { BiUser } from "react-icons/bi";
import { AiOutlineUnlock } from "react-icons/ai";
import { useState } from "react";
import axios from "axios";
// import bgImage from "/assets/bg3.jpg";
import bgImage from "../../assets/bg3.jpg";


const Register = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    // Send registration request to the backend
    try {
      const response = await axios.post("http://localhost:8080/auth/register", {
        username: username,
        email: email,
        password: password,
      });

      // Handle successful registration
      if (response.status === 200) {
        console.log("Registration successful:", response.data);
        navigate("/login"); // Navigate to login page after successful registration
      }
    } catch (error) {
      console.error("Registration failed:", error);
    }
  };

  return (
    <div
      className="text-white  w-screen h-screen flex  bg-cover"
      style={{
        backgroundImage: `url(${bgImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
    <div className="flex justify-center items-center h-screen w-screen">
      <div className="bg-slate-800/30 border border-slate-400 rounded-md p-8 shadow-lg backdrop-filter backdrop-blur-sm bg-opacity-30 relative">
        <h1 className="text-4xl text-gray-100 font-bold text-center mb-6">
          Register
        </h1>
        <form onSubmit={handleRegister}>
          <div className="relative my-4">
            <input
              type="text"
              className="block w-72 py-2 px-0 text-sm text-white bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
              placeholder="Your Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            {/* <label htmlFor="" className='absolute text-sm text-white duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6 peer-focus:text-blue-600'>
                          Your username
                        </label> */}
            <BiUser className="absolute top-4 right-4" />
          </div>

          <div className="relative my-4">
            <input
              type="email"
              className="block w-72 py-2 px-0 text-sm text-white bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus-text:white focus:border-blue-600 peer"
              placeholder="Your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            {/* <label htmlFor="" className='absolute text-sm text-white duration-300 -transform translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-0'>
                          Your email
                        </label> */}
            <BiUser className="absolute top-4 right-4" />
          </div>

          <div className="relative my-4">
            <input
              type="password"
              className="block w-72 py-2 px-0 text-sm text-white bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:text-white focus:border-blue-600 peer"
              placeholder="Your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            {/* <label htmlFor="" className='absolute text-sm text-white duration-300 -transform translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-0'>
                          Your password
                        </label> */}
            <AiOutlineUnlock className="absolute top-4 right-4" />
          </div>

          <div className="relative my-4">
            <input
              type="password"
              className="block w-72 py-2 px-0 text-sm text-white bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:text-white focus:border-blue-600 peer"
              placeholder="Confirm password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            {/* <label htmlFor="" className='absolute text-sm text-white duration-300 -transform translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-focus:dark:text-blue-500 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-0'>
                          Confirm password
                        </label> */}
            <AiOutlineUnlock className="absolute top-4 right-4" />
          </div>

          <button
            className="w-full mb-4 text-[18px] mt-6 rounded-full bg-white text-pink-500 hover:bg-pink-400 hover:text-white py-2 transition-colors duration-300"
            type="submit"
          >
            Register
          </button>
          <div>
            <span className="m-4">
              {" "}
              Already Have An Account?
              <Link className="text-blue-500" to="/login">
                Login
              </Link>
            </span>
          </div>
        </form>
      </div>
    </div>
    </div>
  );
};

export default Register;
