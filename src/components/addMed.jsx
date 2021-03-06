import React from "react";
import { useRef, useState, useEffect } from "react";
import userStore from "../stores/userStore";
import { Navigate } from "react-router-dom";
import { getDoc, setDoc, doc } from "firebase/firestore";
import db from "../firebase";

function AddMed() {
  const user = userStore.getState().user;
  const [tokenAdd, setTokenAdd] = useState(false);
  const [redirect, setRedirect] = useState(false);
  useEffect(() => {
    if (!user) return;
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
              oldmeds[i].quantity += parseInt(quantityRef.current.value);
              oldmeds[i].price = parseInt(priceRef.current.value);
              found = true;
              break;
            }
          }
          if (!found) {
            oldmeds.push({
              name: nameRef.current.value,
              quantity: parseInt(quantityRef.current.value),
              price: parseInt(priceRef.current.value),
            });
          }
          data.meds = oldmeds;
          await setDoc(ref, data);
          setRedirect(true);
        } catch (e) {
          alert(e.message);
        }
      }
    };
    code();
    setTokenAdd(false);
  }, [tokenAdd]);
  const nameRef = useRef(null);
  const quantityRef = useRef(null);
  const priceRef = useRef(null);
  if (!user) return <Navigate to="/" />;
  const handleAdd = (e) => {
    e.preventDefault();
    setTokenAdd(true);
  };
  return (
    <div className="container">
      {redirect ? (
        <Navigate to="/home" />
      ) : (
        <form action="">
          <input class="btn" type="text" placeholder="Med Name" ref={nameRef} />
          <input
            class="btn"
            type="text"
            placeholder="quantity"
            ref={quantityRef}
          />
          <input class="btn" type="text" placeholder="price" ref={priceRef} />
          <br />
          <br />
          <input
            class="btn selected btn-fluid2"
            type="button"
            value="Add Stock"
            onClick={handleAdd}
          />
        </form>
      )}
    </div>
  );
}

export default AddMed;
