export const QUERY_KEY = {
  getAllUser: () => ["getAllUser"],
  postNewUser: () => ["postNewUser"],
  confirmNewUser: (token: string | null) => ["confirmNewUser", token],
  postLogin: () => ["postLogin"],
  getTokenFromCode: (code: string | null) => ["getTokenFromCode", code],
  // getDataCurrentUser: () => ["getDataCurrentUser"],
  getCurrentUserFromToken: (token: string | null) => [
    "getCurrentUserFromToken",
    token,
  ],
  fetchAllRoom: () => ["fetchAllRoom"],
  getProfileUser: () => ["getProfileUser"],
  getPersonalInformation: () => ["getPersonalInformation"],
  fetchAllRoomForUser: () => ["fetchAllRoomForUser"],
};
