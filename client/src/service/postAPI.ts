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

export {
  getPostOnProfileWall,
  getAllPostOnNewsFeed,
  likePost,
  unlikePost,
  createAPost,
  getPostPresignedURL,
  getAllPostInRoom,
};
