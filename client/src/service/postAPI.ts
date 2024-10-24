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
    url = `/posts/newsfeed?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/posts/newsfeed?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  console.log(response);
  return response.data;
};
export { getPostOnProfileWall, getAllPostOnNewsFeed };
