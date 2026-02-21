import { Modal, Box, TextField, Button,CircularProgress } from "@mui/material";
import { useState } from "react";
import { referFriend } from "../jobAPI";
import { blue } from "@mui/material/colors";
import toast from "react-hot-toast";

export default function ReferForm({ open, close, job,getRefer }) {

  const [form, setForm] = useState({ friendName: "", friendEmail: "", note: "" });
  const [file, setFile] = useState(null);
  const [loading,setLoading] = useState(false);

  const submit = async () => {
    

    if(!form.friendName.trim()) return toast.error("name is required");
    if(!form.friendEmail.trim()) return toast.error("email is required");
    if(!/\S+@\S+\.\S+/.test(form.friendEmail)) return toast.error("email is not velid formate");
    if(!file) return toast.error("CV is required")
   try{
    setLoading(true);
    const data = new FormData();
    data.append("file", file);
    data.append("friendName", form.friendName);
    data.append("friendEmail", form.friendEmail);
    data.append("note", form.note);
    data.append("jobId",job.jobId);
    await referFriend(data);
    toast.success("Job Refered Successfully")
    await getRefer(job.jobId)
    close();
   }
   finally{
       setLoading(false);
   }
  };

  return (
    <Modal open={open} onClose={close}>
      <Box className="bg-white p-6 rounded w-[400px] mx-auto mt-40 space-y-4">

        <h2>Refer Candidate</h2>

        <TextField label="Name" fullWidth
          onChange={e => setForm({ ...form, friendName: e.target.value })} />

        <TextField label="Email" fullWidth
          onChange={e => setForm({ ...form, friendEmail: e.target.value })} />

        <TextField label="Note" fullWidth
          onChange={e => setForm({ ...form, note: e.target.value })} />

        <input type="file" accept=".pdf" onChange={e => setFile(e.target.files[0])} />

        <Button fullWidth variant="contained" onClick={submit}>
            {loading ? <CircularProgress color="success"   size={24} /> : "Submit"}   
        </Button>
      </Box>
    </Modal>
  );
}