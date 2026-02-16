import { createContext, useContext, useState, useEffect, Children } from "react";
import api from "../api/axios";
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token,setToken] = useState(null);
    const [loading,setLoading] = useState(true);
  

    useEffect(() => {
        const savedToken = localStorage.getItem("token");
        if (savedToken) {
            try{
            const decode = jwtDecode(savedToken);
            setUser({ 
                id:decode.id,
                role:decode.role,
                email:decode.sub
            });

            setToken(savedToken);
            }
            catch{
                logout();
            }    
        }
        setLoading(false);
    }, []);

    const login = async (email, password) => {
      
        try {
            const response = await api.post("/auth/login", {
                email, password
            });

            const restoken = response.data;
            localStorage.setItem("token", restoken);
             const decode = jwtDecode(restoken);

             const userData = { 
                id:decode.id,
                role:decode.role,
                email:decode.sub
            };

            setUser(userData);
            setToken(restoken);
            console.log(userData);
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
        <AuthContext.Provider value={{ user, login, logout,loading}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
