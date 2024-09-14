import axios from "axios";
import Cookies from "js-cookie";
import { handleRefreshToken } from "../service/userAPI";
const instance = axios.create({
  baseURL: `${import.meta.env.VITE_BACKEND_URL}`,
  withCredentials: true,
});

const instanceAuth = axios.create({
  baseURL: `${import.meta.env.VITE_BACKEND_AUTH_URL}`,
  withCredentials: true,
});

// Add a request interceptor to set the Authorization header
instance.interceptors.request.use(
  (config) => {
    const token = Cookies.get("access_token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a request interceptor
instance.interceptors.request.use();

const NO_RETRY_HEADER = "x-no-retry";
// Add a response interceptor
instance.interceptors.response.use(
  function (response) {
    return response;
  },

  async function (error) {
    const status = error.response ? error.response.status : null;
    if (status === 401 && !error.config.headers[NO_RETRY_HEADER]) {
      try {
        const response = await handleRefreshToken();
        if (response && response.access_token) {
          Cookies.set("access_token", response.access_token);
          Cookies.set("refresh_token", response.refresh_token);
          Cookies.set("id_token", response.id_token);

          error.config.headers[NO_RETRY_HEADER] = "true";
          error.config.headers[
            "Authorization"
          ] = `Bearer ${response.access_token}`;
          return instance.request(error.config);
        }
      } catch (err) {
        console.error("Token refresh failed:", err);
      }
    }
    return Promise.reject(error);
  }
);

//Instance Auth
// Add a response interceptor
instanceAuth.interceptors.response.use(
  function (response) {
    return response;
  },

  async function (error) {
    const status = error.response ? error.response.status : null;

    if (status === 400 && error.config.url === "/oauth2/token") {
      Cookies.remove("access_token");
      Cookies.remove("refresh_token");
      Cookies.remove("id_token");
      alert("Your session has expired. Please log in again.");
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);
export { instance, instanceAuth };
