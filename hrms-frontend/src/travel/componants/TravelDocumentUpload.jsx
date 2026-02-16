import { useState, useRef } from "react";
import { Button } from "@mui/material";
import { uploadTravelDocument } from "../travelAPI";

const TravelDocumentUpload = ({ assignedId }) => {
  const [file, setFile] = useState(null);
  const [fileType, setFiletype] = useState(null);
  const [status, setStatus] = useState("uploading")
  const [docname,setDocname] = useState("");
  const fileInputRef = useRef(null);


  const handleUpload = async () => {
    if (!file) return;
    try {
      console.log(assignedId);
      const res = await uploadTravelDocument(assignedId, fileType, docname, file);
      setStatus("success")
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
      setFiletype("");
      console.log(res);
    } catch {
      setStatus("fail")
    }
  };



  return (
    <>
      <div className="bg-white p-4 rounded shadow flex justify-evenly ">
        <h4 className="mb-3 mt-2 font-semibold">Upload Document :</h4>

        <input
          type="file"
          ref={fileInputRef}
          onChange={(e) => setFile(e.target.files[0])}
          className="px-4 py-2 border border-gray-300 rounded-lg shadow-sm 
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500
             transition duration-200 ease-in-out"
        />
         <input
          type="string"
           placeholder="File Name"
          onChange={(e) => setDocname(e.target.value)}
          className="px-2 py-1 border border-gray-300 rounded-lg shadow-sm 
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500
             transition duration-200 ease-in-out"
        />

        <input
          type="string"
          placeholder="File type"
          onChange={(e) => setFiletype(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg shadow-sm 
             focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500
             transition duration-200 ease-in-out"
        />

        <Button variant="contained" onClick={handleUpload}>
          Upload
        </Button> <br />

      </div>
      {status === 'success' && <p style={{ color: 'green' }}>Uploaded SUCCESS</p>}
      {status === 'fail' && <p style={{ color: 'red' }}>Upload failed!</p>}
    </>
  );
};

export default TravelDocumentUpload;