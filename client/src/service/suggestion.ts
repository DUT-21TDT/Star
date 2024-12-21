import { instance } from "../utils/customizeAxios";

const getListSuggestionPeople = async () => {
  const response = await instance.get("/suggestions/people");
  return response.data;
};
export { getListSuggestionPeople };
