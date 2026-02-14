import HRTravelList from "./HRTravelList";
import { useAuth } from "../../context/AuthContext";
import { jwtDecode } from 'jwt-decode'
import EmployeeTravelList from "./EmployeeTravelList";
const TravelIndex = () => {
  const { user,role } = useAuth();

   if(role=== "ROLE_Hr")
  return <HRTravelList />;

  return <EmployeeTravelList/>;
};

export default TravelIndex;