import { useState } from "react";
import { useLocation,useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { verifyOtp } from "./passwordApi";
export default function VerifyOtp(){

  const [otp,setOtp] = useState("");
  const navigate = useNavigate();
  const location = useLocation();

  const email = location.state?.email;

  const handleSubmit = async(e)=>{
    e.preventDefault();

    try{
      await verifyOtp(email,otp);
      toast.success("OTP verified");
      navigate("/reset-password",{state:{email}});
    }
    catch{
      toast.error("Invalid OTP");
    }
  };

  return(

    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <form onSubmit={handleSubmit}
        className="bg-white p-8 rounded-lg shadow-md w-96">
        <h2 className="text-xl font-bold mb-6 text-center">
          Verify OTP
        </h2>

        <input
          type="text"
          placeholder="Enter OTP"
          className="w-full p-2 border rounded mb-4"
          value={otp}
          required
          onChange={(e)=>setOtp(e.target.value)} />

        <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded">
          Verify OTP
        </button>
      </form>

    </div>
  );
}
