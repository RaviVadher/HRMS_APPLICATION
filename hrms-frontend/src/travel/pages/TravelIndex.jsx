import HRTravelList from "./HRTravelList";
import { useAuth } from "../../context/AuthContext";
import EmployeeTravelList from "./EmployeeTravelList";
import { CircularProgress } from "@mui/material";
const TravelIndex = () => {
  const {user,loading } = useAuth();

   if(loading) return <CircularProgress/>
   if(user.role=== "ROLE_Hr")
   return <HRTravelList />;
   return <EmployeeTravelList/>;


};

export default TravelIndex;