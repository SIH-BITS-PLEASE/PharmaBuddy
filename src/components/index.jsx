import React, { useRef, useState } from "react";
import { auth } from "../firebase";
import {
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
} from "firebase/auth";
import { Navigate } from "react-router";
import userStore from "../stores/userStore";
import { SET_USER } from "../constants";
import { Link } from "react-router-dom";

function Index() {
  const [redirect, setRedirect] = useState(false);
  const emailRef = useRef(null);
  const passwordRef = useRef(null);

  const handleLogin = (e) => {
    e.preventDefault();
    signInWithEmailAndPassword(
      auth,
      emailRef.current.value,
      passwordRef.current.value
    )
      .then((authUser) => {
        console.log(authUser);
        userStore.dispatch({ type: SET_USER, payload: authUser });
        setRedirect(true);
      })
      .catch((error) => alert(error.message));
  };

  const handleRegister = (e) => {
    e.preventDefault();
    createUserWithEmailAndPassword(
      auth,
      emailRef.current.value,
      passwordRef.current.value
    )
      .then((authUser) => {
        console.log(authUser);
        userStore.dispatch({ type: SET_USER, payload: authUser });
        setRedirect(true);
      })
      .catch((error) => {
        alert(error.message);
      });
  };

  return (
    <div>
      {redirect ? (
        <Navigate to="/home" />
      ) : (
        <form>
          <input type="email" placeholder="Email" ref={emailRef} />
          <input type="password" placeholder="Password" ref={passwordRef} />
          <input type="submit" onClick={handleLogin} />
          <input type="submit" value="Register" onClick={handleRegister} />
          <Link to="/forgot">Forgot Password</Link>
        </form>
      )}
    </div>
  );
}

export default Index;
