import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { updateExpenseStatus } from "../travelAPI";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { TextField,MenuItem } from "@mui/material";


const ExpenseList = ({ expenses, refresh }) => {
  const { user } = useAuth();
  const [hrRemark, setHrRemark] = useState({});
  const [status, setStatus] = useState({});
  const [filter, setFilter] = useState("ALL");
  const navigate = useNavigate();
  const handleUpdate = async (id) => {

    if(status[id]==="Approved" || status[id]==="Rejected"){
        await updateExpenseStatus(id, {
      status: status[id],
      hrRemark: hrRemark[id],
    })
    console.log(status)
    refresh();
    }
    else{
      toast.error("please select status");
    }
   
  };


  const total = expenses.reduce(
    (sum, e) => sum + e.amount, 0
  )

   let filtred = null;
  if (filter === "ALL") {
    filtred = expenses;
  }
  else if (filter === "Submitted") {
    filtred = expenses.filter((t) => t.status ==="Submitted");
  }
  else if (filter === "Rejected") {
    filtred = expenses.filter((t) => t.status ==="Rejected")
  }
  else {
        filtred = expenses.filter((t) => t.status ==="Approved")
  }

  return (
    <div className="bg-white p-6 rounded shadow">
      <div className="flex flex-row justify-between mb-1">
      <h3 className="font-semibold mb-4"> <b>Expenses</b> </h3>
      {user.role==="ROLE_Hr" &&
          <TextField select size="small" value={filter} onChange={e => setFilter(e.target.value)}>
          <MenuItem value="ALL">All</MenuItem>
          <MenuItem value="Submitted">Submitted</MenuItem>
          <MenuItem value="Rejected">Rejected</MenuItem>
          <MenuItem value="Approved">Approved</MenuItem>
        </TextField>
      }
      </div>
      <table className="w-full mb-4">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2">Amount</th>
            <th>Category</th>
            <th>ExpenseDate</th>
            <th>Status</th>
            <th>Hr Remark</th>
            <th>Review Date</th>
            <th>View Proofs</th>
          </tr>
        </thead>

        <tbody>
          {filtred.length === 0 ? (
            <tr><td colSpan="8" className="text-center p-4">NO EXPENSE FOUND</td></tr>
          ) : (
            filtred.map(e => (
              <tr key={e.expenseId} className="border-t text-center">
                <td className="p-2">{e.amount}</td>
                <td>{e.category}</td>
                <td>{e.expenseDate}</td>
                {user.role === "ROLE_Hr" && !e.reviewedDate ? (
                  <>
                    <td className="space-x-2" >
                      <select className="border p-1" onChange={(ev) => setStatus({ ...status, [e.expenseId]: ev.target.value, })} >
                        <option>{e.status}</option>
                        <option value={"Approved"}>Approved</option>
                        <option value={"Rejected"}>Rejected</option>
                      </select> </td>
                    <td className="space-x-2" >
                      <input placeholder="remark" className="border p-1" onChange={(ev) => setHrRemark({ ...hrRemark, [e.expenseId]: ev.target.value, })} />
                      <button onClick={() => handleUpdate(e.expenseId)}>
                        Save
                      </button>

                    </td>
                    <td></td>
                  </>
                ) : (<><td>{e.status}</td>
                  <td>{e.hrRemark}</td>
                  <td>{e.reviewedDate}</td></>)}
                <td><button onClick={() => navigate(`/expenseProof/${e.expenseId}`)}>VIEW</button></td>
              </tr>
            ))
          )}

        </tbody>
      </table>

      <div className="text-right font-semibold">
        Total: ₹ {total}
      </div>
    </div>
  );
};

export default ExpenseList;
