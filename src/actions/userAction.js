import {
  USER_REGISTERED_SUC,
  LOGIN_FAILED,
  SET_USER,
  USER_REGISTERED_FAI,
  LOGOUT_USER,
} from "../constants";

export const login = (email, password) => async (dispatch) => {
  try {
    // Try Login
    const user = null;
    dispatch({ type: SET_USER, payload: { user: user } });
  } catch (error) {
    dispatch({ type: LOGIN_FAILED, payload: { error: error } });
  }
};

export const register =
  (first_name, last_name, email, password) => async (dispatch) => {
    try {
      // regsiter user
      const user = null;
      dispatch({ type: USER_REGISTERED_SUC, payload: { user: user } });
    } catch (error) {
      dispatch({ type: USER_REGISTERED_FAI, payload: { error: error } });
    }
  };

export const logout = () => async (dispatch) => {
  dispatch({ type: LOGOUT_USER });
};
