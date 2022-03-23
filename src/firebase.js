import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";

// Please don't abuse...
const firebaseConfig = {
  apiKey: "AIzaSyCfrjKdogOYppkWFYBk3hVO9k5oWB_CaB8",
  authDomain: "predev-sih.firebaseapp.com",
  databaseURL:
    "https://predev-sih-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "predev-sih",
  storageBucket: "predev-sih.appspot.com",
  messagingSenderId: "1040019542629",
  appId: "1:1040019542629:web:a291f5cfad3ff8b38f6c8c",
  measurementId: "G-KXK0F45YKC",
};

const firebaseApp = initializeApp(firebaseConfig);
const db = getFirestore(firebaseApp);
const auth = getAuth(firebaseApp);

export { auth };
export default db;
