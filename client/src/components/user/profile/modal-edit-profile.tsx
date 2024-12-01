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
  Select,
} from "antd";
import { LockOutlined, PlusOutlined, UserOutlined } from "@ant-design/icons";
import {
  useEditProfile,
  useGetPersonalInformation,
  useGetPresignedUrl,
} from "../../../hooks/user";
import { useAppDispatch, useAppSelector } from "../../../redux/store/hook";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { storeInformationUser } from "../../../redux/slice/user-slice";

import type { GetProp, UploadProps } from "antd";

type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];

dayjs.extend(customParseFormat);

interface IProps {
  openModal: boolean;
  setOpenModal: (value: boolean) => void;
}

const ModalEditProfile: React.FC<IProps> = ({ openModal, setOpenModal }) => {
  const { TextArea } = Input;
  const { Option } = Select;
  const id = useAppSelector((state) => state.user.id) || "";
  const { data, refetch } = useGetPersonalInformation();
  const { mutate: getPresignedURL } = useGetPresignedUrl();
  const { mutate: updateProfile } = useEditProfile();

  // Form and State
  const [form] = Form.useForm();
  const [privateProfile, setPrivateProfile] = useState(false);
  const [avatarUrlFile, setAvatarUrlFile] = useState<File | null>(null);
  const [avatarUrlBase64, setAvatarUrlBase64] = useState("");
  const [loading, setLoading] = useState(false);
  const [avatarFileName, setAvatarFileName] = useState<string>();
  const queryClient = useQueryClient();

  const dispatch = useAppDispatch();

  // Memoized initial values to avoid re-rendering
  const initialValues = useMemo(() => {
    if (!data) return {};
    setAvatarFileName(data.avatarUrl ? data.avatarUrl.split("/").pop() : "");
    return {
      username: data.username,
      firstName: data.firstName,
      lastName: data.lastName,
      bio: data.bio,
      email: data.email,
      gender: data.gender,
      dateofbirth: data.dateOfBirth
        ? dayjs(data.dateOfBirth, "YYYY/MM/DD")
        : null,
      registerDate: dayjs(data.registerAt, "YYYY/MM/DD"),
    };
  }, [data]);

  const beforeUpload = (file: FileType) => {
    const fileType = ["image/jpeg", "image/png", "image/jpg"];
    const isJpgOrPng = fileType.includes(file.type);
    if (!isJpgOrPng) {
      message.error("You can only upload JPG/PNG/JPEG file!");
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
      message.error("Image must smaller than 2MB!");
    }
    return isJpgOrPng && isLt2M;
  };

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
      setAvatarUrlBase64(base64String as string);
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
  const handleOk = async () => {
    setLoading(true);
    const formValues = form.getFieldsValue();
    const dataEdit = {
      username: formValues.username,
      firstName: formValues.firstName,
      lastName: formValues.lastName,
      bio: formValues.bio,
      email: formValues.email,
      avatarFileName: avatarFileName ? `avatar/${avatarFileName}` : "",
      dateOfBirth: formValues?.dateofbirth?.format("DD/MM/YYYY") || null,
      gender: formValues.gender,
      privateProfile,
    };
    if (!dataEdit.username) {
      message.error("Username is required");
      setLoading(false);
      return;
    }
    try {
      if (avatarUrlFile) {
        const fileExtension = avatarUrlFile.name.split(".").pop();
        const getDateTime = new Date().getTime();
        getPresignedURL(`avatar/${id}${getDateTime}.${fileExtension}`, {
          onSuccess: async (url) => {
            const uploadSuccess = await handleUploadAvatarToCloud(url);
            if (uploadSuccess) {
              dataEdit.avatarFileName = `avatar/${id}${getDateTime}.${fileExtension}`;
              updateProfile(dataEdit, {
                onSuccess: (result) => {
                  message.success("Profile updated successfully");
                  queryClient.invalidateQueries({
                    queryKey: QUERY_KEY.getProfileUser(id),
                  });
                  dispatch(storeInformationUser(result));
                  setOpenModal(false);
                },
                onError: () => {
                  message.error("Profile update failed");
                },
              });
            } else {
              message.error("Upload avatar failed");
            }
            setLoading(false);
          },
          onError: () => {
            message.error("Error fetching presigned URL for upload");
            setLoading(false);
          },
        });
      } else {
        updateProfile(dataEdit, {
          onSuccess: (result) => {
            message.success("Profile updated successfully");
            queryClient.invalidateQueries({
              queryKey: QUERY_KEY.getProfileUser(id),
            });
            dispatch(storeInformationUser(result));
            setOpenModal(false);
          },
          onError: () => {
            message.error("Profile update failed");
          },
        });
        setLoading(false);
      }
    } catch (error) {
      console.error("Error updating profile:", error);
      setLoading(false);
    }
  };

  useEffect(() => {
    if (openModal) {
      refetch();
      form.setFieldsValue(initialValues);
      setPrivateProfile(data.privateProfile);
    }
    return () => {
      setAvatarUrlBase64("");
    };
  }, [openModal, refetch]);

  useEffect(() => {
    if (data) {
      form.setFieldsValue(initialValues);
      setPrivateProfile(data.privateProfile);
    }
    return () => {
      setAvatarUrlBase64("");
    };
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
          loading={loading}
          iconPosition="start"
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
            <Avatar
              size={64}
              src={avatarUrlBase64 || data?.avatarUrl || undefined}
              icon={
                !avatarUrlBase64 && !data?.avatarUrl ? (
                  <UserOutlined />
                ) : undefined
              }
            />

            <Upload
              showUploadList={false}
              onChange={handleUploadChange}
              beforeUpload={beforeUpload}
            >
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
            <Select placeholder="Select gender">
              <Option value="MALE">Male</Option>
              <Option value="FEMALE">Female</Option>
            </Select>
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
