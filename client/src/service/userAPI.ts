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

// const getDataCurrentUser = async () => {
//   try {
//     const token = Cookies.get("access_token");
//     if (token !== null) {
//       const response = await instance.get("/home/me", {
//         headers: {
//           Authorization: `Bearer ${token}`,
//         },
//       });
//       return response.data;
//     }
//     // eslint-disable-next-line @typescript-eslint/no-explicit-any
//   } catch (error: any) {
//     throw new Error(error.response.data);
//   }
// };

const getCurrentUserFromToken = async (token: string | null) => {
  try {
    const introspectUrl = import.meta.env.VITE_BACKEND_AUTH_URL + "/oauth2/introspect"
    const clientId = import.meta.env.VITE_CLIENT_ID;
    const clientSecret = import.meta.env.VITE_CLIENT_SECRET;
    const response = await axios.post(introspectUrl, {
      token: token,
      token_type_hint: "access_token",
    }, {
      headers: {
        Authorization: 'Basic ' + btoa(`${clientId}:${clientSecret}`),
        'Content-Type': "application/x-www-form-urlencoded",
      },
      withCredentials: true
    });
    return response.data;
  } catch (error) {
    console.log(error);
  }
};

const logOut = async () => {
  try {
    const logoutUrl = import.meta.env.VITE_BACKEND_AUTH_URL + "/connect/logout";
    const idToken = Cookies.get("id_token");

    if (!idToken) {
      throw new Error("idToken is null");
    }
    
    const clientId = import.meta.env.VITE_CLIENT_ID;

    await instance.post(logoutUrl, {
      id_token_hint: idToken,
      client_id: clientId,
    }, 
    {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      withCredentials: true
    });
  } catch (error) {
    console.log(error);
  }
};

export {
  fetchAllUser,
  createNewUser,
  confirmAccount,
  getTokenFromCode,
  // getDataCurrentUser,
  getCurrentUserFromToken,
  logOut
};
