import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTravelById } from "../travelAPI";
import { getAssignById } from "../travelAPI";
import { Button } from "@mui/material";
import DashboardLayout from "../../layout/DashboardLayout";
import { useAuth } from "../../context/AuthContext";
import AssignEmployeeModal from "../componants/AssignEmployeeModel";
import TravelDocumentUpload from "../componants/TravelDocumentUpload";
import ExpenseList from "../componants/ExpenseList";
import { getMyExpenses } from "../travelAPI";

const TravelDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [travel, setTravel] = useState(null);
  const [openAssign, setOpenAssign] = useState(false);
  const [employees,setEmployee] = useState([]);
  const [expenses,setExpenses] = useState({});

  useEffect(() => {
    fetchTravel();
    fetchAssignEmployee();
  }, [openAssign]);

  const fetchTravel = async () => {
    const res = await getTravelById(id);
    setTravel(res);
    console.log(res);
  };


  const fetchExpenses = async (assignedId) => {
    const res = await getMyExpenses(assignedId);
    setExpenses(prev => ({ 
      ...prev,
      [assignedId]:res
    }));
    console.log(expenses)
  };


  const  fetchAssignEmployee = async () => {
    const res = await getAssignById(id);
    setEmployee(res);
    console.log(res);
    res.forEach(emp => {
    fetchExpenses(emp.assignedId);
    });
  };

  if (!travel) return null;
  const isTravelStarted = new Date(travel.startDate) <= new Date();

  return (
    <DashboardLayout>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold">Travel Details</h2>
        {!isTravelStarted && (
          <Button
            variant="contained"
            onClick={() => setOpenAssign(true)}>
            + Assign Travel
          </Button>
        )}
      </div>

      <div className="bg-white p-6 rounded shadow mb-6">
        <p><b>Destination:</b> {travel.destination}</p>
        <p><b>Strat Dates:</b> {travel.startDate}  </p>
        <p><b>End Dates:</b> {travel.endDate}  </p>
        <p><b>Description:</b> {travel.description}</p>
      </div>

      <div className="bg-white p-6 rounded shadow mb-6">
        <h3 className="text-lg font-semibold mb-4">
          Assigned Employees
        </h3>
        {employees?.length === 0 && (
          <p className="text-gray-500">No employees assigned.</p>
        )}

        <ul className="space-y-10">
          {employees.map((emp) => (
            <li key={emp.assignedUserId}>
              <div className="flex justify-evenly bg-gray-100 p-4">
              <span><b>Name:</b> {emp.assignedUserName} </span>
              <span className="ml-5"><b>Assigned Date:</b> {emp.assignedDate}</span>
              <span>
                 <button className="px-4 py-2 bg-blue-100 text-black rounded even:bg-green-500 hover:opacity-80" onClick={()=>navigate(`/documents/${emp.assignedId}`)}>
                  View Documents
                 </button>
              </span>
              </div>
              {!isTravelStarted && (<TravelDocumentUpload assignedId={emp.assignedId}/> )}
              {isTravelStarted && (     
                 <div className="bg-white p-6 rounded shadow-md-6">
                 <ExpenseList expenses={expenses[emp.assignedId]|| []} refresh ={()=>fetchExpenses(emp.assignedId)}/>
                </div> )}
              <hr />
            </li>    
          ))}
        </ul>
      </div>


      <AssignEmployeeModal
        open={openAssign}
        handleClose={() => setOpenAssign(false)}
        travelId={id}
        refresh={fetchTravel}/>
       </DashboardLayout>
  );
};

export default TravelDetails;