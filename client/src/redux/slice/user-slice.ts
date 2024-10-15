import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface IUser {
  id: string;
  username: string;
  role: string;
  status: string;
  avatarUrl: string;
}

// Create an initial state
const initialState: IUser = {
  id: "",
  username: "",
  role: "",
  status: "",
  avatarUrl: ""
};
export const userSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    storeInformationUser: (state, action: PayloadAction<IUser>) => {
      state.id = action.payload.id;
      state.username = action.payload.username;
      state.role = action.payload.role;
      state.status = action.payload.status;
      state.avatarUrl = action.payload.avatarUrl;
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
