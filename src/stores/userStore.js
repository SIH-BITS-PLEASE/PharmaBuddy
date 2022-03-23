import { createStore } from "redux";
import {
  SET_USER,
  LOGOUT_USER,
  LOGIN_FAILED,
  USER_REGISTERED_SUC,
  USER_REGISTERED_FAI,
  SET_MEDS,
} from "../constants";

function userReducer(state = { user: null, isLoggedIn: false }, action) {
  switch (action.type) {
    case USER_REGISTERED_SUC:
    case SET_USER:
      return { user: action.payload.user, isLoggedIn: true };
    case USER_REGISTERED_FAI:
    case LOGIN_FAILED:
      return { error: action.payload.error.error, user: null };
    case LOGOUT_USER:
      return { user: null, isLoggedIn: false };
    case SET_MEDS:
      var ns = { ...state };
      ns.meds = action.payload;
      return ns;
    default:
      return state;
  }
}

export default createStore(userReducer);
