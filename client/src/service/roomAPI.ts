import { instance } from "../utils/customizeAxios";

interface DataCreateRoom {
  name: string;
  description: string | null;
}
const getAllRoom = async () => {
  const response = await instance.get("/rooms");
  return response.data;
};

const createRoom = async (data: DataCreateRoom) => {
  const fomData = new FormData();
  fomData.append("name", data.name);
  if (data.description) {
    fomData.append("description", data.description || "");
  }
  const response = await instance.post("/rooms", fomData);
  return response.data;
};
export { getAllRoom, createRoom };
