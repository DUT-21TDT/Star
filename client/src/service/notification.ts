import {instance} from "../utils/customizeAxios";

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

const markNotificationAsRead = async (notificationId: string) => {
  const response = await instance.patch(`/notifications/${notificationId}/read`);
  return response.data;
}

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

const getActivitiesLikeOnPostDetail = async (
  postId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null) {
    url = `/posts/${postId}/likes?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/posts/${postId}/likes?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getActivitiesRepostOnPostDetail = async (
  postId: string,
  config: configTypeProfileWall
) => {
  let url;
  if (config.after !== null) {
    url = `/posts/${postId}/reposts?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/posts/${postId}/reposts?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

export {
  getNotifications,
  markNotificationAsRead,
  getActivitiesOnPostDetail,
  getActivitiesLikeOnPostDetail,
  getActivitiesRepostOnPostDetail,
};
