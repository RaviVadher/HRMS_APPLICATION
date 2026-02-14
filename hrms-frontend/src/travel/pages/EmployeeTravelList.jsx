import { useEffect,useState } from "react";
import DashboardLayout from "../../layout/DashboardLayout";
import {getMyTravels} from "../travelAPI"
import { useNavigate } from "react-router-dom";

const EmployeeTravelList = () => {
  const [travels, setTravels] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchTravels();
  }, []);

  const fetchTravels = async () => {
    const res = await getMyTravels();
    setTravels(res || []);
    console.log(travels)
  };

  return (
    <DashboardLayout>
      <h2 className="text-2xl font-semibold mb-6">My Travels</h2>

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
            {travels.map(t => (
              <tr key={t.assignedId} className="border-t text-center">
                <td className="p-3">{t.destination}</td>
                <td>{t.startDate}</td>
                <td>{t.endDate}</td>
                <td>{t.assignedBy}</td>
                <td>{t.assignedDate}</td>

                <td>
                  <button
                    className="text-blue-600"
                    onClick={() =>
                      navigate(`/travel/my/${t.assignedId}`)
                    }
                  >
                    View Details
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

export default EmployeeTravelList;