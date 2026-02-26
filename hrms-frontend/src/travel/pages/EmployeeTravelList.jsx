import { useEffect, useState } from "react";
import DashboardLayout from "../../layout/DashboardLayout";
import { getMyTravels } from "../travelAPI"
import { useNavigate } from "react-router-dom";
import { Button, TextField, MenuItem } from "@mui/material";
import { useAuth } from "../../context/AuthContext";

const EmployeeTravelList = () => {
  const [travels, setTravels] = useState([]);
  const { user } = useAuth();
  const [filter, setFilter] = useState("ALL");
  const navigate = useNavigate();

  useEffect(() => {
    fetchTravels();
  }, []);

  const fetchTravels = async () => {
    const res = await getMyTravels();
    setTravels(res || []);
    console.log(travels)
  };

  let filtred = null;
  if (filter === "ALL") {
    filtred = travels;
  }
  else if (filter === "Upcoming") {
    filtred = travels.filter((t) => new Date(t.startDate) > new Date())
  }
  else if (filter === "Ongoing") {
    filtred = travels.filter((t) => (new Date(t.startDate) <= new Date()) && (new Date(t.endDate) >= new Date()))
  }
  else {
    filtred = travels.filter((t) => new Date(t.endDate) < new Date())
  }

  return (
    <DashboardLayout>
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
        <h2 className="text-2xl font-semibold">My Travels</h2>
        <TextField select size="small" className="flex items-center gap-3" value={filter} onChange={e => setFilter(e.target.value)}>
          <MenuItem value="ALL">All</MenuItem>
          <MenuItem value="Upcoming">Upcomming</MenuItem>
          <MenuItem value="Ongoing">Ongoing</MenuItem>
          <MenuItem value="Complated">Complated</MenuItem>
        </TextField>
        {user.role === "ROLE_Manager" &&
          (<Button variant="contained" color="primary" onClick={() => navigate(`/travel/team`)}>View Team Travels</Button>)}
      </div>


      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-3">Destination</th>
              <th>Start</th>
              <th>End</th>
              <th>AssignedBy</th>
              <th>Assigned Date</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {filtred.length === 0 ? (
              <tr><td colSpan="8" className="text-center p-4">No Travel Found</td></tr>
            ) : (
              filtred.map(t => (
                <tr key={t.assignedId} className="border-t text-center">
                  <td className="p-3">{t.destination}</td>
                  <td>{t.startDate}</td>
                  <td>{t.endDate}</td>
                  <td>{t.assignedBy}</td>
                  <td>{t.assignedDate}{filtred.length}</td>
                  <td>
                    <button
                      className="text-blue-600"
                      onClick={() =>
                        navigate(`/travel/my/${t.assignedId}`)
                      }>
                      View Details
                    </button>
                  </td>
                </tr>
              )))}
          </tbody>
        </table>
      </div>
    </DashboardLayout>
  );
};

export default EmployeeTravelList;