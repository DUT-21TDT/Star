import { ConfigProvider } from "antd";
import "../../assets/css/profile.css";
import { roomUserTheme } from "../../utils/theme";
import HeaderRoom from "../../components/user/room/header-room";
import MainRoomContent from "../../components/user/room/main-room";
import { Helmet } from "react-helmet-async";

const RoomUser = () => {
  return (
    <>
      <Helmet>
        <title>Rooms • Star</title>
      </Helmet>
      <ConfigProvider theme={roomUserTheme}>
        <div className="w-full flex justify-center">
          <div
            className=" h-full pt-2 "
            style={{ width: "100%", maxWidth: "650px" }}
          >
            <HeaderRoom />
            <div
              style={{
                border: "1px solid #bdbdbd",
                marginTop: "20px",
                borderRadius: "30px",
                padding: "20px",
                backgroundColor: "white",
              }}
            >
              <MainRoomContent />
            </div>
          </div>
        </div>
      </ConfigProvider>
    </>
  );
};
export default RoomUser;
