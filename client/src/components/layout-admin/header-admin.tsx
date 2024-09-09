import { BarsOutlined } from "@ant-design/icons";
import { Avatar, Button, Input, Layout } from "antd";
import { SearchOutlined, BellOutlined } from "@ant-design/icons";
import react from "../../assets/images/QR.svg";
interface IProps {
  collapsed: boolean;
  setCollapsed: (collapsed: boolean) => void;
}

const { Header } = Layout;
const HeaderAdmin = (props: IProps) => {
  const { collapsed, setCollapsed } = props;
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
          <Avatar
            shape="square"
            size={32}
            src={react}
            style={{
              cursor: "pointer",
              border: "1px solid #f0f0f0",
            }}
          />
        </div>
      </div>
    </Header>
  );
};

export default HeaderAdmin;
