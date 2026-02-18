import { Modal,Box,TextField,Button, CircularProgress} from "@mui/material";
import { useState } from "react";
import { shareJob } from "../jobAPI";

export default function ShareForm({open,close,job}){

  const [email,setEmail]=useState(""); 
  const [loading,setLoading] = useState(false);

  const submit=async()=>{
    setLoading(true);
    await shareJob(job.jobId,email);
    setLoading(false);
    close();

  };


  return(
  <Modal open={open} onClose={close}>
    <Box className="bg-white p-6 rounded w-[400px] mx-auto mt-40 space-y-4">

      <h2>Share Job</h2>
      <TextField fullWidth label="Recipient Email"
        onChange={e=>setEmail(e.target.value)}/>
      <Button fullWidth variant="contained" onClick={submit}>
            {loading ? <CircularProgress color="success" /> : "Send"}
      </Button>
    </Box>
  </Modal>
  );
}