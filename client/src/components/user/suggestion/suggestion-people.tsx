import { Avatar } from "antd";
import { useAppDispatch, useAppSelector } from "../../../redux/store/hook";
import { useGetListSuggestion } from "../../../hooks/suggestion";
import SuggestionItem from "./suggestion-item";
import { useNavigate } from "react-router-dom";
import { endSession, revokeToken } from "../../../service/userAPI";
import Cookies from "js-cookie";
import { removeInformationUser } from "../../../redux/slice/user-slice";
import default_avatar from "../../../assets/images/default_image.jpg";
import optionPin from "../../../utils/optionPin";
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

const FooterLinks = () => {
  const linkStyle = {
    textDecoration: "none",
    fontWeight: "500",
    color: "#8e8e8e",
    fontSize: "14px",
  };

  const spanStyle = {
    margin: "0 5px",
    color: "#aaa",
  };

  return (
    <div
      style={{
        textAlign: "left",
        fontSize: "12px",
        color: "#aaa",
        lineHeight: "1.5",
        width: "400px",
        wordWrap: "break-word",
        marginTop: "12px",
        marginBottom: "12px",
        paddingLeft: "4px",
      }}
    >
      <a href="#" style={linkStyle}>
        About
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Help
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Press
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        API
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Jobs
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Privacy
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Terms
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Locations
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Language
      </a>
      <span style={spanStyle}>·</span>
      <a href="#" style={linkStyle}>
        Contact
      </a>
    </div>
  );
};

const SuggestionPeopleOnNewFeed = () => {
  const { id, username, avatarUrl, firstName, lastName, pin } = useAppSelector(
    (state) => state.user
  );
  const { data } = useGetListSuggestion();
  const navigate = useNavigate();

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

  const handleNavigateProfileUser = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (id) {
      navigate(`/profile/${id}`);
      window.scrollTo(0, 0);
    }
  };

  const isPinnedRoom = pin?.includes(optionPin.ROOM);
  const isPinnedProfile = pin?.includes(optionPin.PROFILE);
  const isPinnedPeople = pin?.includes(optionPin.PEOPLE);
  const isPinnedActivity = pin?.includes(optionPin.ACTIVITY);

  const isHasPin =
    isPinnedRoom || isPinnedProfile || isPinnedPeople || isPinnedActivity;

  const isHomepage =
    window.location.pathname === "/" ||
    window.location.pathname === "/following" ||
    window.location.pathname === "/liked";

  if (isHasPin && isHomepage) {
    return;
  }
  return (
    <div>
      <div
        className="w-[400px] h-[fit-content] bg-white"
        style={{
          marginTop: "55px",
          border: "1px solid #dbdbdb",
          borderRadius: "20px",
          padding: "15px 15px 0px 15px",
          marginRight: "20px",
        }}
      >
        <div className="flex items-center justify-between">
          {/* User */}
          <div
            className="flex items-center gap-3 cursor-pointer"
            onClick={handleNavigateProfileUser}
          >
            <Avatar src={avatarUrl || default_avatar} size={45} />
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
                {firstName || lastName ? (
                  <div className="text-[14px] text-gray-500">
                    {`${firstName || ""} ${lastName || ""}`}
                  </div>
                ) : (
                  <div className="text-[14px] text-gray-500">{username}</div>
                )}
              </p>
            </div>
          </div>
          {/* Switch */}
          <p
            style={{
              fontSize: "14px",
              fontWeight: 500,
              color: "#ff3040",
              cursor: "pointer",
            }}
            onClick={handleLogout}
          >
            Log out
          </p>
        </div>
        <div className="flex items-center justify-between py-1 mt-5">
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
              navigate("/search");
            }}
          >
            See all
          </div>
        </div>
        <div className="py-3 space-y-2">
          {data &&
            data.map((item: ISuggestionItem) => <SuggestionItem data={item} />)}
        </div>
      </div>

      <FooterLinks />
      <div
        style={{
          marginTop: "8px",
          fontWeight: "500",
          color: "#8e8e8e",
          fontSize: "14px",
          paddingRight: "10px",
          paddingLeft: "4px",
        }}
      >
        © 2024 STAR FROM WAD
      </div>
    </div>
  );
};
export default SuggestionPeopleOnNewFeed;
