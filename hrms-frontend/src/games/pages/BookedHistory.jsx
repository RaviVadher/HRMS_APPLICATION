import { useNavigate, useParams } from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import { useEffect, useState } from "react";
import { getHistory, cancelBooking } from "../gameAPI";
import { CircularProgress } from "@mui/material";

const BookedHistory = () => {
  const [books, setBooks] = useState([]);
  const { gameId, userId } = useParams();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    load()
  }, [gameId, userId])

  const load = async () => {
    setLoading(true);
    const data = await getHistory(gameId, userId);
    setBooks(data);
    setLoading(false);

  }

  const cancalBooking = async (bookedId) => {
    await cancelBooking(bookedId);
    load();
  }

  if (loading) {
    return (<DashboardLayout> <CircularProgress/> </DashboardLayout>)
  }

  return (
    <DashboardLayout>
      <h2 className="text-center text-3xl">Game History</h2>
      <div className="bg-white shadow rounded-lg">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-3">SlotId</th>
              <th className="p-3">BookedId</th>
              <th>BookedAt</th>
              <th>BookedBy</th>
              <th>StartTime</th>
              <th>EndTime</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {books.length===0?(
               <h1 className="text-center"> No Booking Found</h1>
            ):( 
            books.map((t) => (
              <tr key={t.bookingId} className="border-t text-center">
                <td className="p-3">{t.slotId}</td>
                <td className="p-3">{t.bookingId}</td>
                <td>{t.bookingTime}</td>
                <td>{t.bookedBy}</td>
                <td>{t.startTime}</td>
                <td>{t.endTime}</td>
                <td>{t.status}</td>
                <td>
                  <button
                    className="text-blue-600"
                    onClick={() => cancalBooking(t.bookingId)}>
                    Cancel
                  </button>
                </td>
              </tr>
            )))}
          </tbody>
        </table>
      </div>
    </DashboardLayout>
  );
};

export default BookedHistory;