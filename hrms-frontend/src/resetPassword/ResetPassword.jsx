import { useState } from "react";
import { useLocation,useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { resetPassword } from "./passwordApi";
export default function ResetPassword(){

  const [password,setPassword] = useState("");
   const [repassword,setRepassword] = useState("");


  const navigate = useNavigate();
  const location = useLocation();

  const email = location.state?.email;
  const handleSubmit = async(e)=>{
    e.preventDefault();

    try{

      if(password!==repassword) {
        toast.error("Password does not matched")
         return;
      }
      await resetPassword(email,password);
      toast.success("Password reset successful");
      navigate("/");
    }
    catch{
      toast.error("Failed to reset password");
    }
  };

  return(

    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <form onSubmit={handleSubmit}
        className="bg-white p-8 rounded-lg shadow-md w-96">
        <h2 className="text-xl font-bold mb-6 text-center">
          Reset Password
        </h2>

        <input
          type="password"
          placeholder="New Password"
          className="w-full p-2 border rounded mb-4"
          value={password}
          required
          onChange={(e)=>setPassword(e.target.value)}/>

          <input
          type="password"
          required
          placeholder="Confirm Password"
          className="w-full p-2 border rounded mb-4"
          value={repassword}
          onChange={(e)=>setRepassword(e.target.value)}/>

        <button
          type="submit"
          className="w-full bg-blue-600 text-white p-2 rounded">
          Reset Password
        </button>
      </form>

    </div>
  );
}