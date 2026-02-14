import { useState } from "react";
import { TextField, Button } from "@mui/material";
import { createTravel } from "../travelAPI";
import { useNavigate } from "react-router-dom";

const TravelForm = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title:"",
    origin:"",
    destination: "",
    startDate: "",
    endDate: "",
    description: "",
  });

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async () => {
    await createTravel(form);
    navigate("/travel");
  };

  return (
    <div className="bg-white p-6 rounded shadow max-w-xl flex flex-col gap-4">
         <TextField
        label="Title"
        name="title"
        onChange={handleChange}
      /> 
      <TextField
        label="Origin"
        name="origin"
        onChange={handleChange}
      />

      <TextField
        label="Destination"
        name="destination"
        onChange={handleChange}
      />

      <TextField
        type="date"
        name="startDate"
        onChange={handleChange}
      />

      <TextField
        type="date"
        name="endDate"
        onChange={handleChange}
      />

      <TextField
        label="Description"
        name="description"
        multiline
        rows={3}
        onChange={handleChange}
      />

      

      <Button variant="contained" onClick={handleSubmit}>
        Save Travel
      </Button>
    </div>
  );
};

export default TravelForm;