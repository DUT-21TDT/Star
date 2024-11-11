import { ConfigProvider } from "antd";
import { newFeedsTheme } from "../../utils/theme";
import CreatePost from "../../components/user/profile/posts/create-post";
import { useGetAllRoomForUser } from "../../hooks/room";
import PostsInRoom from "../../components/user/newfeed/posts-in-room";
import { useNavigate, useParams } from "react-router-dom";
import HeaderNewFeed from "../../components/user/newfeed/header-newfeed";
import { useEffect, useState } from "react";
interface RoomType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
  isParticipant?: boolean;
}

const PostInRoomContainer = () => {
  const { roomId } = useParams<{ roomId: string }>();
  const { listRoomJoined, isLoading } = useGetAllRoomForUser();
  const childrenRoom = listRoomJoined.map((room: RoomType) => ({
    key: room.id.toString(),
    label: room.name,
  }));
  const [itemActive, setItemActive] = useState({ label: "", key: "" });

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

  useEffect(() => {
    if (roomId) {
      const room = childrenRoom.find(
        (r: { key: string; label: string }) => r.key === roomId
      );
      if (room) {
        setItemActive({ label: room.label, key: room.key });
      }
    }
  }, [roomId, isLoading]);
  return (
    <ConfigProvider theme={newFeedsTheme}>
      <div className="w-full flex justify-center bg-white">
        <div
          className="h-full pt-2"
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
            <PostsInRoom roomId={roomId || ""} key={roomId} />
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};

export default PostInRoomContainer;
