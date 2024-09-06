import { RouterProvider } from "react-router-dom";
import router from "./router/router";
import { ConfigProvider } from "antd";
import { globalTheme } from "./utils/theme";
function App() {
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
