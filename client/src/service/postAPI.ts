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
export { getPostOnProfileWall };
