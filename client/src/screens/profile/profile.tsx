import { ConfigProvider } from "antd";
import "../../assets/css/profile.css";
import { profileTheme } from "../../utils/theme";
import UserProfile from "../../components/user/profile/user-profile";
import HeaderProfile from "../../components/user/profile/header-profile";
import TabProfile from "../../components/user/profile/tab-profile";
import { useGetProfileUser } from "../../hooks/user";
import { useParams } from "react-router-dom";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import React, { useEffect, useRef, useState } from "react";
import { useAppSelector } from "../../redux/store/hook";
import { Helmet } from "react-helmet-async";

const Profile: React.FC<{ isPinned?: boolean }> = ({ isPinned }) => {
  const { id: userId } = useParams<{ id: string }>();
  const userIdFromRedux = useAppSelector((state) => state.user.id);
  const id = isPinned ? userIdFromRedux : userId;
  const { data, isLoading } = useGetProfileUser(id || "");

  const [followStatus, setFollowStatus] = useState("");

  const scrollContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (data && !isLoading) {
      setFollowStatus(data.followStatus);
    }
  }, [data, isLoading]);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center mt-8 w-full">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  return (
    data && (
      <>
        <Helmet>
          <title>@{data.publicProfile.username} • Star</title>
        </Helmet>
        <ConfigProvider theme={profileTheme}>
          <div
            className={
              !isPinned ? "w-full flex justify-center" : "animate-zoom-in"
            }
          >
            <div
              className="h-full pt-2"
              style={{ width: "100%", maxWidth: "650px" }}
            >
              <HeaderProfile />
              <div
                style={{
                  border: "1px solid #ccc",
                  borderBottom: "none",
                  borderTopLeftRadius: "30px",
                  borderTopRightRadius: "30px",
                  marginTop: "20px",
                  height: "100%",
                  overflowY: "auto",
                  maxHeight: "calc(100vh - 60px)",
                  scrollbarWidth: "thin",
                  scrollbarColor: "#b9b7b7 white",
                  backgroundColor: "white",
                }}
                ref={scrollContainerRef}
              >
                <UserProfile
                  isCurrentUser={data?.isCurrentUser}
                  followStatus={followStatus}
                  setFollowStatus={setFollowStatus}
                  publicProfile={data?.publicProfile}
                  userId={id || ""}
                />
                {followStatus === "FOLLOWING" ||
                data?.publicProfile.privateProfile === false ||
                data?.isCurrentUser ? (
                  <TabProfile
                    isCurrentUser={data?.isCurrentUser}
                    userId={id || ""}
                    scrollRef={scrollContainerRef}
                  />
                ) : (
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "center",
                      alignItems: "center",
                      height: "50vh",
                      color: "#999999",
                    }}
                  >
                    This profile is private
                  </div>
                )}
              </div>
            </div>
          </div>
        </ConfigProvider>
      </>
    )
  );
};
export default Profile;
