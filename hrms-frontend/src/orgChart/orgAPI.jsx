import api from "../api/axios";

//Get ORGCHART
export const getOrgChart = async (id) => {
  const res = await api.get(`/orgchart/${id}`);
  return res.data;
};

// Get employee by search
export const searchEmployees = async (name) => {
  
  const res = await api.get(`/orgchart/search?name=${name}`);
  return res.data;
};

//Get root
export const getRoots = async () => {
  const res = await api.get("/orgchart/roots");
  return res.data;
};