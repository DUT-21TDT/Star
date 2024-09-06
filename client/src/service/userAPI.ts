import instance from "../utils/customizeAxios";
import axios from "axios";
import Cookies from "js-cookie";
interface IUser_SignUp {
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

const createNewUser = async (data: IUser_SignUp) => {
  try {
    const response = await instance.post("/auth/signup", data);
    return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    throw new Error(error.response.data);
  }
};
const confirmAccount = async (token: string | null) => {
  try {
    if (token != null) {
      const response = await instance.get(
        `/auth/confirm-signup?token=${token}`
      );
      return response.data;
    }
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    throw new Error(error.response.data);
  }
};

const getTokenFromCode = async (code: string) => {
  const tokenUrl = import.meta.env.VITE_TOKEN_URL;
  const clientId = import.meta.env.VITE_CLIENT_ID;
  const clientSecret = import.meta.env.VITE_CLIENT_SECRET;
  const redirectUri = import.meta.env.VITE_REDIRECT_URI;

  const body = new URLSearchParams({
    grant_type: "authorization_code",
    code: code,
    redirect_uri: redirectUri,
    client_id: clientId,
  });

  try {
    const response = await axios.post(tokenUrl, body.toString(), {
      headers: {
        Authorization: "Basic " + btoa(`${clientId}:${clientSecret}`),
        "Content-Type": "application/x-www-form-urlencoded",
      },
    });

    return response.data;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    throw new Error(error.response.data);
  }
};

const getDataCurrentUser = async () => {
  try {
    const token = Cookies.get("access_token");
    if (token !== null) {
      const response = await instance.get("/home/me", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    }
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    throw new Error(error.response.data);
  }
};

export {
  fetchAllUser,
  createNewUser,
  confirmAccount,
  getTokenFromCode,
  getDataCurrentUser,
};
