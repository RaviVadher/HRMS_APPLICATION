import React from "react";
import { NavLink } from "react-router-dom";
import FlightIcon from "@mui/icons-material/Flight";
import SportsEsportsIcon from "@mui/icons-material/SportsEsports";
import EmojiEventsIcon from "@mui/icons-material/EmojiEvents";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import WorkIcon from "@mui/icons-material/Work";
import ReceiptIcon from "@mui/icons-material/Receipt";

const menuItems = [
  { name: "Travel", path: "/travel", icon: <FlightIcon /> },
  { name: "Expenses", path: "/expenses", icon: <ReceiptIcon /> },
  { name: "Games", path: "/games", icon: <SportsEsportsIcon /> },
  { name: "Achievements", path: "/posts", icon: <EmojiEventsIcon /> },
  { name: "Org Chart", path: "/org-chart", icon: <AccountTreeIcon /> },
  { name: "Jobs", path: "/jobs", icon: <WorkIcon /> },
];

const Sidebar = () => {
  return (
    <div className="w-64 h-screen bg-blue-500 text-white p-4">     
      <h2 className="text-lg font-semibold mb-6">HRMS</h2>

      <nav className="flex flex-col gap-3">
        {menuItems.map((item) => (
          <NavLink
            key={item.name}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center gap-3 px-4 py-2 rounded-lg transition
               ${isActive ? "bg-blue-600" : "hover:bg-gray-700"}`
            }
          >
            {item.icon}
            {item.name}
          </NavLink>
        ))}
      </nav>
    </div>
  );
};

export default Sidebar;