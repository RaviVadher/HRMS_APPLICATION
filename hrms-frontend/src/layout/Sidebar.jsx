import React from "react";
import { NavLink } from "react-router-dom";
import FlightIcon from "@mui/icons-material/Flight";
import SportsEsportsIcon from "@mui/icons-material/SportsEsports";
import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import WorkIcon from "@mui/icons-material/Work";

const menuItems = [
  { name: "Travel", path: "/travel", icon: <FlightIcon /> },
  { name: "Org Chart", path: "/org-chart", icon: <AccountTreeIcon /> },
  { name: "Jobs", path: "/jobs", icon: <WorkIcon /> },
  { name: "Games", path: "/games", icon: <SportsEsportsIcon /> },
  { name: "Achievements", path: "/achievements", icon: <EmojiEventsIcon /> },
];

const Sidebar = () => {
  return (
    <div className="w-64 h-screen bg-blue-500 text-gray-200 flex flex-col">
      <div className="px-6 py-5 border-b border-gray-800">
        <h2 className="text-xl font-semibold tracking-wide text-white mt-1 ml-14"><i> HRMS </i></h2>
      </div>

      <hr />
      <nav className="flex-1 px-6 py-8 space-y-2">
        {menuItems.map((item) => (
          <>
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 px-6 py-4  rounded-md transition-all duration-200 bg-blue-600
               ${isActive ? "bg-blue-700 text-white" : "hover:bg-blue-800"}`}>
              {item.icon}
             <b>{item.name}</b> 
            </NavLink>
            <hr />
          </>
        ))}
      </nav>
    </div>
  );
};

export default Sidebar;