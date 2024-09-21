import React, { useEffect, useState, useCallback, useMemo } from "react";
import {
  Modal,
  Input,
  Switch,
  Button,
  DatePicker,
  Form,
  Avatar,
  Upload,
  message,
} from "antd";
import { LockOutlined, PlusOutlined, UserOutlined } from "@ant-design/icons";
import {
  useEditProfile,
  useGetPersonalInformation,
  useGetPresignedUrl,
} from "../../../hooks/user";
import { useAppSelector } from "../../../redux/store/hook";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";

dayjs.extend(customParseFormat);

interface IProps {
  openModal: boolean;
  setOpenModal: (value: boolean) => void;
}

const ModalEditProfile: React.FC<IProps> = ({ openModal, setOpenModal }) => {
  const { TextArea } = Input;
  const id = useAppSelector((state) => state.user.id);
  const { data } = useGetPersonalInformation(id);
  const { mutate: getPresignedURL } = useGetPresignedUrl();
  const { mutate: updateProfile } = useEditProfile();

  // Form and State
  const [form] = Form.useForm();
  const [privateProfile, setPrivateProfile] = useState(false);
  const [avatarUrlFile, setAvatarUrlFile] = useState<File | null>(null);
  const [avatarUrl, setAvatarUrl] = useState("");
  const queryClient = useQueryClient();

  // Memoized initial values to avoid re-rendering
  const initialValues = useMemo(() => {
    if (!data) return {};
    return {
      username: data.username,
      firstName: data.firstName,
      lastName: data.lastName,
      bio: data.bio,
      email: data.email,
      gender: data.gender,
      dateofbirth: dayjs(data.dateOfBirth, "YYYY/MM/DD"),
      registerDate: dayjs(data.registerAt, "YYYY/MM/DD"),
    };
  }, [data]);

  // Convert file to Base64
  const convertFileToBase64 = useCallback((file: File) => {
    return new Promise<string | ArrayBuffer | null>((resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }, []);

  const handleUploadChange = useCallback(
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    async ({ file }: any) => {
      setAvatarUrlFile(file.originFileObj);
      const base64String = await convertFileToBase64(file.originFileObj);
      setAvatarUrl(base64String as string);
    },
    [convertFileToBase64]
  );

  // Upload avatar to cloud
  const handleUploadAvatarToCloud = useCallback(
    async (url: string) => {
      try {
        await fetch(url, {
          method: "PUT",
          body: avatarUrlFile,
        });
        return true;
      } catch (error) {
        console.error("Error uploading avatar:", error);
        return false;
      }
    },
    [avatarUrlFile]
  );

  // Handle modal "Done" click
  const handleOk = useCallback(async () => {
    const formValues = form.getFieldsValue();
    const dataEdit = {
      username: formValues.username,
      firstName: formValues.firstName,
      lastName: formValues.lastName,
      bio: formValues.bio,
      email: formValues.email,
      avatarFileName: "",
      dateOfBirth: formValues.dateofbirth.format("DD/MM/YYYY"),
      gender: formValues.gender,
      privateProfile,
    };

    try {
      if (avatarUrlFile) {
        const fileExtension = avatarUrlFile.name.split(".").pop();
        getPresignedURL(`${id}.${fileExtension}`, {
          onSuccess: async (url) => {
            const uploadSuccess = await handleUploadAvatarToCloud(url);
            if (uploadSuccess) {
              dataEdit.avatarFileName = `${id}.${fileExtension}`;
              updateProfile(dataEdit, {
                onSuccess: () => {
                  message.success("Profile updated successfully");
                  queryClient.invalidateQueries({
                    queryKey: QUERY_KEY.getProfileUser(id),
                  });
                  setOpenModal(false);
                },
                onError: () => {
                  message.error("Profile update failed");
                },
              });
            } else {
              message.error("Upload avatar failed");
            }
          },
          onError: () => {
            message.error("Error fetching presigned URL for upload");
          },
        });
      } else {
        updateProfile(dataEdit, {
          onSuccess: () => {
            message.success("Profile updated successfully");
            queryClient.invalidateQueries({
              queryKey: QUERY_KEY.getProfileUser(id),
            });
            setOpenModal(false);
          },
          onError: () => {
            message.error("Profile update failed");
          },
        });
      }
    } catch (error) {
      console.error("Error updating profile:", error);
    }
  }, [
    avatarUrlFile,
    form,
    getPresignedURL,
    handleUploadAvatarToCloud,
    id,
    privateProfile,
    queryClient,
    setOpenModal,
    updateProfile,
  ]);

  useEffect(() => {
    if (data) {
      form.setFieldsValue(initialValues);
      setPrivateProfile(data.privateProfile);
    }
  }, [data, form, initialValues]);

  return (
    <Modal
      title="Edit Profile"
      open={openModal}
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
          key="done"
        >
          Done
        </Button>,
      ]}
      onCancel={() => setOpenModal(false)}
    >
      <Form form={form} layout="vertical" initialValues={initialValues}>
        <div className="flex items-center justify-between">
          <Form.Item name="email" label="Email" className="w-[80%]">
            <Input prefix={<LockOutlined />} disabled />
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
          <Form.Item name="username" label="Username" style={{ width: "50%" }}>
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
          <TextArea maxLength={255} />
        </Form.Item>

        <div className="flex items-center justify-between gap-4">
          <Form.Item
            name="firstName"
            label="First Name"
            style={{ width: "50%" }}
          >
            <Input placeholder="First name" />
          </Form.Item>

          <Form.Item name="lastName" label="Last Name" style={{ width: "50%" }}>
            <Input placeholder="Last name" />
          </Form.Item>
        </div>

        <div className="flex items-center justify-between gap-4">
          <Form.Item name="gender" label="Gender" style={{ width: "50%" }}>
            <Input placeholder="Gender" />
          </Form.Item>

          <Form.Item
            name="dateofbirth"
            label="Date of Birth"
            className="w-[50%]"
          >
            <DatePicker style={{ width: "100%" }} format="YYYY/MM/DD" />
          </Form.Item>
        </div>

        <Form.Item>
          <div className="flex justify-between items-center">
            <span className="text-[16px] font-semibold">Private profile</span>
            <Switch
              checked={privateProfile}
              onChange={() => setPrivateProfile(!privateProfile)}
            />
          </div>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default ModalEditProfile;
