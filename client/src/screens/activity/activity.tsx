import { ConfigProvider } from "antd";
import "../../assets/css/profile.css";
import { activityTheme } from "../../utils/theme";
import { Helmet } from "react-helmet-async";
import HeaderActivity from "../../components/user/activity/header-activity";
import MainContentActivity from "../../components/user/activity/main-activity";

const Activity = () => {
  return (
    <>
      <Helmet>
        <title>Activities • Star</title>
      </Helmet>
      <ConfigProvider theme={activityTheme}>
        <div className="w-full flex justify-center">
          <div
            className=" h-full pt-2 "
            style={{ width: "100%", maxWidth: "650px" }}
          >
            <HeaderActivity />
            <div
              style={{
                border: "1px solid #bdbdbd",
                marginTop: "20px",
                borderRadius: "30px",
                padding: "20px",
                backgroundColor: "white",
              }}
            >
              <MainContentActivity />
            </div>
          </div>
        </div>
      </ConfigProvider>
    </>
  );
};
export default Activity;
