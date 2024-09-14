import { BarsOutlined } from "@ant-design/icons";
import { Avatar, Button, Input, Layout, Dropdown } from "antd";
import { SearchOutlined, BellOutlined } from "@ant-design/icons";
import react from "../../../assets/images/QR.svg";
import type { MenuProps } from "antd";
import Cookies from "js-cookie";
import { endSession, revokeToken } from "../../../service/userAPI";
import { useAppDispatch } from "../../../redux/store/hook";
import { removeInformationUser } from "../../../redux/slice/user-slice";

interface IProps {
  collapsed: boolean;
  setCollapsed: (collapsed: boolean) => void;
}

const { Header } = Layout;
const HeaderAdmin = (props: IProps) => {
  const { collapsed, setCollapsed } = props;
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
  const items: MenuProps["items"] = [
    {
      key: "1",
      label: <div onClick={handleLogout}>Log out</div>,
    },
  ];

  return (
    <Header style={{ padding: 0, background: "#f5f5f5" }}>
      <div className="flex justify-between items-center p-[20px] h-[64px]">
        <Button
          type="text"
          icon={<BarsOutlined />}
          onClick={() => setCollapsed(!collapsed)}
          style={{
            fontSize: "16px",
            width: 48,
            height: 48,
            flex: 0.3,
          }}
        />
        <Input
          placeholder="Search"
          prefix={<SearchOutlined className="text-[#b9b8b8]" />}
          style={{
            flex: 3,
          }}
        />
        <div className="flex justify-center gap-3 flex-1">
          <Button icon={<BellOutlined />} className="border-none" />
          <Dropdown menu={{ items }} placement="bottomRight">
            <Button
              icon={
                <Avatar
                  shape="square"
                  size={32}
                  src={react}
                  style={{
                    cursor: "pointer",
                    border: "1px solid #f0f0f0",
                  }}
                />
              }
            ></Button>
          </Dropdown>
        </div>
      </div>
    </Header>
  );
};

export default HeaderAdmin;
