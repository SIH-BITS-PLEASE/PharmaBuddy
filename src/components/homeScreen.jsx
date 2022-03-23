import React, { useEffect, useState } from "react";
import userStore from "../stores/userStore";
import { Navigate } from "react-router-dom";
import StockList from "./stockList";
import { doc, getDoc } from "firebase/firestore";
import db from "../firebase";
import AddMed from "./addMed";

function HomeScreen() {
  const user = userStore.getState().user;
  const [tokenHome, setTokenHome] = useState(false);
  useEffect(() => {
    const fetchPharma = async () => {
      if (!user.uid) return;
      const ref = doc(db, "Pharma", user.uid);
      const docSnap = await getDoc(ref);
      if (!docSnap.exists()) return setTokenHome(true);
      return setTokenHome(false);
    };
    fetchPharma();
  }, [tokenHome]);
  if (!user) return <Navigate to="/" />;

  return (
    <React.Fragment>
      {tokenHome ? <Navigate to="/filldetails" /> : <AddMed />}
    </React.Fragment>
  );
}

export default HomeScreen;
