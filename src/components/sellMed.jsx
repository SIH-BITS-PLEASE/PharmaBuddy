import React from "react";
import { useRef, useState, useEffect } from "react";
import userStore from "../stores/userStore";
import { Navigate } from "react-router-dom";
import { getDoc, setDoc, doc } from "firebase/firestore";
import db from "../firebase";

function SellMed() {
  const user = userStore.getState().user;
  const [tokenAdd, setTokenAdd] = useState(false);
  useEffect(() => {
    if (!user.uid) return;
    const code = async () => {
      const ref = doc(db, "Pharma", user.uid);
      const docSnap = await getDoc(ref);
      var data = docSnap.data();
      var oldmeds = data.meds;
      if (tokenAdd) {
        try {
          var found = false;
          for (var i = 0; i < oldmeds.length; i++) {
            if (oldmeds[i].name === nameRef.current.value) {
              if (oldmeds[i].quantity < parseInt(quantityRef.current.value)) {
                alert("Insuffcient Stock");
                return;
              }
              oldmeds[i].quantity -= parseInt(quantityRef.current.value);
              found = true;
              break;
            }
          }
          if (!found) {
            alert("Med not found");
          }
          data.meds = oldmeds;
          await setDoc(ref, data).then(alert("Updated"));
        } catch (e) {
          alert(e.message);
        }
      }
    };
    code();
    setTokenAdd(false);
  }, [tokenAdd, user.uid]);
  const nameRef = useRef(null);
  const quantityRef = useRef(null);
  if (!user) return <Navigate to="/" />;
  const handleAdd = (e) => {
    e.preventDefault();
    setTokenAdd(true);
  };
  return (
    <div className="container">
      <form action="">
        <input class="btn" type="text" placeholder="Med Name" ref={nameRef} />
        <input
          class="btn"
          type="text"
          placeholder="quantity"
          ref={quantityRef}
        />
        <br />
        <br />
        <input
          class="btn selected btn-fluid2"
          type="button"
          value="Sell Stock"
          onClick={handleAdd}
        />
      </form>
    </div>
  );
}

export default SellMed;
