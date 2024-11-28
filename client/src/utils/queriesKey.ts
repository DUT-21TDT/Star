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
  fetchAllUsers: () => ["fetchAllUser"],
  fetchAllRoom: () => ["fetchAllRoom"],
  getRoomDetails: (roomId: string) => ["getRoomDetails", roomId],
  getModerators: (roomId: string) => ["getModerators", roomId],
  getProfileUser: (userId: string) => ["getProfileUser", userId],
  getPersonalInformation: () => ["getPersonalInformation"],
  fetchAllRoomForUser: () => ["fetchAllRoomForUser"],
  fetchAllPostsOnWall: (userId: string) => ["fetchAllPostsOnWall", userId],
  fetchAllPostsOnNewsFeed: () => ["fetchAllPostsOnNewsFeed"],
  likePost: (postId: string) => ["likePost", postId],
  fetchAllPostsInRoom: (roomId: string) => ["fetchAllPostsInRoom", roomId],
  fetchAllPendingPostOnWall: () => ["fetchAllPendingPostOnWall"],
  fetchAllPendingPostForModerator: (roomId: string, status: string) => [
    "fetchAllPendingPostForModerator",
    roomId,
    status,
  ],
  getMembersInRoom: (roomId: string) => ["getMembersInRoom", roomId],
  fetchAllFollowersByUserId: (userId: string) => [
    "fetchAllFollowersByUserId",
    userId,
  ],
  fetchAllFollowingByUserId: (userId: string) => [
    "fetchAllFollowingByUserId",
    userId,
  ],
  fetchAllFollowRequest: () => ["fetchAllFollowRequest"],
  fetchCountFollowerByUserId: (userId: string) => [
    "fetchCountFollowerByUserId",
    userId,
  ],
  fetchPostDetailById: (postId: string) => ["fetchPostDetailById", postId],
  fetchRepliesByPostId: (postId: string) => ["fetchRepliesByPostId", postId],
  fetchRepliesByUserId: (userId: string) => ["fetchRepliesByUserId", userId],
  fetchRepostsByUserId: (userId: string) => ["fetchRepostsByUserId", userId],
};
