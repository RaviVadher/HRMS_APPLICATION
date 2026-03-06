import { Modal, Box, TextField, Button, CircularProgress } from "@mui/material";
import { useState } from "react";
import toast from "react-hot-toast";
import { addProof } from "../travelAPI";

export default function AddProof({ open, close,  expenseId }) {

  const [loading, setLoading] = useState(false);
  const[file, setFile] = useState();

  const submit = async () => {
    try {
      setLoading(true);
      const form = new FormData();
      form.append("file",file);
      await addProof( expenseId,form);
      toast.success("Proof Added Successfully")
    } finally {
      setLoading(false);
      close();
    }

  };

  return (
    <Modal open={open} onClose={close}>
      <Box className="bg-white p-6 rounded w-[400px] mx-auto mt-40 space-y-4">

        <h2>Add Expense Proof</h2>
        <input type="file" lable="add proof" onChange={e => setFile(e.target.files[0])} />

        <Button fullWidth variant="contained" onClick={submit}>
        {loading ? <CircularProgress color="success" /> : "Send"}
        </Button>
      </Box>
    </Modal>
  );
}