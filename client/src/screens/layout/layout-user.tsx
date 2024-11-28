import { Outlet } from "react-router-dom";
import SideBar from "../../components/user/layout-user/sidebar";
import { useAppSelector } from "../../redux/store/hook";
import NotPermitted from "../error/not-permitted";
import VerifyEmail from "../auth/VerifyEmail";

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
        <div
          className="flex gap-5 bg-[#fafafa] "
          style={{
            minHeight: "100vh",
            maxHeight: "100%",
          }}
        >
          <SideBar />
          <Outlet />
        </div>
      )}
      {isUserRoute && role === "ADMIN" && <NotPermitted />}
    </>
  );
};

export default LayoutUser;
