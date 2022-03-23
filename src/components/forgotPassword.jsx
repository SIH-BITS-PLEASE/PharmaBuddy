import React, { useRef } from "react";
import { sendPasswordResetEmail } from "firebase/auth";
import { auth } from "../firebase";

function ForgotPassword() {
  const emailRef = useRef(null);
  const handleForm = (e) => {
    e.preventDefault();
    sendPasswordResetEmail(auth, emailRef.current.value)
      .then(() => {
        // Password reset email sent!
        alert("Password Rest Link Sent Successfully");
      })
      .catch((error) => {
        alert(error.message);
      });
  };
  return (
    <div className="container">
      <form action="">
        <input
          className="box"
          type="email"
          placeholder="Email"
          ref={emailRef}
        />
        <br />
        <input
          className="btn selected btn-fluid2"
          type="submit"
          value="Submit"
          onClick={handleForm}
        />
      </form>
    </div>
  );
}

export default ForgotPassword;
