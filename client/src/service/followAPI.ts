import { instance } from "../utils/customizeAxios";
type configType = {
  limit: number;
  after: string | null;
};
const getAllFollowersByUserId = async (userId: string, config: configType) => {
  let url;
  if (config.after !== null) {
    url = `/users/${userId}/followers?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/${userId}/followers?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const getAllFollowingsByUserId = async (userId: string, config: configType) => {
  let url;
  if (config.after !== null) {
    url = `/users/${userId}/followings?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/${userId}/followings?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const deleteFollowerByUserId = async (userId: string) => {
  const response = await instance.delete(`/users/me/followers/${userId}`);
  return response.data;
};

const getAllFollowRequest = async (config: configType) => {
  let url;
  if (config.after !== null) {
    url = `/users/me/follow-requests?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/me/follow-requests?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

const acceptFollowRequest = async (followingId: string) => {
  const response = await instance.patch(
    `/users/me/follow-requests/${followingId}`
  );
  return response.data;
};

const getCountFollowerByUserId = async (userId: string) => {
  const response = await instance.get(`/users/${userId}/follow-sections`);
  return response.data;
};

export {
  getAllFollowersByUserId,
  getAllFollowingsByUserId,
  deleteFollowerByUserId,
  getAllFollowRequest,
  acceptFollowRequest,
  getCountFollowerByUserId,
};
