import { useAppSelector } from "../../redux/store/hook";
import NotPermitted from "../error/not-permitted";

const LayoutUser: React.FC = () => {
  const isUserRoute = window.location.pathname.startsWith(
    `${import.meta.env.VITE_BASE_URL}`
  );
  const role = useAppSelector((state) => state.user.role);
  return (
    <>
      {isUserRoute && role === "USER" ? (
        <div>This is a layout user</div>
      ) : (
        <NotPermitted />
      )}
    </>
  );
};

export default LayoutUser;
