import { Dropdown, Menu } from "antd";
import { DownOutlined } from "@ant-design/icons";

import "../../../assets/css/header-newfeed.css";

interface IProps {
  itemActive: string;
  menuItems: { key: string; label: JSX.Element; children?: JSX.Element[] }[];
}

const HeaderNewFeed: React.FC<IProps> = ({ itemActive, menuItems }) => {
  return (
    <div className="flex">
      <div style={{ flexGrow: 1 }}></div>
      <div
        className="flex items-center justify-center gap-[10px]"
        style={{ flexGrow: 4, textAlign: "center", fontWeight: 500 }}
      >
        <span>{itemActive}</span>
        <div
          className="flex items-center justify-center border border-[#ccc] rounded-full"
          style={{ width: "20px", height: "20px" }}
        >
          <Dropdown
            overlay={<Menu items={menuItems} />}
            placement="bottom"
            trigger={["click"]}
          >
            <DownOutlined style={{ fontSize: "11px" }} />
          </Dropdown>
        </div>
      </div>
      <div style={{ flexGrow: 1 }}></div>
    </div>
  );
};

export default HeaderNewFeed;
