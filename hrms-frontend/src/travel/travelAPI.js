import toast from "react-hot-toast";
import api from "../api/axios";
//HR View:
// GET all travels
export const getAllTravels = async () => {
  const res = await api.get("/travels/getAll");
  return res.data;
};

// GeT travel by id
export const getTravelById = async (id) => {
  const res = await api.get(`/travels/${id}`);
  return res.data;
};

//get all by assignId and travelId
export const getAssignById = async (id) => {
  const res = await api.get(`/travels/${id}/assigns/getAll`);
  return res.data;
};

// CREATE travel
export const createTravel = async (data) => {
     const res = await api.post("/travels/create", data);
     return res.data;

};

// GET employees list
export const getEmployees = async () => {
  const res = await api.get("/auth/getEmployee");
  return res.data;
};

//assign 
export const assignEmployeeToTravel = async (travelId, employeeId) => {
  const res = await api.post(
    `/travels/${travelId}/assign/${employeeId}`,
  );
  return res.data;
};

// UPLOAD document
export const uploadTravelDocument = async (assignedId,filetype,docname,file) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("docname",docname);
    formData.append("filetype", filetype);
  const res = await api.post(
    `/travels/${assignedId}/documnets`,
    formData,{
    headers:{
      'Content-Type':'multipart/form-data'
    },
  });

  return res.data;
};

//Get documents list by assignId
export const getAllDocument = async (assignedId) => {
  const res = await api.get(
    `/travels/${assignedId}/uploaded`,
  );
  return res.data;
};

//view document
export const getDocUrl = async (docId) => {
  const res = await api.get(
    `/travels/${docId}/download`,{responseType:"blob"}
  );
  return res;
};

//Employee View:
export const getMyTravels = async () => {
  const res = await api.get("/travels/my");
  return res.data;
};

export const getTeamTravels = async () => {
  const res = await api.get("/travels/team");
  return res.data;
};

export const getMyTravelsById= async (id) => {
  const res = await api.get(`/travels/my/${id}`);
  return res.data;
};

//upadat sttus
export const updateExpenseStatus= async (expenseId, data) => {
  console.log(data);
  const res = await api.post(`/travels/assign/expense/${expenseId}/changestatus`,data);
  return res.data;
};



//Upload employee document
export const uploadEmployeeDocument = async (travelId, file) => {
  const form = new FormData();
  form.append("file", file);

  const res = await api.post(
    `/travels/${travelId}/documents/employee`,
    form
  );
  return res.data;
};

//Post Expense
export const createExpense = async (travelId, formData) => {
  const res = await api.post(
    `/travels/${travelId}/assign/expense`,
    formData
  );
  return res.data;
};

//get Expense
export const getMyExpenses = async (assign_id) => {
  const res = await api.get(
    `/travels/${assign_id}/expense`
  );
  console.log(res.data)
  return res.data;
};


//get proof list
export const getExpeseProofs = async(expense_id)=>{
   const res = await api.get(
     `/travels/proof/${expense_id}`
   );

   return res.data;
}

//get proof url
export const getProofUrl = async(proof_id)=>{
   const res = await api.get(
     `/travels/${proof_id}/downloadProofs`,
     {responseType:"blob"}
   );

   return res;
}