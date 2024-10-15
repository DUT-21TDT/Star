import { Tag, Button } from "antd";
import { useState } from "react";
import { PlusOutlined } from "@ant-design/icons";
import ModalCreateRoom from "./modal-create-room";

interface IProps {
  countRoom: number;
}
const HeaderTableRoom = (props: IProps) => {
  const [openModal, setOpenModal] = useState<boolean>(false);

  const { countRoom } = props;

  return (
    <div className="flex justify-between items-center gap-2">
      <div className="flex  justify-start items-center gap-1">
        <div className="text-[22px] font-semibold">Total Rooms</div>
        <Tag color="purple" bordered style={{ borderRadius: "10px" }}>
          {countRoom} rooms
        </Tag>
      </div>

      <div>
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
