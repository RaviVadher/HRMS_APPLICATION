import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { updateExpenseStatus } from "../travelAPI";
import { useNavigate } from "react-router-dom";


const ExpenseList = ({ expenses, refresh }) => {
  const { user } = useAuth();
  const [hrRemark, setHrRemark] = useState({});
  const [status, setStatus] = useState({})
  const navigate = useNavigate();
  const handleUpdate = async (id) => {
    await updateExpenseStatus(id, {
      status: status[id],
      hrRemark: hrRemark[id],
    })
    console.log(status)
    refresh();
  };


  const total = expenses.reduce(
    (sum, e) => sum + e.amount, 0
  )

  return (
    <div className="bg-white p-6 rounded shadow">
      <h3 className="font-semibold mb-4">
        Expenses
      </h3>

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
          {expenses.length === 0 ? (
            <tr><td colSpan="8" className="text-center p-4">NO EXPENSE FOUND</td></tr>
          ) : (
            expenses.map(e => (
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
