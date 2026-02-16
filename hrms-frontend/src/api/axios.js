import axios from "axios";
import ErrorHandler from "../utils/ErrorHandler";


const api = axios.create({
    baseURL: "http://localhost:8081/api",
});

api.interceptors.request.use((config)=>{

    const token = localStorage.getItem("token");

    if(token)
    {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

api.interceptors.response.use(
    res => res,
    err => {
        err.customMessage = ErrorHandler.getMessage(err);
        return Promise.reject(err);
    }
)

export default api;