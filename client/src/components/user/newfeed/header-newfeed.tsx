import { Button, Dropdown } from "antd";
import { DownOutlined, EllipsisOutlined } from "@ant-design/icons";
import type { MenuProps } from "antd";
import { useState } from "react";

const HeaderNewFeed = () => {
  const [itemActive, setItemActive] = useState("For you");

  const menuItems: MenuProps["items"] = [
    { key: "1", label: "For you" },
    { key: "2", label: "Following" },
    { key: "3", label: "Like" },
    { key: "4", label: "Save" },
  ].map(({ key, label }) => ({
    key,
    label: (
      <div
        className="w-[200px] h-[40px] flex items-center text-[16px] font-semibold"
        onClick={() => setItemActive(label)}
      >
        {label}
      </div>
    ),
  }));

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
          <Dropdown menu={{ items: menuItems }} placement="bottom">
            <DownOutlined style={{ fontSize: "14px" }} />
          </Dropdown>
        </div>
      </div>
      <div style={{ flexGrow: 1 }}>
        <Button
          icon={<EllipsisOutlined />}
          style={{ borderRadius: "50%", width: "25px", height: "25px" }}
        />
      </div>
    </div>
  );
};

export default HeaderNewFeed;
