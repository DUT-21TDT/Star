import { Modal, Form, Input, message } from "antd";
import { useEffect, useState } from "react";
import { useEditRoom } from "../../../hooks/room";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { AxiosError } from "axios";
interface DataType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: string;
  participantsCount: number;
}
interface IProps {
  openModalEdit: boolean;
  setOpenModalEdit: React.Dispatch<React.SetStateAction<boolean>>;
  data: DataType | null;
}

const ModalEditRoom: React.FC<IProps> = (props) => {
  const { openModalEdit, setOpenModalEdit, data } = props;
  const [confirmLoading, setConfirmLoading] = useState<boolean>(false);
  const { mutate: editRoom } = useEditRoom();
  const [form] = Form.useForm();
  const queryClient = useQueryClient();
  const handleOk = () => {
    setConfirmLoading(true);

    const dataToEdit = {
      id: data?.id.toString(),
      name: form.getFieldValue("name"),
      description: form.getFieldValue("description"),
    };
    if (!dataToEdit.name) {
      message.error("Room name is required");
      setConfirmLoading(false);
      return;
    }
    editRoom(dataToEdit, {
      onSuccess: () => {
        message.success("Edit a room successfully");
        form.resetFields();
        queryClient.invalidateQueries({ queryKey: QUERY_KEY.fetchAllRoom() });
        setConfirmLoading(false);
        setOpenModalEdit(false);
      },
      onError: (error: Error) => {
        const axiosError = error as AxiosError;
        const errorMessage =
          axiosError?.response?.status === 409
            ? `Room "${dataToEdit.name}" already exists`
            : "Something went wrong";

        message.error(errorMessage);
        setConfirmLoading(false);
      },
    });
  };
  const { TextArea } = Input;

  useEffect(() => {
    form.setFieldValue("name", data?.name);
    form.setFieldValue("description", data?.description);
  }, [data, form]);
  return (
    <>
      <Modal
        title="Edit Room"
        open={openModalEdit}
        onOk={handleOk}
        confirmLoading={confirmLoading}
        onCancel={() => {
          setOpenModalEdit(false);
          form.resetFields();
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
export default ModalEditRoom;
