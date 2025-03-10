import { Link, useNavigate } from "react-router-dom";

import Spline from "@splinetool/react-spline";

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="relative w-screen h-screen">
      <nav className="absolute top-0 left-0 w-full bg-black/50 text-white p-4 flex justify-between">
        <h1 className="text-xl font-bold">Whatsapp Clone</h1>
        <ul className="flex gap-4">
          <li>
            <Link to="/register" className="hover:underline">
              Register
            </Link>
          </li>
          <li>
            <Link to="/login" className="hover:underline">
              Login
            </Link>
          </li>
        </ul>
      </nav>

      <div className="absolute top-50 left-1/2 transform -translate-x-1/2 text-white text-center z-10">
        <h2 className="text-3xl font-bold">Welcome to whatsapp clone</h2>
        <p className="text-lg mt-2">
          Explore and interact with people in real-time.
        </p>

        <Link to="/register">
          <button className="btn btn-outline">Get Started</button>
        </Link>
      </div>

      <Spline scene="https://prod.spline.design/xow0nplAxSHNmPaP/scene.splinecode" />

      <div className="absolute bottom-4 right-5 bg-black text-white p-2 rounded">
        <button className="btn btn-ghost w-30">About Us</button>
      </div>
    </div>
  );
};

export default Home;
