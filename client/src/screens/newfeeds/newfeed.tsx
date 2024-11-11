import { ConfigProvider } from "antd";
import { newFeedsTheme } from "../../utils/theme";
import HeaderNewFeed from "../../components/user/newfeed/header-newfeed";
import CreatePost from "../../components/user/profile/posts/create-post";
import PostsOnNewsFeed from "../../components/user/newfeed/posts-on-newsfeed";
import { useState } from "react";
import { useGetAllRoomForUser } from "../../hooks/room";
import { useNavigate } from "react-router-dom";
interface RoomType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
  isParticipant?: boolean;
}

const NewFeed = () => {
  const [itemActive, setItemActive] = useState({ label: "For you", key: "1" });
  const { listRoomJoined } = useGetAllRoomForUser();
  const childrenRoom = listRoomJoined.map((room: RoomType) => ({
    key: room.id,
    label: room.name,
  }));
  const navigate = useNavigate();

  const menuItems = [
    { key: "1", label: "For you", url: "/" },
    { key: "2", label: "Following", url: "/following" },
    { key: "3", label: "Like", url: "/like" },
    { key: "4", label: "Save", url: "/save" },
    {
      key: "5",
      label: "Room",
      children: childrenRoom,
    },
  ].map(({ key, label, url, children }) => ({
    key,
    label: (
      <div
        className="w-[200px] h-[40px] flex items-center text-[14px] font-semibold"
        onClick={() => {
          if (label !== "Room") {
            if (url) {
              navigate(url);
            }
            setItemActive({ label, key });
          }
        }}
      >
        {label}
      </div>
    ),
    children: children
      ? children.map((child: { key: string; label: string }) => ({
          key: child.key,
          label: (
            <div
              className="w-[200px] h-[35px] flex items-center text-[14px] font-semibold"
              onClick={() => {
                navigate(`/room/${child.key}/posts`);
              }}
            >
              {child.label}
            </div>
          ),
        }))
      : null,
  }));
  return (
    <ConfigProvider theme={newFeedsTheme}>
      <div className="w-full flex justify-center bg-white">
        <div
          className=" h-full pt-2 "
          style={{ width: "100%", maxWidth: "650px" }}
        >
          <HeaderNewFeed itemActive={itemActive.label} menuItems={menuItems} />
          <div
            style={{
              border: "1px solid #ccc",
              marginTop: "20px",
              height: "100%",
              borderRadius: "30px",
              paddingTop: "10px",
            }}
          >
            <CreatePost />
            {/* NewFeed Content */}
            {itemActive.label === "For you" && (
              <>
                <PostsOnNewsFeed />
              </>
            )}
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};
export default NewFeed;
