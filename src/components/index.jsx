import React, { useRef, useState } from "react";
import { auth } from "../firebase";
import {
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
} from "firebase/auth";
import { Navigate } from "react-router";
import userStore from "../stores/userStore";
import { SET_USER } from "../constants";

function Index() {
  const [redirect, setRedirect] = useState(false);
  const [forgot, setForgot] = useState(false);
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

  const handleForgot = () => {
    setForgot(true);
  };
  return (
    <div>
      {redirect ? (
        <Navigate to="/home" />
      ) : (
        <React.Fragment>
          {forgot ? (
            <Navigate to="/forgot" />
          ) : (
            <form>
              <input
                className="box"
                type="email"
                placeholder="Email"
                ref={emailRef}
              />
              <br />
              <input
                className="box"
                type="password"
                placeholder="Password"
                ref={passwordRef}
              />
              <br />
              <input
                className="btn selected btn-fluid2"
                type="submit"
                onClick={handleLogin}
              />
              <br />
              <input
                className="btn selected btn-fluid2"
                type="submit"
                value="Register"
                onClick={handleRegister}
              />
              <input
                className="btn selected btn-fluid2"
                type="submit"
                value="Forgot"
                onClick={handleForgot}
              />
            </form>
          )}
        </React.Fragment>
      )}
    </div>
  );
}

export default Index;
