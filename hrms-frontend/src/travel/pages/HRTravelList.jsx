import { useEffect, useState } from "react";
import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getAllTravels } from "../travelAPI";
import DashboardLayout from "../../layout/DashboardLayout";
import TravelTable from "../componants/TravelTable";

const HRTravelList = () => {
  const navigate = useNavigate();
  const [travels, setTravels] = useState([]);

  useEffect(() => {
    fetchTravels();
  }, []);

  const fetchTravels = async () => {
    const res = await getAllTravels();
    setTravels(res || []);
  };

  return (
    <DashboardLayout>
      <div className="flex justify-between mb-6">
        <h2 className="text-2xl font-semibold">Travel Plans</h2>
        <Button
          variant="contained"
          onClick={() => navigate("/travel/create")}
        >
          + Create Travel
        </Button>
      </div>
      <TravelTable travels={travels} />
    </DashboardLayout>
  );
};

export default HRTravelList;