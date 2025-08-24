import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import ChallengeCreate from "./modules/challenges/ChallengeCreate";
import MemoryMap from "./modules/memory-map/MemoryMap";
import AdminDashboard from "./modules/admin/AdminDashboard";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/challenge/create" element={<ChallengeCreate />} />
        <Route path="/memory-map" element={<MemoryMap />} />
        <Route path="/admin" element={<AdminDashboard />} />
        {/* ...add other routes as needed */}
      </Routes>
    </BrowserRouter>
  );
}
export default App;