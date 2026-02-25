
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import TravelIndex from "./travel/pages/TravelIndex";
import CreateTravel from "./travel/pages/CreateTravel";
import TravelDetails from "./travel/pages/TravelDetails";
import TravelForm from "./travel/componants/TravelForm";
import TravelDocumentUpload from "./travel/componants/TravelDocumentUpload";
import SubmittedDocumentList from "./travel/componants/SubmittedDocumentList";
import EmployeeTravelDetails from "./travel/pages/EmployeeTravelDetails";
import ExpenseProofList from "./travel/pages/ExpenseProofList";
import OrgChart from "./orgChart/pages/OrgChart";
import JobPage from "./openJob/pages/JobPage";
import CreateJobForm from "./openJob/componants/CreateJobForm";
import JobDetails from "./openJob/pages/JobDetails";
import TeamTravel from "./travel/pages/TeamTravel";
import GameList from "./games/pages/GameList";
import GameConfig from "./games/pages/GameConfig";
import Slots from "./games/pages/Slots";
import BookedHistory from "./games/pages/BookedHistory";
import AchievementsFeed from "./achievements/pages/achievementsFeed";
import AchievementDetails from "./achievements/pages/AchievementDetails";
import Profile from "./pages/Profile";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/home" element={<Home />} />
        <Route path="/profile/:id" element={<Profile />} />
        <Route path="/addtravel" element={<TravelForm />} />
        <Route path="/travel" element={<TravelIndex />} />
        <Route path="/travel/create" element={<CreateTravel />} />
        <Route path="/travel/:id" element={<TravelDetails />} />
        <Route path="/games" element={<GameList />} />
        <Route path="/upload" element={<TravelDocumentUpload />} />
        <Route path="/documents/:id" element={<SubmittedDocumentList />} />
        <Route path="/travel/my/:id" element={<EmployeeTravelDetails/>} />
        <Route path="/expenseProof/:id" element={<ExpenseProofList/>} />
        <Route path="/travel/team" element={<TeamTravel/>} />
        
        <Route path="/org-chart" element={<OrgChart />} />

        
        {/* jobs */}
        <Route path="/jobs" element={<JobPage/>} />
        <Route path="/job/createForm" element={<CreateJobForm/>}/>
        <Route path="/job/:id" element={<JobDetails/>}/>

        {/* game */}
        <Route path="/games/:id/gameconfig" element={<GameConfig/>}/>
          <Route path="/games/:id/slot" element={<Slots/>}/>
          <Route path="/game/:gameId/bookingHistory/:userId" element={<BookedHistory/>}/>

        {/* achievements */}
         <Route path="/achievements" element={<AchievementsFeed />}/>
         <Route path="/achievements/:id" element={<AchievementDetails/>} />
        
      </Routes>
    </BrowserRouter>
  );
}

export default App;