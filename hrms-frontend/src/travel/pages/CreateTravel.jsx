import DashboardLayout from "../../layout/DashboardLayout";
import TravelForm from "../componants/TravelForm";

const CreateTravel = () => {
  return (
    <DashboardLayout>
      <h2 className="text-2xl font-semibold mb-6">Create Travel</h2>
      <TravelForm />
    </DashboardLayout>
  );
};

export default CreateTravel;