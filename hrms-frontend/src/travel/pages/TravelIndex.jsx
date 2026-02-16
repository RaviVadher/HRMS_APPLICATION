import HRTravelList from "./HRTravelList";
import { useAuth } from "../../context/AuthContext";
import EmployeeTravelList from "./EmployeeTravelList";
const TravelIndex = () => {
  const {user } = useAuth();

   if(user.role=== "ROLE_Manager")
   return <HRTravelList />;
   
   if(user.role=== "ROLE_Employee")
   return <EmployeeTravelList/>;

    return <HRTravelList />;

};

export default TravelIndex;