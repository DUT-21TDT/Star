import { RouterProvider } from "react-router-dom";
import router from "./router/router";
import { ConfigProvider } from "antd";
import { globalTheme } from "./utils/theme";
import Cookies from "js-cookie";
import { useEffect, useState } from "react";
import { useAppDispatch } from "./redux/store/hook";
import { getCurrentUserFromToken, handleRefreshToken } from "./service/userAPI";
import { storeInformationUser } from "./redux/slice/user-slice";
function App() {
  const dispatch = useAppDispatch();
  const access_token = Cookies.get("access_token") || null;

  const [retry, setRetry] = useState(false);

  const fetchCurrentUser = async (token: string) => {
    try {
      const res = await getCurrentUserFromToken(token);

      if (res.active === false && !retry) {
        try {
          const response = await handleRefreshToken();
          if (response && response.access_token) {
            Cookies.set("access_token", response.access_token);
            Cookies.set("refresh_token", response.refresh_token);
            Cookies.set("id_token", response.id_token);

            setRetry(true);
          }
        } catch (err) {
          console.error("Token refresh failed:", err);

          Cookies.remove("access_token");
          Cookies.remove("refresh_token");
          Cookies.remove("id_token");

          window.location.href = "/login";
        }
      } else {
        dispatch(
          storeInformationUser({
            id: res?.sub,
            role: res?.roles ? res?.roles[0] : "USER",
          })
        );
      }
    } catch (err) {
      console.error("Error fetching user data:", err);
    }
  };

  useEffect(() => {
    if (access_token) {
      fetchCurrentUser(access_token);
    }
  }, [access_token, retry, dispatch]);

  return (
    <>
      {
        <ConfigProvider theme={globalTheme}>
          <RouterProvider router={router} />
        </ConfigProvider>
      }
    </>
  );
}

export default App;
