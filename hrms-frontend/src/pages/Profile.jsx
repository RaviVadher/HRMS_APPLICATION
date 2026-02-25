import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate, useParams } from "react-router-dom";
import toast from "react-hot-toast";
import api from "../api/axios";
import DashboardLayout from "../layout/DashboardLayout";
import { CircularProgress } from "@mui/material";

function Profile() {

    const { user, loading } = useAuth();
    const { id } = useParams();
    const [email, setEmail] = useState("");
    const [userName, setUsername] = useState("");
    const [birthDate, setBirthdate] = useState("");
    const [joiningDate, setJoinDate] = useState("");
    const [designation, setDesignation] = useState("");


    useEffect(() => {
        getProfile(id);
    }, [id]);

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
        <div className=" flex mt-10 justify-center bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md w-96">
                <h2 className="text-2xl font-bold mb-6 text-center">Profile</h2>
                <p><strong>Email:</strong> {email}</p>
                <p><strong>Username:</strong> {userName}</p>
                <p><strong>Birth Date:</strong> {birthDate}</p>
                <p><strong>Join Date:</strong> {joiningDate}</p>
                <p><strong>Designation:</strong> {designation}</p>
            </div>
        </div>
    </DashboardLayout>
);
}

export default Profile;