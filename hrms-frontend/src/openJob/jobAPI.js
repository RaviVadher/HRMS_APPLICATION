import toast from "react-hot-toast";
import api from "../api/axios";

//get all jobs
export const getJob= async()=>{
    const res = await api.get("/jobs/allJob");
    return res.data;
}

//get all users
export const getUsers = async () => {
  const res = await api.get("/auth/getEmployee");
  return res;
};


// get job by id
export const getJobById = async (id) => {
  const res = await api.get(`/jobs/${id}`);
  return res.data;
};

// create job 
export const createJob = async (data) => {
  const res = await api.post("/jobs/create", data);
  return res.data;
  
};



// share job email
export const shareJob = async (jobId,email) => {
  const data =new  FormData();
  data.append("email",email);
  const res = await api.post(`/jobs/${jobId}/share`, data);
  return res.data;
};


// list shared jobs by jobId
export const getShares = async (jobId) => {
  const res = await api.get(`/jobs/${jobId}/allShared`);
  return res.data;
};



// refer friend with CV upload
export const referFriend = async (formData) => {
  const res = await api.post("/jobs/refer", formData);
  return res.data;
};

// get referrals by job
export const getRefers = async (jobId) => {
  const res = await api.get(`/jobs/${jobId}/refers`);
  return res.data;
};

//get cv by referId
export const getCvUrl = async(referId)=>{
   const res = await api.get(
     `/jobs/refer/${referId}/downloadCv`,
     {responseType:"blob"}
   );

   return res;
}

//update job status
export const updateJobStatus = async(jobId,status)=>{
   const res = await api.patch( `/jobs/${jobId}/chageStatus`,status);
   return res.data;
}