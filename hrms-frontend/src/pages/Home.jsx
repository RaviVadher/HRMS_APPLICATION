import React from "react";
import DashboardLayout from "../layout/DashboardLayout";
import FlightTakeoffIcon from "@mui/icons-material/FlightTakeoff";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import SportsEsportsIcon from "@mui/icons-material/SportsEsports";
import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";

const Home = () => {
  return (
    <DashboardLayout>
      <div className="p-6">

        <div className=" bg-blue-400 align-middle border rounded p-6 text-center mb-8">
          <h2 className="text-2xl font-semibold mb-4">
            Welcome to HRMS Dashboard
          </h2>
          <p className="text-gray-600">
            Select a module from the sidebar to continue.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white border rounded-lg p-6 shadow hover:shadow-lg transition">
            <FlightTakeoffIcon className="text-blue-500 mb-3" fontSize="large" />
            <h3>Manage Your Travels</h3>
            <p className="text-gray-600 text-sm">
              Plan and manage your business travel trips, track travel request,
              submit travel expenses, and monitor status easily from one place.
            </p>
          </div>

          <div className="bg-white border rounded-lg p-6 shadow hover:shadow-lg transition">
            <AccountTreeIcon className="text-green-500 mb-3" fontSize="large" />
            <h3>Organization Chart</h3>
            <p className="text-gray-600 text-sm">
              view company’s reporting structure, explore teams, understand hierarchy,
              and see how employee are connected across the organization.
            </p>
          </div>

          <div className="bg-white border rounded-lg p-6 shadow hover:shadow-lg transition">
            <SportsEsportsIcon className="text-orange-500 mb-3" fontSize="large" />
            <h3>Book Game Slots</h3>
            <p className="text-gray-600 text-sm">
              Reserve Pool table, Chess, FoosBall game slots and refresh your mind with fun and engaging sessions.
            </p>
          </div>

          <div className="bg-white border rounded-lg p-6 shadow hover:shadow-lg transition">
            <EmojiEventsIcon className="text-yellow-500 mb-3" fontSize="large" />
            <h3>Achievements & Posts</h3>
            <p className="text-gray-600 text-sm">
              share your achievement post, celebrate team successes, and stay updated
              with posts of employee birthday and work anniversary across the organization.
            </p>
          </div>

        </div>
      </div>
    </DashboardLayout>
  );
};

export default Home;

