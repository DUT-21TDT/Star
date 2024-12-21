import { Avatar } from "antd";
import { useAppDispatch, useAppSelector } from "../../../redux/store/hook";
import { useGetListSuggestion } from "../../../hooks/suggestion";
import SuggestionItem from "./suggestion-item";
import react from "../../../assets/images/QR.svg";
import { useNavigate } from "react-router-dom";
import { endSession, revokeToken } from "../../../service/userAPI";
import Cookies from "js-cookie";
import { removeInformationUser } from "../../../redux/slice/user-slice";
interface ISuggestionItem {
  userId: string;
  username: string;
  avatarUrl: string;
  firstName: string;
  lastName: string;
  commonRoomRelation: {
    repId: string;
    repName: string;
    count: number;
    score: number;
  };
  mutualFollowingRelation: {
    repId: string;
    repName: string;
    count: number;
    score: number;
  };
  mutualFriendRelation: {
    repId: string;
    repName: string;
    count: number;
    score: number;
  };
  suggestType: string;
  totalRelationScore: number;
}

const SuggestionPeopleOnNewFeed = () => {
  const { username, avatarUrl } = useAppSelector((state) => state.user);
  const { data } = useGetListSuggestion();
  const navgiate = useNavigate();

  const dispatch = useAppDispatch();
  const handleLogout = async () => {
    await revokeToken();
    await endSession();
    Cookies.remove("JSESSIONID");
    Cookies.remove("id_token");
    Cookies.remove("refresh_token");
    Cookies.remove("access_token");
    dispatch(removeInformationUser());
    window.location.href = "/login";
  };

  return (
    <div>
      <div
        className="w-[400px] h-[fit-content] bg-white"
        style={{
          marginTop: "55px",
          border: "1px solid #dbdbdb",
          borderRadius: "20px",
          padding: "15px 15px 0px 15px",
        }}
      >
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "auto 1fr auto",
            alignItems: "center",
            gap: "15px",
          }}
        >
          <Avatar src={avatarUrl} size={45} />
          <div>
            <h4
              style={{
                fontWeight: 500,
                display: "-webkit-box",
                WebkitLineClamp: 1,
                WebkitBoxOrient: "vertical",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}
            >
              {username}
            </h4>
            <p
              style={{
                fontSize: "14px",
                fontWeight: 400,
                color: "#8e8e8e",
                display: "-webkit-box",
                WebkitLineClamp: 1,
                WebkitBoxOrient: "vertical",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}
            >
              {/* {firstName || lastName ? (
                <div className="text-[14px] text-gray-500">
                  {`${firstName || ""} ${lastName || ""}`}
                </div>
              ) : (
                <div className="text-[14px] text-gray-500">{username}</div>
              )} */}
              {username}
            </p>
          </div>
          <p
            style={{
              fontSize: "14px",
              fontWeight: 500,
              color: "black",
              cursor: "pointer",
            }}
            onClick={handleLogout}
          >
            Switch
          </p>
        </div>
        <div className="flex items-center justify-between py-3">
          <h3
            style={{
              fontSize: "16px",
              fontWeight: 500,
              color: "#8e8e8e",
            }}
          >
            Suggested for you
          </h3>
          <div
            style={{
              fontSize: "14px",
              fontWeight: 500,
              color: "black",
              cursor: "pointer",
            }}
            onClick={() => {
              navgiate("/search");
            }}
          >
            See all
          </div>
        </div>
        <div>
          {data &&
            data.map((item: ISuggestionItem) => <SuggestionItem data={item} />)}
        </div>
      </div>
      <div>
        <div
          style={{
            padding: "5px",
          }}
        >
          <p
            style={{
              fontSize: "16px",
              fontWeight: 500,
              color: "#8e8e8e",
            }}
          >
            Download mobile app
          </p>
          <div
            style={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              backgroundColor: "white",
              width: "150px",
              height: "150px",
              borderRadius: "20px",
              marginTop: "5px",
            }}
          >
            <img
              src={react}
              alt="qr"
              style={{
                width: "100%",
                height: "100%",
                objectFit: "contain",
                objectPosition: "center",
              }}
            />
          </div>
        </div>
        <div
          style={{
            marginTop: "8px",
            fontWeight: "500",
            color: "#8e8e8e",
            fontSize: "14px",
            paddingRight: "10px",
          }}
        >
          © 2024 STAR FROM WAD
        </div>
      </div>
    </div>
  );
};
export default SuggestionPeopleOnNewFeed;
