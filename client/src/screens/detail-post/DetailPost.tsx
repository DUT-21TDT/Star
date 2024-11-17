import { ConfigProvider } from "antd";
import HeaderDetailPost from "./HeaderDetailPost";
import { newFeedsTheme } from "../../utils/theme";
import MainContentDetailPost from "../../components/user/profile/posts/detail-post";

const DetailPost = () => {
  return (
    <ConfigProvider theme={newFeedsTheme}>
      <div className="w-full flex justify-center">
        <div
          className="h-full pt-2"
          style={{
            width: "100%",
            maxWidth: "650px",
          }}
        >
          <HeaderDetailPost />
          <div
            style={{
              border: "1px solid #bdbdbd",
              marginTop: "20px",
              borderRadius: "30px",
              padding: "20px 0px 20px 0px",
              backgroundColor: "white",
              minHeight: "800px",
            }}
          >
            <MainContentDetailPost />
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};
export default DetailPost;
