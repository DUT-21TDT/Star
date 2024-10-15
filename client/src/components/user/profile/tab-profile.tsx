import React from "react";
import { Tabs } from "antd";
import type { TabsProps } from "antd";
import PostOnWall from "./posts/posts-on-wall";

const onChange = (key: string) => {
  console.log(key);
};

const items: TabsProps["items"] = [
  {
    key: "1",
    label: <div className="text-[16px]  font-semibold">Posts</div>,
    children: <PostOnWall />,
  },
  {
    key: "2",
    label: <div className="text-[16px]  font-semibold">Replies</div>,
    children: "Content of Tab Pane 2",
  },
  {
    key: "3",
    label: <div className="text-[16px]  font-semibold">Reposts</div>,
    children: "Content of Tab Pane 3",
  },
];

const TabProfile: React.FC = () => {
  return (
    <Tabs
      defaultActiveKey="1"
      items={items}
      onChange={onChange}
      tabBarStyle={{
        display: "flex",
      }}
      moreIcon={null}
    />
  );
};

export default TabProfile;
