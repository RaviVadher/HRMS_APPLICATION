import { useEffect, useState } from "react";
import { Button } from "@mui/material";
import {  useNavigate, useParams} from "react-router-dom";
import DashboardLayout from "../../layout/DashboardLayout";
import { getAllDocument,getDocUrl} from "../travelAPI";

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

  const openDocument = async(doc_id) =>{  
    
        const res = await getDocUrl(doc_id);
        console.log(res.headers["content-type"])
        const blob = new Blob([res.data], {type : res.headers["content-type"]});
        const url = window.URL.createObjectURL(blob);
        window.open(url);
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
            <th>SubmitedBy</th>
            <th>View Document</th>
          </tr>
        </thead>

        <tbody>
          {document.map((t) => (
            
            <tr key={t.documentId} className="border-t text-center">
              <td>{t.documentId}</td>
              <td className="p-3">{t.documentName}</td>
              <td>{t.submittedDate}</td>
              <td>{t.submittedBy}</td>
              <td>
                <button
                  className="text-blue-600"
                  onClick={() => openDocument(t.documentId)}
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