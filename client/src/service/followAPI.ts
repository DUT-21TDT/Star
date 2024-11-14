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
    url = `/users/${userId}/following?limit=${config.limit}&after=${config.after}`;
  } else {
    url = `/users/${userId}/following?limit=${config.limit}`;
  }
  const response = await instance.get(url);
  return response.data;
};

export { getAllFollowersByUserId, getAllFollowingsByUserId };
