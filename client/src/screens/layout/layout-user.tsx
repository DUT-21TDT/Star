import { Outlet } from "react-router-dom";
import { useAppSelector } from "../../redux/store/hook";
import NotPermitted from "../error/not-permitted";
import VerifyEmail from "../auth/VerifyEmail";
import MiniSidebar from "../../components/user/layout-user/mini-sidebar";
import Sidebar from "../../components/user/layout-user/sidebar.tsx";

const LayoutUser: React.FC = () => {
  const isUserRoute = window.location.pathname.startsWith(
    `${import.meta.env.VITE_BASE_URL}`
  );
  const { role, status } = useAppSelector((state) => state.user);

  if (status === "INACTIVE") {
    return <VerifyEmail />;
  }
  return (
    <>
      {isUserRoute && role === "USER" && (
        <div className="flex gap-5 bg-[#fafafa]" style={{minHeight: "100vh", maxHeight: "100%"}}>
          <div className="sidebar">
            <Sidebar/>
          </div>
          <div className="mini-sidebar">
            <MiniSidebar/>
          </div>
          <Outlet/>
        </div>
      )}
      {isUserRoute && role === "ADMIN" && <NotPermitted/>}
    </>
  );
};

export default LayoutUser;
