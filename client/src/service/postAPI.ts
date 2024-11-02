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
}

const unlikePost = async (postId: string) => {
  const response = await instance.delete(`/posts/${postId}/likes`);
  return response.data;
}

export { getPostOnProfileWall, getAllPostOnNewsFeed, likePost, unlikePost };
