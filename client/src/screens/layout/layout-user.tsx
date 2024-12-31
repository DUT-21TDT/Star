import { Outlet } from "react-router-dom";
import { useAppSelector } from "../../redux/store/hook";
import NotPermitted from "../error/not-permitted";
import VerifyEmail from "../auth/VerifyEmail";
import MiniSidebar from "../../components/user/layout-user/mini-sidebar";
import Sidebar from "../../components/user/layout-user/sidebar.tsx";
import optionPin from "../../utils/optionPin.ts";
import SuggestionPeopleOnNewFeed from "../../components/user/suggestion/suggestion-people.tsx";

const LayoutUser: React.FC = () => {
  const isUserRoute = window.location.pathname.startsWith(
    `${import.meta.env.VITE_BASE_URL}`
  );
  const { role, status, pin } = useAppSelector((state) => state.user);

  const isPinnedRoom = pin?.includes(optionPin.ROOM);
  const isPinnedProfile = pin?.includes(optionPin.PROFILE);
  const isPinnedPeople = pin?.includes(optionPin.PEOPLE);
  const isPinnedActivity = pin?.includes(optionPin.ACTIVITY);

  const isHasPin =
    isPinnedRoom || isPinnedProfile || isPinnedPeople || isPinnedActivity;

  if (status === "INACTIVE") {
    return <VerifyEmail />;
  }
  return (
    <>
      {isUserRoute && role === "USER" && (
        <div
          className="flex gap-5 bg-[#fafafa]"
          style={{ minHeight: "100vh", maxHeight: "100%", width: "100%" }}
        >
          <div className="sidebar">
            {pin && pin.length === 0 ? <Sidebar /> : <MiniSidebar />}
          </div>
          <div className="mini-sidebar">
            <MiniSidebar />
          </div>
          <div
            className="flex gap-2 bg-[#fafafa]"
            style={{
              width: "calc(100% - 200px)",
              marginLeft: "300px",
              paddingRight: !isHasPin ? "100px" : "0px",
            }}
          >
            <Outlet />
            <SuggestionPeopleOnNewFeed />
          </div>
        </div>
      )}
      {isUserRoute && role === "ADMIN" && <NotPermitted />}
    </>
  );
};

export default LayoutUser;
