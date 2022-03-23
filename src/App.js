import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Index from "./components/index";
import HomeScreen from "./components/homeScreen";
import ForgotPassword from "./components/forgotPassword";
import PharmaForm from "./components/pharmaForm";
import AddMed from "./components/addMed";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/addMed" element={<AddMed />} />
        <Route path="/filldetails" element={<PharmaForm />} />
        <Route path="/forgot" element={<ForgotPassword />} />
        <Route path="/home" element={<HomeScreen />} />
        <Route path="/" element={<Index />} />
      </Routes>
    </Router>
  );
}

export default App;
