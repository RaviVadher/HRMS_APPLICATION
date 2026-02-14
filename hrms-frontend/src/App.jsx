
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import TravelIndex from "./travel/pages/TravelIndex";
import CreateTravel from "./travel/pages/CreateTravel";
import TravelDetails from "./travel/pages/TravelDetails";
import TravelForm from "./travel/componants/TravelForm";
import TravelDocumentUpload from "./travel/componants/TravelDocumentUpload";
import SubmittedDocumentList from "./travel/componants/SubmittedDocumentList";
import DownloadDoc from "./travel/componants/DownloadDoc";
import EmployeeTravelDetails from "./travel/pages/EmployeeTravelDetails";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/addtravel" element={<TravelForm />} />
        <Route path="/home" element={<Home />} />
        <Route path="/travel" element={<TravelIndex />} />
        <Route path="/travel/create" element={<CreateTravel />} />
        <Route path="/travel/:id" element={<TravelDetails />} />
        <Route path="/games" element={<Home />} />
        <Route path="/posts" element={<Home />} />
        <Route path="/org-chart" element={<Home />} />
        <Route path="/jobs" element={<Home />} />
        <Route path="/upload" element={<TravelDocumentUpload />} />
        <Route path="/documents/:id" element={<SubmittedDocumentList />} />
        <Route path="/dowload/:id" element={<DownloadDoc />} />
        <Route path="/travel/my/:id" element={<EmployeeTravelDetails/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;