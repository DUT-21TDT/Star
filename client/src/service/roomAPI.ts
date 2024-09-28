import { instance } from "../utils/customizeAxios";

interface DataCreateRoom {
  name: string;
  description: string | null;
}

interface DataEditRoom extends DataCreateRoom {
  id: string | undefined;
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

const deleteRoom = async (id: string) => {
  const response = await instance.delete(`/rooms/${id}`);
  return response.data;
};

const editRoom = async (data: DataEditRoom) => {
  const formData = new FormData();
  formData.append("name", data.name);
  if (data.description) {
    formData.append("description", data.description || "");
  }
  const response = await instance.patch(`/rooms/${data.id}`, formData);
  return response.data;
};

const joinRoom = async (roomId: string) => {
  const response = await instance.post(`/rooms/${roomId}/members`);
  return response.data;
};

const getAllRoomForUser = async () => {
  const response = await instance.get("/rooms/user-rooms");
  return response.data;
};
const leaveRoomForUser = async (roomId: string) => {
  const response = await instance.delete(`/rooms/${roomId}/members`);
  return response.data;
};
export {
  getAllRoom,
  createRoom,
  deleteRoom,
  editRoom,
  joinRoom,
  getAllRoomForUser,
  leaveRoomForUser,
};
