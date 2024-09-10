import instance from "../utils/customizeAxios";

const getAllRoom = async () => {
  try {
    const response = await instance.get("/rooms");
    return response.data;

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    throw new Error(error);
  }
};
export { getAllRoom };
