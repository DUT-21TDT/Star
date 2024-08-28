import instance from "../utils/customizeAxios";

interface IUser {
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
}

const fetchAllUser = async () => {
  try {
    const response = await instance.get("/user");
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

const createNewUser = async (data: IUser) => {
  try {
    const dataToSend = {
      username: data.username,
      email: data.email,
      password: data.password,
      confirmPassword: data.confirmPassword,
      firstName: "",
      lastName: "",
      dateOfBirth: "",
      gender: "",
    };
    console.log(dataToSend);
    const response = await instance.post("/auth/signup", dataToSend);
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

export { fetchAllUser, createNewUser };
