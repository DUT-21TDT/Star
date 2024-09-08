import { Button } from "antd";
import Cookies from 'js-cookie';
import { logOut } from "../service/userAPI";

const handleLogout = async () => {
  await logOut();
  Cookies.remove("JSESSIONID");
  Cookies.remove("id_token");
  Cookies.remove("refresh_token");
  Cookies.remove("access_token");
  window.location.href = "/login";
};

const LayoutUser = () => {
  return (
    <div className="app-container ">
      this is layout user and home page user
      {
        Cookies.get("access_token") &&
        <Button onClick={() => handleLogout()}>Logout</Button>
      }
    </div>
  );
};
const LayoutAdmin = () => {

  return (
    <div className="app-container ">
      this is layout admin and home page admin
      {
        Cookies.get("access_token") &&
        <Button onClick={() => handleLogout()}>Logout</Button>
      }
    </div>
  );
};
export { LayoutUser, LayoutAdmin };
