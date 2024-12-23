import { RouterProvider } from "react-router-dom";
import router from "./router/router";
import { ConfigProvider } from "antd";
import { globalTheme } from "./utils/theme";
import Cookies from "js-cookie";
import { useEffect, useState } from "react";
import { useAppDispatch } from "./redux/store/hook";
import { getCurrentUser, handleRefreshToken } from "./service/userAPI";
import { storeInformationUser } from "./redux/slice/user-slice";
import "react-photo-view/dist/react-photo-view.css";
import { Helmet, HelmetProvider } from "react-helmet-async";

function App() {
  const dispatch = useAppDispatch();
  const access_token = Cookies.get("access_token") || null;

  const [retry, setRetry] = useState(false);

  const fetchCurrentUser = async () => {
    try {
      const res = await getCurrentUser();
      dispatch(
        storeInformationUser({
          id: res?.id,
          username: res?.username,
          firstName: res?.firstName || "",
          lastName: res?.lastName || "",
          role: res?.role || "USER",
          status: res?.status || "INACTIVE",
          avatarUrl: res?.avatarUrl || "",
        })
      );
    } catch (err) {
      try {
        if (retry) {
          throw new Error();
        }

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
    }
  };

  useEffect(() => {
    if (access_token) {
      fetchCurrentUser();
    }
  }, [access_token, retry, dispatch]);

  return (
    <>
      {
        <HelmetProvider>
          <Helmet>
            <title>Star</title>
          </Helmet>
          <ConfigProvider theme={globalTheme}>
            <RouterProvider router={router} />
          </ConfigProvider>
        </HelmetProvider>
      }
    </>
  );
}

export default App;
