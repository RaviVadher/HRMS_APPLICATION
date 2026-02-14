import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import ExpenseForm from "../componants/ExpenseForm";
import {
  getMyTravelsById,
  getMyExpenses,
} from "../travelAPI";
import ExpenseList from "../componants/ExpenseList";
import TravelDocumentUpload from "../componants/TravelDocumentUpload";

const EmployeeTravelDetails = () => {
  const { id } = useParams();

  const [travel, setTravel] = useState(null);
  const [expenses, setExpenses] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    const t = await getMyTravelsById (id);
    const e = await getMyExpenses(id);
    setTravel(t);
     setExpenses(e || []);
     console.log(t)
  };

  if (!travel) return null;

  const started =
    new Date(travel.startDate) <= new Date();

  const total = expenses.reduce(
    (sum, e) => sum + e.amount,
    0
  );

  return (
    <DashboardLayout>
      <h2 className="text-2xl mb-6">Travel Details</h2>

      <div className="bg-white p-6 rounded shadow mb-6">
        <p><b>Destination:</b> {travel.destination}</p>
        <p><b>Dates:</b> {travel.startDate} → {travel.endDate}</p>
        <p><b>Description:</b> {travel.description}</p>
      </div>

      {!started && (
        <TravelDocumentUpload assignedUserId={id} />
      )}

      {started && (
        <>
          <ExpenseForm assignedUserId={id} refresh={fetchData} travelId={travel.travelId}/>
          <ExpenseList expenses={expenses} total={total} />
        </>
      )}
    </DashboardLayout>
  );
};

export default EmployeeTravelDetails;