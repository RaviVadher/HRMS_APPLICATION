import React from "react";
import DashboardLayout from "../layout/DashboardLayout";

const Home = () => {
  return (
    <DashboardLayout>
      <h2 className="text-2xl font-semibold mb-4">
        Welcome to HRMS Dashboard
      </h2>

      <p className="text-gray-600">
        Select a module from the sidebar to continue.
      </p>
    </DashboardLayout>
  );
};

export default Home;