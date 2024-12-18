import { instance } from "../utils/customizeAxios";
type configTypeProfileWall = {
  limit: number;
  after: string | null;
};
const getNotifications = async (config: configTypeProfileWall) => {
  let url;
  if (config.after !== null) {
    url = `/notifications?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/notifications?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getActivitiesOnPostDetail = async (
  postId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null) {
    url = `/posts/${postId}/interactions?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/posts/${postId}/interactions?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

export { getNotifications, getActivitiesOnPostDetail };
