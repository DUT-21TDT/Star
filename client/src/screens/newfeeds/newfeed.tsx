import { ConfigProvider } from "antd";
import { newFeedsTheme } from "../../utils/theme";
import HeaderNewFeed from "../../components/user/newfeed/header-newfeed";
import CreatePost from "../../components/user/profile/posts/create-post";
import Post from "../../components/user/profile/posts/Post";
import { useGetProfileUser } from "../../hooks/user";
import { useAppSelector } from "../../redux/store/hook";

const NewFeed = () => {
  const id = useAppSelector((state) => state.user.id);
  const { data } = useGetProfileUser(id || "");
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
            <CreatePost
              isCurrentUser={data?.isCurrentUser}
              isFollowing={data?.isFollowing}
              publicProfile={data?.publicProfile}
            />
            <Post
              isCurrentUser={data?.isCurrentUser}
              isFollowing={data?.isFollowing}
              publicProfile={data?.publicProfile}
            />
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};
export default NewFeed;
