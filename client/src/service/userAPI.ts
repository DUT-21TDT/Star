import { instance, instanceAuth } from "../utils/customizeAxios";
import axios from "axios";
import Cookies from "js-cookie";

interface IUser_SignUp {
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
}

interface IEditProfile {
  username: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  avatarFileName?: string;
  dateOfBirth?: string;
  gender?: string;
  privateProfile?: boolean;
}
const createNewUser = async (data: IUser_SignUp) => {
  const response = await instance.post("/auth/signup", data);
  return response.data;
};
const confirmAccount = async (token: string | null) => {
  if (token != null) {
    const response = await instance.get(`/auth/confirm-signup?token=${token}`);
    return response.data;
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
    console.log(error);
  }
};

const getCurrentUser = async () => {
  const response = await instance.get(`/users/me`);
  return response.data;
};

/**
 * @deprecated This function is outdated and will be remove in future.
 * Please use getCurrentUser() instead.
 */
const getCurrentUserFromToken = async (token: string | null) => {
  try {
    const introspectUrl =
      import.meta.env.VITE_BACKEND_AUTH_URL + "/oauth2/introspect";
    const clientId = import.meta.env.VITE_CLIENT_ID;
    const clientSecret = import.meta.env.VITE_CLIENT_SECRET;
    const response = await axios.post(
      introspectUrl,
      {
        token: token,
        token_type_hint: "access_token",
      },
      {
        headers: {
          Authorization: "Basic " + btoa(`${clientId}:${clientSecret}`),
          "Content-Type": "application/x-www-form-urlencoded",
        },
        withCredentials: true,
      }
    );
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

const revokeToken = async () => {
  try {
    const revokeUrl = import.meta.env.VITE_BACKEND_AUTH_URL + "/oauth2/revoke";
    const refreshToken = Cookies.get("refresh_token");
    const clientId = import.meta.env.VITE_CLIENT_ID;
    const clientSecret = import.meta.env.VITE_CLIENT_SECRET;

    await instance.post(
      revokeUrl,
      {
        token: refreshToken,
        token_type_hint: "refresh_token",
      },
      {
        headers: {
          Authorization: "Basic " + btoa(`${clientId}:${clientSecret}`),
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    );
  } catch (error) {
    console.log(error);
  }
};

const endSession = async () => {
  try {
    const logoutUrl = import.meta.env.VITE_BACKEND_AUTH_URL + "/connect/logout";
    const idToken = Cookies.get("id_token");

    if (!idToken) {
      throw new Error("idToken is null");
    }

    const clientId = import.meta.env.VITE_CLIENT_ID;

    await instance.post(
      logoutUrl,
      {
        id_token_hint: idToken,
        client_id: clientId,
      },
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        withCredentials: true,
      }
    );
  } catch (error) {
    console.log(error);
  }
};

const handleRefreshToken = async () => {
  const form = new FormData();
  form.append("grant_type", "refresh_token");
  form.append("refresh_token", Cookies.get("refresh_token") || "");
  const clientId = import.meta.env.VITE_CLIENT_ID;
  const clientSecret = import.meta.env.VITE_CLIENT_SECRET;
  const response = await instanceAuth.post(`/oauth2/token`, form, {
    headers: {
      Authorization: "Basic " + btoa(`${clientId}:${clientSecret}`),
      "Content-Type": "application/x-www-form-urlencoded",
    },

    withCredentials: true,
  });
  return response.data;
};

const getInformationUserFromId = async (username: string | null) => {
  const response = await instance.get(`/users/${username}`);
  return response.data;
};

const editProfile = async (data: IEditProfile) => {
  const formData = new FormData();
  formData.append("username", data.username);
  formData.append("firstName", data.firstName || "");
  formData.append("lastName", data.lastName || "");
  formData.append("bio", data.bio || "");
  formData.append("avatarFileName", data.avatarFileName || "");
  formData.append("dateOfBirth", data.dateOfBirth || "");
  formData.append("gender", data.gender || "");
  formData.append("privateProfile", data.privateProfile ? "true" : "false");
  const response = await instance.patch(
    `/users/me/personal-information`,
    formData
  );
  return response.data;
};

const getPersonalInformation = async () => {
  const response = await instance.get(`/users/me/personal-information`);
  return response.data;
};

const getPresignedURL = async (fileName: string) => {
  const response = await instance.post(
    `/images/avatar-presigned-url`,
    fileName,
    {
      headers: {
        "Content-Type": "text/plain",
      },
    }
  );
  return response.data;
};

const resendVerifyEmail = async (email: string) => {
  if (email === "") {
    const response = await instance.post(`auth/resend-confirmation`);
    return response;
  }
  const response = await instance.post(`auth/resend-confirmation`, {
    email,
  });
  return response;
};

const followUser = async (userId: string) => {
  const response = await instance.post(`/users/me/followings`, {
    userId,
  });
  return response.data;
};

const unfollowUser = async (userId: string) => {
  const response = await instance.delete(`/users/me/followings/${userId}`);
  return response.data;
};

export {
  createNewUser,
  confirmAccount,
  getTokenFromCode,
  getCurrentUser,
  getCurrentUserFromToken,
  revokeToken,
  endSession,
  handleRefreshToken,
  getInformationUserFromId,
  editProfile,
  getPersonalInformation,
  getPresignedURL,
  resendVerifyEmail,
  followUser,
  unfollowUser,
};
