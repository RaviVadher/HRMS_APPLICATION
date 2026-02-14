import { useEffect, useState } from "react";
import { Button } from "@mui/material";
import {  useNavigate, useParams} from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import { getAllDocument } from "../travelAPI";

const SubmittedDocumentList = () => {
 const {id} = useParams();
  const navigate = useNavigate();
  const [document, setDocuments] = useState([]);

  
  useEffect(() => {
    fetchDocument();
  }, []);

  const fetchDocument = async () => {
    const res = await getAllDocument(id);
    setDocuments(res || []);
    console.log(res);
  };

  return (
    <DashboardLayout>
                <h2 className="text-2xl font-semibold">Submited Documents List</h2>

      <div className="flex justify-between mb-6">
        <table className="w-full">
        <thead className="bg-gray-100">
          <tr>
            <th>DocumentID</th>
            <th className="p-3">Document Name</th>
            <th>Submited Date</th>
          </tr>
        </thead>

        <tbody>
          {document.map((t) => (
            
            <tr key={t.documentId} className="border-t text-center">
              <td>{t.documentId}</td>
              <td className="p-3">{t.documentName}</td>
              <td>{t.submittedDate}</td>
              <td>
                <button
                  className="text-blue-600"
                  onClick={() => navigate(`/dowload/${t.documentId}`)}
                >
                  View
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>
    </DashboardLayout>
  );
};



export default SubmittedDocumentList;