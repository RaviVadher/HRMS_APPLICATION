import { useState } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { sendOtp } from "./passwordApi";
import CircularProgress from "@mui/material/CircularProgress";

export default function ForgotPassword() {

  const [email, setEmail] = useState("");
  const [loading,setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);
      await sendOtp(email);
      toast.success("OTP sent to your registered email");
      navigate("/verify-otp", { state: { email } });
    } catch {
      toast.error("Failed to Send OTP");
    }finally{
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <form onSubmit={handleSubmit}
        className="bg-white p-8 rounded-lg shadow-md w-96">
        <h2 className="text-xl font-bold mb-6 text-center">
          Forgot Password
        </h2>

        <input
          type="email"
          placeholder="Enter Username"
          className="w-full p-2 border rounded mb-4"
          value={email}
          required
          onChange={(e) => setEmail(e.target.value)}/>
          {loading ? (
            <div>
              <CircularProgress  size={24}/> 
            </div>
          ):( <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded">
          Send OTP
        </button>)}
       
      </form>

    </div>
  );
}