import { ConfigProvider } from "antd";
import { newFeedsTheme, roomUserTheme } from "../../utils/theme";
import HeaderNewFeed from "../../components/user/newfeed/header-newfeed";
import PostsOnNewsFeed from "../../components/user/newfeed/posts-on-newsfeed";
import { useState } from "react";
import { useGetAllRoomForUser } from "../../hooks/room";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../redux/store/hook";
import optionPin from "../../utils/optionPin";
import HeaderRoom from "../../components/user/room/header-room";
import MainRoomContent from "../../components/user/room/main-room";
import HeaderPeople from "../../components/user/people/header-people";
import MainPeopleContent from "../../components/user/people/main-people";
import Profile from "../profile/profile";
import { Helmet } from "react-helmet-async";
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

  const pinnedPage = useAppSelector((state) => state.user.pin);
  const isPinnedRoom = pinnedPage?.includes(optionPin.ROOM);
  const isPinnedProfile = pinnedPage?.includes(optionPin.PROFILE);
  const isPinnedPeople = pinnedPage?.includes(optionPin.PEOPLE);
  const isPinnedActivity = pinnedPage?.includes(optionPin.ACTIVITY);

  const pinnedCount = [
    isPinnedRoom,
    isPinnedProfile,
    isPinnedPeople,
    isPinnedActivity,
  ].filter(Boolean).length;

  const renderContentPinned = () => {
    return pinnedPage?.map((pin) => {
      if (pin === optionPin.ROOM) {
        return (
          <ConfigProvider key="room" theme={roomUserTheme}>
            <div
              className="h-full pt-2 animate-zoom-in"
              style={{
                width: "100%",
                maxWidth: "650px",
                flexShrink: 0,
              }}
            >
              <HeaderRoom />
              <div
                style={{
                  border: "1px solid #bdbdbd",
                  borderBottom: "none",
                  borderTopLeftRadius: "30px",
                  borderTopRightRadius: "30px",
                  marginTop: "20px",
                  padding: "20px",
                  backgroundColor: "white",
                  overflowY: "auto",
                  maxHeight: "calc(100vh - 60px)",
                  minHeight: "calc(100vh - 60px)",
                  scrollbarWidth: "thin",
                  scrollbarColor: "#b9b7b7 white",
                }}
              >
                <MainRoomContent />
              </div>
            </div>
          </ConfigProvider>
        );
      }

      if (pin === optionPin.PEOPLE) {
        return (
          <ConfigProvider key="people" theme={roomUserTheme}>
            <div
              className="h-full pt-2 animate-zoom-in"
              style={{ width: "100%", maxWidth: "650px", flexShrink: 0 }}
            >
              <HeaderPeople />
              <div
                style={{
                  border: "1px solid #bdbdbd",
                  borderBottom: "none",
                  borderTopLeftRadius: "30px",
                  borderTopRightRadius: "30px",
                  marginTop: "20px",
                  padding: "20px",
                  backgroundColor: "white",
                  overflowY: "auto",
                  maxHeight: "calc(100vh - 60px)",
                  minHeight: "calc(100vh - 60px)",
                  scrollbarWidth: "thin",
                  scrollbarColor: "#b9b7b7 white",
                }}
              >
                <MainPeopleContent />
              </div>
            </div>
          </ConfigProvider>
        );
      }
      if (pin === optionPin.PROFILE) {
        return <Profile isPinned={true} />;
      }
    });
  };

  return (
    <>
      <Helmet>
        <title>Star</title>
      </Helmet>
      <ConfigProvider theme={newFeedsTheme}>
        <div
          className="w-full flex justify-center bg-[#fafafa] gap-5"
          style={{
            minWidth: `${
              pinnedCount < 2
                ? ""
                : pinnedCount === 2
                ? "140vw"
                : pinnedCount === 3
                ? "180vw"
                : "220vw"
            }`,
          }}
        >
          <div
            className=" h-full pt-2"
            style={{ width: "100%", maxWidth: "650px", flexShrink: 0 }}
          >
            <HeaderNewFeed
              itemActive={itemActive.label}
              menuItems={menuItems}
            />
            {/* <div
            style={{
              border: "1px solid #ccc",
              marginTop: "20px",
              height: "100%",
              borderRadius: "30px",
              paddingTop: "10px",
              overflowY: "auto",
              maxHeight: "calc(100vh - 30px)",
            }}
          >
            <CreatePost />
            {itemActive.label === "For you" && (
              <>
                <PostsOnNewsFeed />
              </>
            )}
          </div> */}
            {itemActive.label === "For you" && (
              <>
                <PostsOnNewsFeed />
              </>
            )}
          </div>
          {renderContentPinned()}
        </div>
      </ConfigProvider>
    </>
  );
};
export default NewFeed;
