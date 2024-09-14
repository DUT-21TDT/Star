import { RouterProvider } from "react-router-dom";
import router from "./router/router";
import { ConfigProvider } from "antd";
import { globalTheme } from "./utils/theme";
import Cookies from "js-cookie";
import { useEffect } from "react";
import { useAppDispatch } from "./redux/store/hook";
import { getCurrentUserFromToken } from "./service/userAPI";
import { storeInformationUser } from "./redux/slice/user-slice";
function App() {
  const dispatch = useAppDispatch();
  const access_token = Cookies.get("access_token") || null;
  useEffect(() => {
    getCurrentUserFromToken(access_token).then((res) => {
      //store user data in redux
      dispatch(
        storeInformationUser({
          name: res?.sub,
          role: res?.roles[0],
        })
      );
    });
  }, [access_token, dispatch]);

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
