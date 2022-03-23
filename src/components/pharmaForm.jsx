import React, { useRef, useEffect, useState } from "react";
import { doc, setDoc } from "firebase/firestore";
import db from "../firebase";
import userStore from "../stores/userStore";
import { Navigate } from "react-router-dom";

function PharmaForm() {
  const nameRef = useRef(null);
  const addRef = useRef(null);
  const [location, setLocation] = useState({ lat: 0, long: 0 });
  const [token, setToken] = useState(false);
  const [redirect, setRedirect] = useState(false);
  useEffect(() => {
    async function run() {
      try {
        const ref = doc(db, "Pharma", userStore.getState().user.uid);
        const payload = {
          id: userStore.getState().user.uid,
          name: nameRef.current.value,
          address: addRef.current.value,
          meds: [],
          location: location,
        };
        await setDoc(ref, payload).then(setRedirect(true));
      } catch (e) {
        console.log(e);
        setToken(false);
      }
    }
    if (token) run();
    setToken(false);
  }, [token]);
  if (!userStore.getState().user) return <Navigate to="/" />;
  const handleSubmit = (e) => {
    e.preventDefault();
    setToken(true);
  };

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      function (position) {
        var latitude = position.coords.latitude;
        var longitude = position.coords.longitude;
        setLocation({ lat: latitude, lon: longitude });
      },
      function error(msg) {
        alert("Please enable your GPS position future.");
      },
      { maximumAge: 600000, timeout: 5000, enableHighAccuracy: true }
    );
  } else {
    alert("Geolocation API is not supported in your browser.");
  }
  return (
    <div className="container">
      {redirect ? (
        <Navigate to="/home" />
      ) : (
        <form>
          <input ref={nameRef} type="text" placeholder="Pharma Name" />
          <input ref={addRef} type="text" placeholder="Address" />
          <input type="button" value="submit" onClick={handleSubmit} />
        </form>
      )}
    </div>
  );
}

export default PharmaForm;
