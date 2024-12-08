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
export { getNotifications };
