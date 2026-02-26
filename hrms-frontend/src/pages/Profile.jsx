import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate, useParams } from "react-router-dom";
import toast from "react-hot-toast";
import api from "../api/axios";
import DashboardLayout from "../layout/DashboardLayout";
import { CircularProgress } from "@mui/material";

function Profile() {

    const { user, loading, logout } = useAuth();
    const { id } = useParams();
    const [email, setEmail] = useState("");
    const [userName, setUsername] = useState("");
    const [birthDate, setBirthdate] = useState("");
    const [joiningDate, setJoinDate] = useState("");
    const [designation, setDesignation] = useState("");
    const navigate = useNavigate();


    useEffect(() => {
        getProfile(id);
    }, [id]);

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    const getProfile = async (id) => {

        const res = await api.get(`/orgchart/user/${id}/profile`)
        console.log(res.data);
        setEmail(res.data.email);
        setUsername(res.data.userName);
        setBirthdate(res.data.birthDate);
        setJoinDate(res.data.joiningDate);
        setDesignation(res.data.designation);
    }
    if (loading) return <DashboardLayout><CircularProgress size={24} /></DashboardLayout>


    return (
        <DashboardLayout>
            <div className=" min-h-screen flex items-center justify-center bg-gray-100 px-4">
                <div className="w-full max-w-md bg-white rounded-lg shadow-sm border p-6">
                    <h2 className="text-2xl font-semibold text-gray-800 mb-6 text-center">Profile</h2>
                    <p><strong>Email:</strong> {email}</p>
                    <p><strong>Username:</strong> {userName}</p>
                    <p><strong>Birth Date:</strong> {birthDate}</p>
                    <p><strong>Join Date:</strong> {joiningDate}</p>
                    <p><strong>Designation:</strong> {designation}</p>

                    <div className="mt-6 pt-4 border-t">
                        <button
                            onClick={handleLogout}
                            className="w-full py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition">
                            Logout
                        </button>
                    </div>
                </div>
            </div>

        </DashboardLayout>
    );
}

export default Profile;