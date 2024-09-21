import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface IUser {
  id: string;
  role: string;
}

// Create an initial state
const initialState: IUser = {
  id: "",
  role: "",
};
export const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    storeInformationUser: (state, action: PayloadAction<IUser>) => {
      state.id = action.payload.id;
      state.role = action.payload.role;
    },
    removeInformationUser: (state) => {
      state.id = "";
      state.role = "";
    },
  },
  extraReducers: () => {},
});

export const { storeInformationUser, removeInformationUser } =
  userSlice.actions;
// Export the reducer
export default userSlice.reducer;
