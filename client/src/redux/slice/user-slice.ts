import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface IUser {
  name: string;
  role: string;
}

// Create an initial state
const initialState: IUser = {
  name: "",
  role: "",
};
export const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    storeInformationUser: (state, action: PayloadAction<IUser>) => {
      state.name = action.payload.name;
      state.role = action.payload.role;
    },
    removeInformationUser: (state) => {
      state.name = "";
      state.role = "";
    },
  },
  extraReducers: () => {},
});

export const { storeInformationUser, removeInformationUser } =
  userSlice.actions;
// Export the reducer
export default userSlice.reducer;
