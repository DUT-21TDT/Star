import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface IUser {
  id?: string;
  username?: string;
  role?: string;
  status?: string;
  avatarUrl?: string;
}

// Create an initial state
const initialState: IUser = {
  id: "",
  username: "",
  role: "",
  status: "",
  avatarUrl: "",
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
  },
  extraReducers: () => {},
});

export const { storeInformationUser, removeInformationUser } =
  userSlice.actions;
// Export the reducer
export default userSlice.reducer;
