import { Tag, Button, Modal, Form, Input, message } from "antd";
import { useState } from "react";
import { PlusOutlined } from "@ant-design/icons";
import { useCreateRoom } from "../../../hooks/room";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useQueryClient } from "@tanstack/react-query";
import { AxiosError } from "axios";

interface IProps {
  countRoom: number;
}
const HeaderTableRoom = (props: IProps) => {
  const [openModal, setOpenModal] = useState<boolean>(false);
  const [confirmLoading, setConfirmLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const { TextArea } = Input;
  const { countRoom } = props;
  const { mutate: postCreateRoom } = useCreateRoom();
  const queryClient = useQueryClient();

  const handleOk = () => {
    setConfirmLoading(true);
    const dataToPost = {
      name: form.getFieldValue("name"),
      description: form.getFieldValue("description"),
    };
    if (!dataToPost.name) {
      message.error("Room name is required");
      setConfirmLoading(false);
      return;
    }
    postCreateRoom(dataToPost, {
      onSuccess: () => {
        message.success("Create a room successfully");
        form.resetFields();
        queryClient.invalidateQueries({ queryKey: QUERY_KEY.fetchAllRoom() });
        setConfirmLoading(false);
        setOpenModal(false);
      },
      onError: (error: Error) => {
        const axiosError = error as AxiosError;

        const errorMessage =
          axiosError?.response?.status === 409
            ? `Room "${dataToPost.name}" already exists`
            : "Something went wrong";

        message.error(errorMessage);
        setConfirmLoading(false);
      },
    });
  };

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
        <Modal
          title="Add new room"
          open={openModal}
          onOk={handleOk}
          confirmLoading={confirmLoading}
          onCancel={() => setOpenModal(false)}
        >
          <Form form={form} layout="vertical" autoComplete="off">
            <Form.Item name="name" label="Room name">
              <Input />
            </Form.Item>
            <Form.Item name="description" label="Description">
              <TextArea
                style={{
                  height: "100px",
                }}
                showCount
                maxLength={200}
              />
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </div>
  );
};
export default HeaderTableRoom;
