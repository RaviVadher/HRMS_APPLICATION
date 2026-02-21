import { useEffect, useState } from "react";
import {
  Modal,
  Box,
  Button,CircularProgress
} from "@mui/material";
import { getEmployees, assignEmployeeToTravel } from "../travelAPI";
import toast from "react-hot-toast";

const AssignEmployeeModal = ({ open, handleClose, travelId, refresh }) => {

  const [employees, setEmployees] = useState([]);
  const [selected, setSelected] = useState("");
  const [load,setLoad] = useState(false);

  useEffect(() => {
    if (open) fetchEmployees();
  }, [open]);

  const fetchEmployees = async () => {
    const res = await getEmployees();
    setEmployees(res || []);
  };

  const handleAssign = async () => {
    if (!selected) return;
    try{
    setLoad(true);
    await assignEmployeeToTravel(travelId, selected);
    refresh(travelId);
    setSelected("");
    toast.success("Assigned travel successfully");
    }
    finally{
           setLoad(false);
           handleClose();
    }
  };
  
  return (
    <Modal open={open} onClose={handleClose}>
      <Box className="bg-white p-6 rounded shadow w-96 mx-auto mt-40">

        <h3 className="text-lg font-semibold mb-4">
          Assign Employee
        </h3>

        <select
          className="w-full border p-2 rounded mb-4"
          value={selected}
          onChange={(e) => setSelected(e.target.value)}>
          <option value="">Select Employee</option>
          {employees.map(emp => (
            <option key={emp.userId} value={emp.userId}>
              {emp.username} - {emp.rolename}
            </option>))}
        </select>

        <div className="flex justify-end gap-2">
          <Button onClick={handleClose}>Cancel</Button>
          <Button
            variant="contained"
            onClick={handleAssign}
            disabled={!selected}>
               {load ? <CircularProgress color="success" size={24} /> : "Assign"}     
          </Button>
        </div>
      </Box>
    </Modal>
  );
};

export default AssignEmployeeModal;