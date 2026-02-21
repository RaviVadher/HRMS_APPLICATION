import { Modal,Box,TextField,Button, CircularProgress} from "@mui/material";
import { useState } from "react";
import { shareJob } from "../jobAPI";
import toast from "react-hot-toast";

export default function ShareForm({open,close,job,getShare}){

  const [email,setEmail]=useState(""); 
  const [loading,setLoading] = useState(false);

  const submit=async()=>{
    setLoading(true);
    if(!/\S+@\S+\.\S+/.test(email)) return toast.error("email is not velid formate");
    await shareJob(job.jobId,email);
    toast.success("Job shared Successfully")
    await getShare(job.jobId)
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