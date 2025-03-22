import { Link, useNavigate } from "react-router-dom";
import { BiUser } from "react-icons/bi";
import { AiOutlineUnlock } from "react-icons/ai";
import { useState } from "react";
import axios from "axios";

import bgImage from "../../assets/bg3.jpg";

const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await axios.post("http://localhost:8080/auth/login", {
        email,
        password,
      });

      if (response.status === 200) {
        console.log("login successful");
        //  console.log(response.data);
        // sessionStorage.setItem("token", response.data.token);
        // sessionStorage.setItem("loggedInUserId", response.data.user.id);
        
    const { token, user } = response.data; // Extract token & user details
    console.log("Login Response:", response.data);

    sessionStorage.setItem("token", token);
    sessionStorage.setItem("loggedInUserId", user.id);  // ✅ Store userId
    sessionStorage.setItem("loggedInUserEmail", user.email); // ✅ Store email
    sessionStorage.setItem("loggedInUserName", user.name); // Optional

    console.log("User ID:", user.id);
      console.log("User Email:", user.email);
      console.log("User Name:", user.name);
      console.log("JWT Token:", token);


        // Navigate to chat page after login
        navigate("/chat");
      }
    } catch (error) {
      console.log(error);
      setError(
        error.response?.data?.message || "Login failed. Please try again."
      );
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
            Login
          </h1>
          {error && <p className="text-red-500 text-center">{error}</p>}
          <form onSubmit={handleLogin}>
            <div className="relative my-4">
              <input
                type="email"
                className="block w-72 py-2 px-0 text-sm text-white bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:text:white focus:border-blue-600 peer"
                placeholder="Your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
              {/* <label className='absolute text-sm text-white duration-300 -transform translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-0'>Your email</label> */}
              <BiUser className="absolute top-4 right-4" />
            </div>
            <div className="relative my-4">
              <input
                type="password"
                className="block w-72 py-2 px-0 text-sm text-white bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:text-white focus:border-blue-600 peer"
                placeholder="Your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              {/* <label className='absolute text-sm text-white duration-300 -transform translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-0'>Your password</label> */}
              <AiOutlineUnlock className="absolute top-4 right-4" />
            </div>
            <div className="flex justify-between items-center">
              <div className="flex gap-2 items-center">
                <input type="checkbox" name="" id="" />
                <label>Remember Me</label>
              </div>
              <Link to="" className="text-blue-500">
                Forgot password?
              </Link>
            </div>
            <button
              className="w-full mb-4 text-[18px] mt-6 rounded-full bg-white text-pink-500 hover:bg-pink-400 hover:text-white py-2 transition-colors duration-600"
              type="submit"
            >
              Login
            </button>
            <div>
              <span className="m-4">
                New Here?
                <Link className="text-blue-500" to="/register">
                  Create an account
                </Link>
              </span>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;
