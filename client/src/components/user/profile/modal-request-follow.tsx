import { Modal, Tabs, TabsProps } from "antd";
import "../../../assets/css/modal-request-follow.css";
import TabFollowers from "./tab-followers";
import { useEffect, useState } from "react";

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
  const [countFollower, setCountFollower] = useState<number>(0);
  const items: TabsProps["items"] = [
    {
      key: "1",
      label: (
        <span className="text-[18px] font-semibold ">
          Followers ({countFollower})
        </span>
      ),

      children: (
        <TabFollowers
          userId={userId}
          setCountFollower={setCountFollower}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
        />
      ),
    },
    {
      key: "2",
      label: <span className="text-[18px] font-semibold ">Following</span>,
      children: "Content of Tab Pane 2",
    },
  ];
  if (isCurrentUser) {
    items.push({
      key: "3",
      label: <span className="text-[18px] font-semibold ">Request</span>,
      children: <div>request</div>,
    });
  }

  const onChange = (key: string) => {
    console.log(key);
  };

  const handleOk = () => {
    setIsOpenModalRequestFollow(false);
  };

  const tabClass = items.length === 3 ? "modal-three-items" : "modal-two-items";

  return (
    <Modal
      open={isOpenModalRequestFollow}
      onOk={handleOk}
      onCancel={() => setIsOpenModalRequestFollow(false)}
      className="modalRequestFollow"
      width={520}
      footer={null}
      closeIcon={null}
    >
      <Tabs
        defaultActiveKey="1"
        items={items}
        onChange={onChange}
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
