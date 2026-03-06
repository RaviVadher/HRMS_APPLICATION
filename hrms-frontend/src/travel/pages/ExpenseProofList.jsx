import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getExpeseProofs, getProofUrl } from "../travelAPI";
import { Button } from "@mui/material";
import AddProof from "../componants/AddProof";
import { useAuth } from "../../context/AuthContext";
import DashboardLayout from "../../layout/DashboardLayout";
const ExpenseProofList = () => {
  const navigate = useNavigate();
  const [proofs, setProofs] = useState([]);
  const [openAdd,setOpenAdd] = useState(false);
  const { id } = useParams();
  const {user,loading} = useAuth();

  useEffect(() => {
    fetchExpenseProof();
  }, [openAdd]);

  const fetchExpenseProof = async () => {
    const res = await getExpeseProofs(id);
    setProofs(res || []);
    console.log(res);
  };

  const openProof = async (proof_id) => {
    const res = await getProofUrl(proof_id);
    console.log(res.data);
    console.log(res.headers["content-type"])
    const blob = new Blob([res.data], { type: res.headers["content-type"] });
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  };
   
  if(loading) return;

  return (
    <DashboardLayout>
    <div className="bg-white shadow rounded-lg">
      <table className="w-full">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3">ProofId</th>
            <th>URL</th>
            <th>Upload Date</th>
            {user.role ==="ROLE_Employee" &&
             <th> <Button variant="outlined" onClick={() => setOpenAdd(true)}>+Add Proof</Button></th>
             }
          </tr>
        </thead>

        <tbody>
          {proofs.length === 0 ? (
            <tr><td colSpan="8" className="text-center p-4">NO Proof EXPENSE FOUND</td></tr>
          ) : (
            proofs.map((t) => (
              <tr key={t.proofId} className="border-t text-center">
                <td className="p-3">{t.proofId}</td>
                <td>
                  <button
                    className="text-blue-600"
                    onClick={() => openProof(t.proofId)} >
                    View
                  </button>
                </td>
                <td>{t.uploadDate}</td>
              </tr>
            ))
          )}

        </tbody>
      </table>
         <AddProof open={openAdd} close={() => setOpenAdd(false)} expenseId ={id} />
      
    </div>
    </DashboardLayout>
  );
};

export default ExpenseProofList;