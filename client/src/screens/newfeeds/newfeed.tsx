import { ConfigProvider } from "antd";
import { newFeedsTheme } from "../../utils/theme";
import HeaderNewFeed from "../../components/user/newfeed/header-newfeed";
import CreatePost from "../../components/user/profile/posts/create-post";
import PostsOnNewsFeed from "../../components/user/newfeed/posts-on-newsfeed";

const NewFeed = () => {
  return (
    <ConfigProvider theme={newFeedsTheme}>
      <div className="w-full flex justify-center bg-white">
        <div
          className=" h-full pt-2 "
          style={{ width: "100%", maxWidth: "650px" }}
        >
          <HeaderNewFeed />
          <div
            style={{
              border: "1px solid #ccc",
              marginTop: "20px",
              height: "100%",
              borderRadius: "30px",
              paddingTop: "10px",
            }}
          >
            {/* NewFeed Content */}
            <CreatePost />
            <PostsOnNewsFeed />
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};
export default NewFeed;
