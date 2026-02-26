import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Notification from "../pages/Notification";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';



export default function Navbar() {
    const { user, login } = useAuth();
    return (
        <>
            <nav className=" bg-blue-500  sticky top-0 z-50">
                <div className="max-w-6xl mx-auto px-5 py-4 flex justify-between items-center">
                    <Link to="/" className="flex items-center gap-2">
                        <h1 className="text-xl font-bold text-white"></h1>
                    </Link>
                    {!user ? (
                        <div className="flex gap-4">
                            <Link
                                to="/"
                                className="px-4 py-2 border border-indigo-600 text-indigo-600 rounded hover:bg-indigo-600 hover:text-white">
                                Login
                            </Link>
                        </div>
                    ) : (
                        <div className="flex items-center gap-4">
                            <Link
                                to={`/profile/${user.id}`}
                                className="px-4 py-2 border border-indigo-600 text-indigo-600 rounded hover:bg-indigo-600 hover:text-white"><AccountCircleIcon style={{ fontSize: 20, color: 'black' }} /> </Link>
                            <Notification userId={user.id} />
                        </div>)}
                </div>
                <hr />
            </nav>
        </>
    )
}