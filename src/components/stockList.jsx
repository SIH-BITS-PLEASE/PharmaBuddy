import React from "react";
import { useState } from "react";
import userStore from "../stores/userStore";
import { SET_MEDS } from "../constants";

function StockList() {
  const [medsList, setMedsList] = useState(userStore.getState().meds);

  if (!medsList) {
    // Fetch and Update MedsList
    userStore.dispatch({
      type: SET_MEDS,
      payload: [{ name: "disprin", quantity: 5 }],
    });
    setMedsList([{ name: "disprin", quantity: 5 }]);
  }
  console.log(medsList);
  return (
    <React.Fragment>
      <table>
        {medsList.map(({ name }) => {
          return <tr key={name}>{name}</tr>;
        })}
      </table>
    </React.Fragment>
  );
}

export default StockList;
