import { useEffect,useState } from "react";
import DashboardLayout from "../../layout/DashboardLayout";
import {getTeamTravels} from "../travelAPI"
import { useNavigate } from "react-router-dom";
import { Button } from "@mui/material";
import { useAuth } from "../../context/AuthContext";

const TeamTravel = () => {

  const [travels, setTravels] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchTeamTravels();
  }, []);

  const fetchTeamTravels = async () => {
    const res = await getTeamTravels();
    setTravels(res || []);
    console.log(res)
  };

  return (
    <DashboardLayout>
      <h2 className="text-2xl font-semibold mb-6">My Travels</h2>

      <div className="bg-white rounded shadow">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th>TraveId</th>
              <th>Name</th>
              <th className="p-3">Destination</th>
              <th>Start</th>
              <th>End</th>
              <th>AssignedBy</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {travels.map(t => (
              <tr key={t.assignedId} className="border-t text-center">
             <td>{t.travelId}</td>
              <td>{t.assignedUserName}</td>
                <td className="p-3">{t.destination}</td>
                <td>{t.startDate}</td>
                <td>{t.endDate}</td>
                <td>{t.assignedBy}</td>
                <td>
                  <button className="px-4 py-2 bg-blue-100 text-black rounded even:bg-green-500 hover:opacity-80" onClick={()=>navigate(`/documents/${t.assignedId}`)}>
                  View Documents
                 </button> 
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </DashboardLayout>
  );
};

export default TeamTravel;