import { Modal, Form, Input, message } from "antd";
import { useCreateRoom } from "../../../hooks/room";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useQueryClient } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useState } from "react";

interface IProps {
  openModal: boolean;
  setOpenModal: React.Dispatch<React.SetStateAction<boolean>>;
}
const ModalCreateRoom: React.FC<IProps> = (props) => {
  const { openModal, setOpenModal } = props;
  const [confirmLoading, setConfirmLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  const { TextArea } = Input;
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
        queryClient.invalidateQueries({
          queryKey: QUERY_KEY.fetchAllRoom(),
        });
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
    <>
      <Modal
        title="Add new room"
        open={openModal}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={() => {
          form.resetFields();
          setOpenModal(false);
        }}
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
    </>
  );
};
export default ModalCreateRoom;
