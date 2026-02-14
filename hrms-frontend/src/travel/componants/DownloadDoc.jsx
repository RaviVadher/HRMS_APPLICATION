import { useEffect, useState } from "react";
import { getAllTravels } from "../travelAPI";
import DashboardLayout from "../../layout/DashboardLayout";
import { useParams } from "react-router-dom";
import { getDocument } from "../travelAPI";

const DownloadDoc = () => {
  const [file, setFile] = useState();
  const {id} = useParams();

  useEffect(() => {
    fetchDocument();
  }, []);

  const fetchDocument = async () => {
    const res = await getDocument(id);
    console.log(res);
    setFile(res || []);
  };

  return (
    <DashboardLayout>
       {file}
    </DashboardLayout>
  );
};

export default DownloadDoc;