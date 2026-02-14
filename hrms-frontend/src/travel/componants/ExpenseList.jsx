import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { updateExpenseStatus } from "../travelAPI";


const ExpenseList = ({ expenses,refresh }) => {
    const { user,role } = useAuth();
    const [hrRemark,setHrRemark] = useState({});
    const [status,setStatus] = useState({})

    const handleUpdate = async(id) =>{
       await updateExpenseStatus(id,{
        status:status[id],
        hrRemark:hrRemark[id],
       })
      console.log(status)

       refresh();
    };

    const total = expenses.reduce(
      (sum,e) => sum +e.amount,0
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
          </tr>
        </thead>

        <tbody>
          {expenses.map(e => (
            <tr key={e.expenseId} className="border-t text-center">
              <td className="p-2">{e.amount}</td>
              <td>{e.category}</td>
             <td>{e.expenseDate}</td>
              <td>{e.status}</td>
              {role ==="ROLE_Hr" && (
                <td className="space-x-2" >
                <select className="border p-1" onChange ={(ev)=> setStatus({...status,[e.expenseId]:ev.target.value,}) } >
                 <option>Select</option>
                 <option value={"Approved"}>Approved</option>
                 <option value={"Rejected"}>Rejected</option>
                 </select>

                 <input placeholder="remark" className="border p-1" onChange ={(ev)=> setHrRemark({...hrRemark,[e.expenseId]:ev.target.value,}) }/>

                 <button onClick={() => handleUpdate(e.expenseId)}>
                  Save
                 </button>
              </td>
              )}

              {role ==="ROLE_Employee" && (
                   <td>{e.hrRemark}</td>
              )}

              <td>{e.reviewedDate}</td>

              
            </tr>
          ))}
        </tbody>
      </table>

      <div className="text-right font-semibold">
        Total: ₹ {total}
      </div>
    </div>
  );
};

export default ExpenseList;
