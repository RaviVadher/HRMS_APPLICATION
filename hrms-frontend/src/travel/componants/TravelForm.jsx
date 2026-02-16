import { useState } from "react";
import { TextField, Button } from "@mui/material";
import { createTravel } from "../travelAPI";
import { useNavigate } from "react-router-dom";

const TravelForm = () => {
  const navigate = useNavigate();
  const [err,setErr] = useState({});

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
    if(!velidate()) return;
    await createTravel(form);
    navigate("/travel");
  };


  const velidate = () =>{

    let errors = {}

    if(!form.title) errors.title="Title required";
    if(!form.origin) errors.origin="Origin required";
    if(!form.destination) errors.destination="destination required";
    if(!form.startDate) errors.startDate="startDate required";
    if(!form.endDate) errors.endDate="EndDate required";

    setErr(errors);
    return Object.keys(errors).length===0;

  }
  return (
    <div className="bg-white p-6 rounded shadow max-w-xl flex flex-col gap-4">
         <TextField
        label="Title"
        name="title"
        value={form.title}
        onChange={handleChange}
        error={!!err.title}
        helperText={err.title}
      /> 
      <TextField
        label="Origin"
        name="origin"
        onChange={handleChange}
         error={!!err.origin}
        helperText={err.origin}
      />

      <TextField
        label="Destination"
        name="destination"
        onChange={handleChange}
         error={!!err.destination}
        helperText={err.destination}
      />

      <TextField
        type="date"
        name="startDate"
        onChange={handleChange}
         error={!!err.startDate}
        helperText={err.startDate}
      />

      <TextField
        type="date"
        name="endDate"
        onChange={handleChange}
         error={!!err.endDate}
        helperText={err.endDate}
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