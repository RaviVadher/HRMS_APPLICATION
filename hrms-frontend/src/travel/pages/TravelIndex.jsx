import HRTravelList from "./HRTravelList";
import { useAuth } from "../../context/AuthContext";
import EmployeeTravelList from "./EmployeeTravelList";
import { CircularProgress } from "@mui/material";
import DashboardLayout from "../../layout/DashboardLayout";
const TravelIndex = () => {
  const {user,loading } = useAuth();

   if(loading) return <DashboardLayout><CircularProgress/></DashboardLayout>
   if(user.role=== "ROLE_Hr")
   return <HRTravelList />;
   return <EmployeeTravelList/>;


};

export default TravelIndex;