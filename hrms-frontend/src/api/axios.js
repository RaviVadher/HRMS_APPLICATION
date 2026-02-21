import axios from "axios";
import toast from "react-hot-toast";


const api = axios.create({
    baseURL: "http://localhost:8080/api",
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
    res => {
        if(res.data?.message || res.data?.msg)
        {
            toast.success(res.data.message || res.data.msg);
        }
        return res;
    },
    err => {
        console.log(err.response)
        const message =
        err.response?.data?.msg ||
        err.msg||
        err.response?.data?.error ||
        "unexpected error"

        toast.error(message);
        return Promise.reject(err);
    }
)

export default api;