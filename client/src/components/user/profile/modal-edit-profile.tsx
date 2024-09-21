import React, { useState } from "react";
import {
  Modal,
  Input,
  Switch,
  Button,
  DatePicker,
  Form,
  Avatar,
  Upload,
} from "antd";
import { LockOutlined, PlusOutlined, UserOutlined } from "@ant-design/icons";
import type { GetProp, UploadProps } from "antd";

type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];

interface IProps {
  dataUser: {
    email: string;
    username: string;
    registerDate: string;
    bio: string;
    firstName: string;
    lastName: string;
  };
  openModal: boolean;
  setOpenModal: (value: boolean) => void;
}

const ModalEditProfile: React.FC<IProps> = () => {
  const { TextArea } = Input;
  const [form] = Form.useForm();
  const [privateProfile, setPrivateProfile] = useState(false);
  const [avatarUrlFile, setAvatarUrlFile] = useState(null);
  const [avatarUrl, setAvatarUrl] = useState("");

  console.log("check ava", avatarUrl);
  console.log("check file", avatarUrlFile);

  const convertFileToBase64 = (file: FileType) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => {
        resolve(reader.result);
      };
      reader.onerror = (error) => {
        reject(error);
      };
      reader.readAsDataURL(file);
    });
  };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const handleUploadChange = async ({ file }: any) => {
    setAvatarUrlFile(file.originFileObj);
    const base64String = await convertFileToBase64(file.originFileObj);
    setAvatarUrl(base64String as string);
  };

  const handleOk = () => {
    console.log(form.getFieldsValue());
  };

  return (
    <>
      <Modal
        title="Edit Profile"
        open={true}
        footer={[
          <Button
            type="primary"
            htmlType="submit"
            style={{
              marginTop: "1.5rem",
              width: "100%",
              height: "40px",
              backgroundColor: "black",
            }}
            onClick={handleOk}
          >
            Done
          </Button>,
        ]}
      >
        <Form form={form} layout="vertical">
          <div className="flex items-center justify-between">
            <Form.Item name="email" label="Email" className="w-[80%]">
              <Input
                prefix={<LockOutlined />}
                defaultValue="nguyenthuchoang17112003@gmail.com"
                disabled
              />
            </Form.Item>
            <div style={{ position: "relative" }}>
              <Avatar size={64} src={avatarUrl} icon={<UserOutlined />} />
              <Upload showUploadList={false} onChange={handleUploadChange}>
                <Button
                  icon={<PlusOutlined />}
                  style={{
                    position: "absolute",
                    bottom: "-5px",
                    right: "-5px",
                    borderRadius: "50%",
                    width: "30px",
                    height: "30px",
                    cursor: "pointer",
                    backgroundColor: "black",
                    color: "white",
                  }}
                ></Button>
              </Upload>
            </div>
          </div>

          <div className="flex items-center justify-between gap-4">
            <Form.Item
              name="username"
              label="Username"
              style={{ width: "50%" }}
              rules={[{ required: true, message: "Username is required" }]}
            >
              <Input placeholder="Username" />
            </Form.Item>

            <Form.Item
              name="registerDate"
              label="Register Date"
              style={{ width: "50%" }}
            >
              <DatePicker disabled style={{ width: "100%" }} />
            </Form.Item>
          </div>

          <Form.Item name="bio" label="Bio">
            <TextArea defaultValue="bio" maxLength={255} />
          </Form.Item>

          <div className="flex items-center justify-between gap-4 ">
            <Form.Item
              name="firstName"
              label="First Name"
              style={{ width: "50%" }}
            >
              <Input placeholder="First name" />
            </Form.Item>

            <Form.Item
              name="lastName"
              label="Last Name"
              style={{ width: "50%" }}
            >
              <Input placeholder="Last name" />
            </Form.Item>
          </div>

          <div className="flex items-center justify-between gap-4 ">
            <Form.Item name="gender" label="Gender" style={{ width: "50%" }}>
              <Input placeholder="Gender" />
            </Form.Item>

            <Form.Item
              label="Date of Birth"
              className="w-[50%]"
              name={"dateofbirth"}
            >
              <DatePicker
                style={{ width: "100%" }}
                format={{
                  format: "YYYY-MM-DD",
                }}
              />
            </Form.Item>
          </div>

          <Form.Item>
            <div
              style={{
                marginTop: "1rem",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
              }}
            >
              <span className="text-[16px] font-semibold ">
                Private profile
              </span>
              <Switch
                checked={privateProfile}
                onChange={() => setPrivateProfile(!privateProfile)}
              />
            </div>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default ModalEditProfile;
