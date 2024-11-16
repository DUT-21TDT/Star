import { message, Modal, Tabs, TabsProps } from "antd";
import "../../../assets/css/modal-request-follow.css";
import TabFollowers from "./tab-followers";
import TabFollowing from "./tab-following";
import TabRequested from "./tab-requested";
import { useEffect, useState } from "react";
import { getCountFollowerByUserId } from "../../../service/followAPI";

interface IProps {
  isOpenModalRequestFollow: boolean;
  setIsOpenModalRequestFollow: (value: boolean) => void;
  isCurrentUser: boolean;
  userId: string;
}
const ModalRequestFollow: React.FC<IProps> = (props) => {
  const {
    isCurrentUser,
    isOpenModalRequestFollow,
    setIsOpenModalRequestFollow,
    userId,
  } = props;

  const items: TabsProps["items"] = [
    {
      key: "1",
      label: <span className="text-[18px] font-semibold ">Followers</span>,

      children: (
        <TabFollowers
          userId={userId}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
        />
      ),
    },
    {
      key: "2",
      label: <span className="text-[18px] font-semibold ">Following</span>,
      children: (
        <TabFollowing
          userId={userId}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
        />
      ),
    },
  ];
  if (isCurrentUser) {
    items.push({
      key: "3",
      label: <span className="text-[18px] font-semibold ">Request</span>,
      children: (
        <TabRequested
          userId={userId}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
        />
      ),
    });
  }

  const handleOk = () => {
    setIsOpenModalRequestFollow(false);
  };

  const tabClass = items.length === 3 ? "modal-three-items" : "modal-two-items";

  const [countFollow, setCountFollow] = useState<object>({});
  const fetchCount = async () => {
    try {
      const response = await getCountFollowerByUserId(userId);
      console.log("check response", response);
    } catch (error) {
      console.error("Error fetching count follower:", error);
      message.error("Error fetching count follower");
    }
  };

  useEffect(() => {
    fetchCount();
  }, []);

  return (
    <Modal
      open={isOpenModalRequestFollow}
      onOk={handleOk}
      onCancel={() => setIsOpenModalRequestFollow(false)}
      className="modalRequestFollow"
      width={520}
      footer={null}
      closeIcon={null}
      destroyOnClose={true}
    >
      <Tabs
        defaultActiveKey="1"
        items={items}
        tabBarStyle={{
          display: "flex",
        }}
        className={tabClass}
        moreIcon={null}
        centered={true}
        destroyInactiveTabPane={true}
      />
    </Modal>
  );
};
export default ModalRequestFollow;
