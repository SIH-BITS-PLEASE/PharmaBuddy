import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";

// Please don't abuse...
const firebaseConfig = {
  apiKey: "AIzaSyC8h627CHRRZulmYBtKLieXslaQW_cLMhM",
  authDomain: "sihpharmabuddy.firebaseapp.com",
  projectId: "sihpharmabuddy",
  storageBucket: "sihpharmabuddy.appspot.com",
  messagingSenderId: "405630897139",
  appId: "1:405630897139:web:56e57c9e61f5eacf9ee086",
  measurementId: "G-DS95HR0DZ6",
};

const firebaseApp = initializeApp(firebaseConfig);
const db = getFirestore(firebaseApp);
const auth = getAuth(firebaseApp);

export { auth };
export default db;
