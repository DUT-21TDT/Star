import { instance } from "../utils/customizeAxios";
type configTypeProfileWall = {
  limit: number;
  after: string | null;
};
const getPostOnProfileWall = async (
  userId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null) {
    url = `/users/${userId}/posts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/${userId}/posts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getAllPostOnNewsFeed = async (config: configTypeProfileWall) => {
  let url;
  if (config.after !== null) {
    url = `/newsfeed/posts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/newsfeed/posts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const likePost = async (postId: string) => {
  const response = await instance.post(`/posts/${postId}/likes`);
  return response.data;
};

const unlikePost = async (postId: string) => {
  const response = await instance.delete(`/posts/${postId}/likes`);
  return response.data;
};

const createAPost = async (
  roomId: string,
  content: string,
  imageFileNames: string[]
) => {
  const data = {
    roomId,
    content,
    imageFileNames,
  };
  const response = await instance.post(`/posts`, data);
  return response.data;
};

const getPostPresignedURL = async (fileNames: string[]) => {
  const data = {
    fileNames,
  };
  const response = await instance.post(`/images/post-presigned-urls`, data);
  return response.data;
};

const getAllPostInRoom = async (
  roomId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null) {
    url = `/rooms/${roomId}/posts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/rooms/${roomId}/posts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getAllPendingPostInUserWall = async (config: configTypeProfileWall) => {
  let url;
  if (config.after !== null) {
    url = `/users/me/pending-posts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/me/pending-posts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getAllPendingPostForModerator = async (
  config: configTypeProfileWall,
  roomId: string,
  status: string
) => {
  let url;
  if (config.after !== null) {
    url = `/moderator/rooms/${roomId}/posts?limit=${config.limit}&after=${config.after}&status=${status}`;
  } else {
    url = `/moderator/rooms/${roomId}/posts?limit=${config.limit}&status=${status}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const changeStatusPostByModerator = async (status: string, postId: string) => {
  const data = {
    status: status,
  };
  const response = await instance.patch(
    `/moderator/posts/${postId}/status`,
    data
  );
  return response.data;
};

const deletePost = async (postId: string) => {
  const response = await instance.delete(`/posts/${postId}`);
  return response.data;
};

const replyPost = async (
  parentPostId: string,
  content: string,
  imageFileNames: string[]
) => {
  const data = {
    content,
    imageFileNames,
  };
  const response = await instance.post(`/posts/${parentPostId}/replies`, data);
  return response.data;
};

const getPostDetailById = async (postId: string) => {
  const response = await instance.get(`/posts/${postId}`);
  return response.data;
};

const getRepliesByPostId = async (
  postId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null && config.after !== undefined) {
    url = `/posts/${postId}/replies?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/posts/${postId}/replies?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getRepliesOnUserWall = async (
  userId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null && config.after !== undefined) {
    url = `/users/${userId}/replies?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/${userId}/replies?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const repostPost = async (postId: string) => {
  const response = await instance.post(`/posts/${postId}/reposts`);
  return response.data;
};

const deleteRepost = async (postId: string) => {
  const response = await instance.delete(`/posts/${postId}/reposts`);
  return response.data;
};

const getRepostsOnWall = async (
  userId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null) {
    url = `/users/${userId}/reposts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/${userId}/reposts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getAllPostFollowingOnNewFeed = async (config: configTypeProfileWall) => {
  let url;
  if (config.after !== null) {
    url = `/followings/posts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/followings/posts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getAllPostAdmin = async (
  page: number,
  size: number,
  type?: string,
  roomId?: number,
  username?: string,
  status?: string,
  isHidden?: boolean
) => {
  const params: {
    page: number;
    size: number;
    type?: string;
    roomId?: number;
    username?: string;
    status?: string;
    isHidden?: boolean;
  } = {
    page: page,
    size: size,
  };

  if (type) {
    params["type"] = type;
  }

  if (roomId) {
    params["roomId"] = roomId;
  }

  if (username) {
    params["username"] = username;
  }

  if (status) {
    params["status"] = status;
  }

  if (isHidden !== undefined) {
    params["isHidden"] = isHidden;
  }

  const response = await instance.get("/admin/posts", { params });
  return response.data;
};

const hidePost = async (postId: string) => {
  const response = await instance.patch(`/admin/posts/${postId}/hide`);
  return response.data;
};

const unhidePost = async (postId: string) => {
  const response = await instance.patch(`/admin/posts/${postId}/unhide`);
  return response.data;
};

export {
  getPostOnProfileWall,
  getAllPostOnNewsFeed,
  likePost,
  unlikePost,
  createAPost,
  getPostPresignedURL,
  getAllPostInRoom,
  getAllPendingPostInUserWall,
  getAllPendingPostForModerator,
  changeStatusPostByModerator,
  deletePost,
  replyPost,
  getPostDetailById,
  getRepliesByPostId,
  getRepliesOnUserWall,
  repostPost,
  deleteRepost,
  getRepostsOnWall,
  getAllPostFollowingOnNewFeed,
  getAllPostAdmin,
  hidePost,
  unhidePost,
};
