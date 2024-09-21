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

const Profile = () => {
  const { id } = useParams();
  const { data, isLoading } = useGetProfileUser(id || "");

  if (isLoading) {
    return (
      <div className="flex items-center justify-center mt-8 w-full">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  return (
    <ConfigProvider theme={profileTheme}>
      <div className="w-full flex justify-center">
        <div
          className=" h-full pt-2 "
          style={{ width: "100%", maxWidth: "650px" }}
        >
          <HeaderProfile />
          <div
            style={{
              border: "1px solid #bdbdbd",
              marginTop: "20px",
              height: "700px",
              borderRadius: "30px",
            }}
          >
            <UserProfile
              isCurrentUser={data?.isCurrentUser}
              isFollowing={data?.isFollowing}
              publicProfile={data.publicProfile}
            />
            <TabProfile />
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};
export default Profile;
