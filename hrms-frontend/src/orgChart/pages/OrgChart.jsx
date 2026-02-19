import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import DashboardLayout from "../../layout/DashboardLayout";
import { getOrgChart } from "../orgAPI";
import ManagerChain from "../component/ManagerChain";
import EmployeeCard from "../component/EmployeeCard";
import DirectReports from "../component/DirectReports";
import SearchEmployee from "../component/SearchEmployee";

const OrgChart = () => {
  const { user } = useAuth();
  const [data, setData] = useState(null);

  useEffect(() => {
    if (user?.id) load(user.id);
    console.log(user);
  }, [user]);

  const load = async (id) => {
    const res = await getOrgChart(id);
    setData(res);
    console.log(res);
  };

  if (!data) return null;


  return (
    <DashboardLayout>

      <h2 className="text-2xl font-semibold mb-6">
        Organization Chart
      </h2>

      <SearchEmployee onSelect={load} />

       
      <ManagerChain managers={data.managerChain} onSelect={load} />
      <EmployeeCard employee={data.user} highlight />
      <DirectReports reports={data.directReports} onSelect={load} />

    </DashboardLayout>
  );
};

export default OrgChart;