import { RouterProvider } from "react-router-dom";
import router from "./router/router";
import { ConfigProvider } from "antd";
function App() {
  return (
    <>
      {
        <ConfigProvider
          theme={{
            components: {
              Input: {
                hoverBorderColor: "#bdbdbd",
                activeBorderColor: "#bdbdbd",
                activeShadow: "0 0 0 2px rgba(255, 255, 255, 0.2)",
              },
              Form: {
                itemMarginBottom: 10,
              },
            },
          }}
        >
          <RouterProvider router={router} />
        </ConfigProvider>
      }
    </>
  );
}

export default App;
