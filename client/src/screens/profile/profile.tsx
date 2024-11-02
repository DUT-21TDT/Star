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
import {useEffect, useState} from "react";

const Profile = () => {
  const { id } = useParams();
  const { data, isLoading } = useGetProfileUser(id || "");

  const [followStatus, setFollowStatus] = useState("");

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
    // console.log("data", data),
    (data && (
      <ConfigProvider theme={profileTheme}>
        <div className="w-full flex justify-center bg-white">
          <div
            className=" h-full pt-2 "
            style={{ width: "100%", maxWidth: "650px" }}
          >
            <HeaderProfile />
            <div
              style={{
                border: "1px solid #ccc",
                marginTop: "20px",
                height: "100%",
                borderRadius: "30px",
              }}
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
                <TabProfile isCurrentUser={data?.isCurrentUser} />
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
    )
  ));
};
export default Profile;
