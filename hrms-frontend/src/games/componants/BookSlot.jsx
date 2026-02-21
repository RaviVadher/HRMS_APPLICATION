import { Modal,Box,MenuItem,Button,Select,CircularProgress} from "@mui/material";
import { useEffect, useState } from "react";
import { bookSlot } from "../gameAPI";
import toast from "react-hot-toast";

export default function BookSlot({reload,open,close,userId,players,maxPlayer,slotId}){

   const [playerIds, setPlayerIds] = useState([userId])
   const [loading,setLoading] = useState(false);

    useEffect(()=>{
        if(open)
        {
            setPlayerIds([userId])
        }
    },[open,userId]);

    const handlePlayerChange = (e) => {
     if(!e.target.value.includes(userId)) return;
    setPlayerIds( e.target.value );
  };


  const submit=async()=>{
    
    if (playerIds.length!== maxPlayer) return;
    try{
          setLoading(true)
          await bookSlot({slotId, teamUserId:playerIds});
           setPlayerIds([userId]);
           reload();
          close(); 
    }finally{
        setLoading(false);
    }
    
  };


  return(
  <Modal open={open} onClose={close}>
    <Box className="bg-white p-6 rounded w-[400px] mx-auto mt-40 space-y-4">

      <h2>Book Slot</h2>

       <div className="mt-6 max-w-sm space-y-3">
          <div className="font-medium">
            Select {maxPlayer} Players
          </div>


          <Select multiple  value={playerIds} fullWidth  label="Select Reviewers" onChange={handlePlayerChange}>
                       {players.map((r) => (
                        <MenuItem key={r.userId} value={r.userId}>
                          {r.name}
                        </MenuItem>
                         ))}
                      </Select>

          <Button
            variant="contained"
            fullWidth
            disabled={playerIds.length !== maxPlayer}
            onClick={submit}>
            {loading ? <CircularProgress color="success"   size={24} /> : "Submit"}   
          </Button>
        </div>
    </Box>
  </Modal>
  );
}

