import React, { forwardRef, Ref } from "react";
import { Tabs } from "antd";
import type { TabsProps } from "antd";
import PostOnWall from "./posts/posts-on-wall";
import PendingPostOnWall from "./posts/pending-posts-on-wall";
import RepliesOnWall from "./posts/replies-on-wall";
import RepostsOnWall from "./posts/reposts-on-wall.tsx";

const onChange = (key: string) => {
  console.log(key);
};

interface IProps {
  isCurrentUser: boolean;
  userId: string;
  scrollRef: Ref<HTMLDivElement>;
}

const TabProfile: React.FC<IProps> = forwardRef((props) => {
  const { isCurrentUser, userId, scrollRef } = props;
  const items: TabsProps["items"] = [
    {
      key: "1",
      label: <div className="text-[16px] font-semibold">Posts</div>,
      children: (
        <PostOnWall
          isCurrentUser={isCurrentUser}
          userId={userId}
          scrollRef={scrollRef}
        />
      ),
    },
    {
      key: "2",
      label: <div className="text-[16px] font-semibold">Replies</div>,
      children: <RepliesOnWall userId={userId} scrollRef={scrollRef} />,
    },
    {
      key: "3",
      label: <div className="text-[16px] font-semibold">Reposts</div>,
      children: <RepostsOnWall userId={userId} scrollRef={scrollRef} />,
    },
  ];
  if (isCurrentUser) {
    items.push({
      key: "4",
      label: <div className="text-[16px] font-semibold">Pending</div>,
      children: (
        <PendingPostOnWall
          isCurrentUser={isCurrentUser}
          scrollRef={scrollRef}
        />
      ),
    });
  }

  const tabClass = items.length === 4 ? "four-items" : "three-items";

  return (
    <Tabs
      defaultActiveKey="1"
      items={items}
      onChange={onChange}
      tabBarStyle={{
        display: "flex",
      }}
      moreIcon={null}
      centered={true}
      className={tabClass}
      destroyInactiveTabPane={true}
    />
  );
});

export default TabProfile;
