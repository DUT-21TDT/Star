import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface IUser {
  id?: string;
  username?: string;
  firstname?: string;
  lastname?: string;
  role?: string;
  status?: string;
  avatarUrl?: string;
  pin?: string[];
}

// Create an initial state
const initialState: IUser = {
  id: "",
  username: "",
  firstname: "",
  lastname: "",
  role: "",
  status: "",
  avatarUrl: "",
  pin: [],
};
export const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    storeInformationUser: (state, action: PayloadAction<IUser>) => {
      return {
        ...state,
        ...action.payload,
      };
    },
    removeInformationUser: (state) => {
      state.id = "";
      state.role = "";
    },
    addPinPageToRedux: (state, action: PayloadAction<string>) => {
      if (state.pin?.includes(action.payload)) return;
      state.pin?.push(action.payload);
    },
    removePinPageToRedux: (state, action: PayloadAction<string>) => {
      state.pin = state.pin?.filter((item) => item !== action.payload);
    },
  },
  extraReducers: () => {},
});

export const {
  storeInformationUser,
  removeInformationUser,
  addPinPageToRedux,
  removePinPageToRedux,
} = userSlice.actions;
// Export the reducer
export default userSlice.reducer;
