import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";


export default function Navbar() {
    const { user, login, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/");
    };



    return (
        <nav className="bg-white shadow-md sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">

                <Link to="/" className="flex items-center gap-2">
                    <h1 className="text-xl font-bold text-indigo-700">Welcome to HRMS</h1>
                </Link>
                {!user ? (
                    <div className="flex gap-4">
                        <Link
                            to="/"
                            className="px-4 py-2 border border-indigo-600 text-indigo-600 rounded hover:bg-indigo-600 hover:text-white"
                        >
                            Login
                        </Link>
                    </div>
                ) : (
                    <div className="flex items-center gap-4">
                        <Link
                            to="/addtravel"
                            className="px-4 py-2 border border-indigo-600 text-indigo-600 rounded hover:bg-indigo-600 hover:text-white">Profile </Link>

                        <button
                            onClick={handleLogout}
                            className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600">
                            Logout</button>
                    </div>
                )}
            </div>
        </nav>
    );
}