import React from "react";
import { useState, useEffect } from "react";
import userStore from "../stores/userStore";
import db from "../firebase";
import { getDoc, doc } from "firebase/firestore";
import { Navigate } from "react-router-dom";
import SellMed from "./sellMed";

function StockList() {
  const [medsList, setMedsList] = useState(userStore.getState().meds);
  const [redirect, setRedirect] = useState(false);
  const [addS, setaddS] = useState(false);
  const user = userStore.getState().user;
  useEffect(() => {
    async function getmeds() {
      const ref = doc(db, "Pharma", user.uid);
      const docSnap = await getDoc(ref);
      const data = docSnap.data();
      if (!data.meds) {
        setRedirect(true);
        return;
      } else if (data.meds.length === 0) setRedirect(true);
      else setRedirect(false);
      setMedsList(data.meds);
    }
    getmeds();
  });

  const handleAdd = () => {
    setRedirect(true);
  };
  return (
    <React.Fragment>
      {redirect ? (
        <Navigate to="/addmeds" />
      ) : (
        <React.Fragment>
          <SellMed />
          <br />
          <hr />
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
              </tr>
            </thead>
            <tbody>
              {medsList &&
                medsList.map(({ name, quantity, price }) => {
                  return (
                    <tr key={name}>
                      <td>{name}</td>
                      <td>{price}</td>
                      <td>{quantity}</td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
          <hr />
          <br />
          <button className="btn selected btn-fluid2" onClick={handleAdd}>
            Add Stock
          </button>
        </React.Fragment>
      )}
    </React.Fragment>
  );
}

export default StockList;
