import React from "react";
import { useState, useEffect } from "react";
import userStore from "../stores/userStore";
import db from "../firebase";
import { getDoc, doc } from "firebase/firestore";
import { Navigate } from "react-router-dom";

function StockList() {
  const [medsList, setMedsList] = useState(userStore.getState().meds);
  const [redirect, setRedirect] = useState(false);
  const user = userStore.getState().user;
  useEffect(() => {
    async function getmeds() {
      const ref = doc(db, "Pharma", user.uid);
      const docSnap = await getDoc(ref);
      const data = docSnap.data();
      if (data.meds.length === 0) setRedirect(true);
      else setRedirect(false);
      setMedsList(data.meds);
    }
    getmeds();
  });
  return (
    <React.Fragment>
      {redirect ? (
        <Navigate to="/addmeds" />
      ) : (
        <table>
          <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Quantity</th>
          </tr>
          {medsList &&
            medsList.map(({ name, quantity, price }) => {
              return (
                <tr key={name}>
                  <td>{name}</td>
                  <td>{quantity}</td>
                  <td>{price}</td>
                </tr>
              );
            })}
        </table>
      )}
    </React.Fragment>
  );
}

export default StockList;
