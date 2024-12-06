import { Tag, Button, Input } from "antd";
import { useState } from "react";
import { PlusOutlined, SearchOutlined } from "@ant-design/icons";
import ModalCreateRoom from "./modal-create-room";

interface IProps {
  countRoom: number;
  setTextSearchRoom: (text: string) => void;
}
const HeaderTableRoom = (props: IProps) => {
  const [openModal, setOpenModal] = useState<boolean>(false);

  const { countRoom, setTextSearchRoom } = props;

  return (
    <div className="flex justify-between items-center gap-5">
      <div className="flex justify-start items-center gap-1 w-[20%] ">
        <div className="text-[22px] font-semibold">Total Rooms</div>
        <Tag color="purple" bordered style={{ borderRadius: "10px" }}>
          {countRoom} rooms
        </Tag>
      </div>
      <Input
        placeholder="Search"
        prefix={<SearchOutlined className="text-[#b9b8b8]" />}
        className="w-[60%] mr-[20px]"
        onChange={(e) => setTextSearchRoom(e.target.value)}
      />
      <div className="w-[20%] flex justify-end">
        <Button
          type="primary"
          onClick={() => setOpenModal(true)}
          icon={<PlusOutlined />}
        >
          Add new
        </Button>
        <ModalCreateRoom openModal={openModal} setOpenModal={setOpenModal} />
      </div>
    </div>
  );
};
export default HeaderTableRoom;
