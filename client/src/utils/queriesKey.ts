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
  getProfileUser: (userId: string) => ["getProfileUser", userId],
  getPersonalInformation: () => ["getPersonalInformation"],
  fetchAllRoomForUser: () => ["fetchAllRoomForUser"],
  fetchAllPostsOnWall: (userId: string) => ["fetchAllPostsOnWall", userId],
  fetchAllPostsOnNewsFeed: () => ["fetchAllPostsOnNewsFeed"],
};
