import { createContext, useContext, useState, useEffect, Children } from "react";
import api from "../api/axios";
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [role,setRole] = useState();
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            setUser({ token });
        }
    }, []);

    const login = async (email, password) => {
        try {
            const response = await api.post("/auth/login", {
                email, password
            });

            const token = response.data;
            localStorage.setItem("token", token);
            setUser({ token });
             setRole(jwtDecode(token).role);
            return { success: true };
        } catch (error) {

            return { success: false, message: "Invalid credential" };

        }
    };

    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
    };


    return (
        <AuthContext.Provider value={{ user, login, logout,role }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
