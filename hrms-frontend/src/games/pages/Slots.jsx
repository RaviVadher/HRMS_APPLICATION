import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { CircularProgress } from "@mui/material";
import { getSlot, getPlayers, getMax } from "../gameAPI";
import DashboardLayout from "../../layout/DashboardLayout";
import BookSlot from "../componants/BookSlot";
import { useAuth } from "../../context/AuthContext";
export default function Slots() {

  const { id } = useParams();
  const [slots, setSlots] = useState([]);
  const [players, setPlayers] = useState([]);
  const [bookOpen, setBookOpen] = useState(false);
  const [slotId, setSlotId] = useState(null);
  const [maxPlayer, setMaxPlayer] = useState(null);
  const { user, loading } = useAuth();


  useEffect(() => {
    load()
  }, [id]);

  const selectSlot = (slot) => {
    setSlotId(slot.slotId);
    setBookOpen(true);
  };

  const load = async () => {
    const resS = await getSlot(id);
    const resP = await getPlayers(id);
    const resM = await getMax(id);
    setSlots(resS);
    setPlayers(resP);
    setMaxPlayer(resM)
  };

  if (loading) return <DashboardLayout> <CircularProgress/></DashboardLayout>
  return (
    <DashboardLayout>
      <div className="p-6">
        <h1 className="text-xl font-semibold mb-6 text-center">Book Slots for your Team</h1>
        <hr />

        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {slots.map((slot) => (
            <div
              key={slot.slotId}
              onClick={() => selectSlot(slot)}
              className={`border rounded p-4 text-center cursor-pointer ${slot.status === "Available" ? "bg-green-100" : "bg-red-100 cursor-not-allowed"
                }`}
            >
              {slot.startTime} - {slot.endTime} <br />
              <span>{slot.status}</span><br />
              BookedCount:<span>{slot.bookedCount}</span>

            </div>
          ))}
        </div>

        <BookSlot reload={load} open={bookOpen} userId={user.id} close={() => setBookOpen(false)} players={players} maxPlayer={maxPlayer} slotId={slotId} />
      </div>
    </DashboardLayout>
  );
}