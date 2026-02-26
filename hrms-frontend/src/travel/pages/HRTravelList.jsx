import { useEffect, useState } from "react";
import { Button, TextField, MenuItem } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { getAllTravels } from "../travelAPI";
import DashboardLayout from "../../layout/DashboardLayout";
import TravelTable from "../componants/TravelTable";

const HRTravelList = () => {
  const navigate = useNavigate();
  const [travels, setTravels] = useState([]);
  const [filter, setFilter] = useState("ALL");


  useEffect(() => {
    fetchTravels();
  }, []);

  const fetchTravels = async () => {
    const res = await getAllTravels();
    setTravels(res || []);
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
      <div className="flex justify-between mb-6">
        <h2 className="text-2xl font-semibold">Travel Plans</h2>
        <TextField select size="small" value={filter} onChange={e => setFilter(e.target.value)}>
          <MenuItem value="ALL">All</MenuItem>
          <MenuItem value="Upcoming">Upcomming</MenuItem>
          <MenuItem value="Ongoing">Ongoing</MenuItem>
          <MenuItem value="Complated">Complated</MenuItem>
        </TextField>
        <Button
          variant="contained"
          onClick={() => navigate("/travel/create")}
        >
          + Create Travel
        </Button>
      </div>
      <TravelTable travels={filtred} />
    </DashboardLayout>
  );
};

export default HRTravelList;