import React, { useState } from "react";
import { Avatar, Modal, Input, Button, Upload, message, Select } from "antd";
import type { GetProp, UploadFile, UploadProps } from "antd";
import { CloudUploadOutlined } from "@ant-design/icons";
import { useAppSelector } from "../../../../redux/store/hook";
import useEmblaCarousel from "embla-carousel-react";
import { PhotoProvider, PhotoView } from "react-photo-view";
import { useGetAllRoomForUser } from "../../../../hooks/room";
import { useCreateAPost, useGetAllPresignedUrl } from "../../../../hooks/post";
import "../../../../assets/css/modal-create-post.css";

type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];
type RoomType = {
  id: number;
  name: string;
};

const getBase64 = (file: FileType): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });

interface IProps {
  isModalOpen: boolean;
  setIsModalOpen: (value: boolean) => void;
}

const ModalCreatePost: React.FC<IProps> = ({ isModalOpen, setIsModalOpen }) => {
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [fileListBase64, setFileListBase64] = useState<string[]>([]);
  const [textValue, setTextValue] = useState<string>("");
  const [optionSelected, setOptionSelected] = useState<string>("");
  const [loading, setLoading] = useState(false);
  const [emblaRef] = useEmblaCarousel();

  const userData = useAppSelector((state) => state.user);
  const { listRoomJoined } = useGetAllRoomForUser();
  const { mutate: createAPost } = useCreateAPost();
  const { mutate: getPostPresignedURL } = useGetAllPresignedUrl();

  const handleChange: UploadProps["onChange"] = ({ fileList: newFileList }) => {
    if (!beforeUpload(newFileList[newFileList.length - 1] as FileType)) {
      return;
    }
    setFileList(newFileList);
    Promise.all(
      newFileList.map((file) => getBase64(file.originFileObj as FileType))
    ).then((base64List) => setFileListBase64(base64List as string[]));
  };

  const optionRoom = listRoomJoined.map((room: RoomType) => ({
    value: room.id,
    label: room.name,
  }));

  const handleOk = async () => {
    setLoading(true);
    if (!textValue && fileList.length === 0) {
      message.error("Please input content or upload an image");
      setLoading(false);
      return;
    }
    if (!optionSelected) {
      message.error("Please choose a room to post");
      setLoading(false);
      return;
    }

    const postContent = {
      roomId: optionSelected,
      content: textValue,
      imageFileNames:
        fileList.length > 0
          ? fileList.map((file) => `post/${file.uid}${file.name}`)
          : [],
    };

    if (fileList.length > 0) {
      getPostPresignedURL(postContent.imageFileNames, {
        onSuccess: async ({ successUrls }) => {
          await Promise.all(
            fileList.map((file, index) =>
              fetch(successUrls[index], {
                method: "PUT",
                body: file.originFileObj,
              })
            )
          );
          await createPost(postContent);
        },
        onError: () => {
          message.error("Create post failed. Please try again later");
          setLoading(false);
        },
      });
    } else {
      await createPost(postContent);
    }
  };

  const createPost = async (postContent: {
    roomId: string;
    content: string;
    imageFileNames: string[];
  }) => {
    createAPost(postContent);
    message.success("Create post successfully");
    resetModal();
  };

  const resetModal = () => {
    setTextValue("");
    setFileList([]);
    setFileListBase64([]);
    setOptionSelected("");
    setIsModalOpen(false);
    setLoading(false);
  };

  const handleCancel = () => resetModal();

  const ImageUpload = () => (
    <PhotoProvider maskOpacity={0.7}>
      {fileListBase64.map((base64, index) => (
        <div
          key={index}
          className="embla__slide"
          style={{
            flex: "0 0 auto",
            marginRight: "15px",
            alignItems: "center",
            justifyContent: "center",
            backgroundColor: "white",
            position: "relative",
          }}
        >
          <div
            style={{
              width: "30px",
              height: "30px",
              borderRadius: "50%",
              backgroundColor: "#767682",
              color: "white",
              fontSize: "20px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              position: "absolute",
              top: "5px",
              right: "5px",
              cursor: "pointer",
            }}
            onClick={() => handleDeleteImage(index)}
          >
            ✕
          </div>
          <PhotoView src={base64}>
            <img
              src={base64}
              alt={`upload-${index}`}
              style={{
                maxHeight: "480px",
                maxWidth: "350px",
                objectFit: "cover",
                borderRadius: "15px",
                border: "1px solid #ccc",
              }}
            />
          </PhotoView>
        </div>
      ))}
    </PhotoProvider>
  );

  const handleDeleteImage = (index: number) => {
    setFileList((prev) => prev.filter((_, i) => i !== index));
    setFileListBase64((prev) => prev.filter((_, i) => i !== index));
  };

  const beforeUpload = (file: FileType) => {
    const fileType = ["image/jpeg", "image/png", "image/jpg"];
    const isJpgOrPng = fileType.includes(file.type);
    if (!isJpgOrPng) {
      message.error("You can only upload JPG/PNG/JPEG file!");
    }
    const isLt10M = file.size / 1024 / 1024 < 10;
    if (!isLt10M) {
      message.error("Image must smaller than 10MB!");
    }
    return isJpgOrPng && isLt10M;
  };

  return (
    <Modal
      title={<p className="text-center text-[18px] font-bold">New Post</p>}
      open={isModalOpen}
      onOk={handleOk}
      onCancel={handleCancel}
      centered
      width={650}
      footer={null}
      destroyOnClose
    >
      <div className="flex items-start gap-5">
        <Avatar src={userData?.avatarUrl} size={50} />
        <div style={{ width: "calc(100% - 100px)" }}>
          <p className="text-[16px] font-semibold">{userData?.username}</p>
          <Input.TextArea
            placeholder="What's new?"
            autoSize={{ maxRows: 20 }}
            value={textValue}
            onChange={(e) => setTextValue(e.target.value)}
            style={{
              border: "none",
              fontSize: "16px",
              maxWidth: "500px",
              paddingLeft: "0px",
            }}
          />
          <div
            className="embla mt-3 mb-4"
            ref={emblaRef}
            style={{ overflow: "hidden", maxHeight: "400px" }}
          >
            <div className="embla__container" style={{ display: "flex" }}>
              <ImageUpload />
            </div>
          </div>
          <div className="flex items-center gap-5">
            <Upload
              fileList={fileList}
              onChange={handleChange}
              showUploadList={false}
            >
              <Button
                icon={<CloudUploadOutlined />}
                type="text"
                style={{ color: "gray", backgroundColor: "#f0f0f0" }}
              >
                Upload
              </Button>
            </Upload>
            <Select
              placeholder="Choose your room"
              onChange={(value) => setOptionSelected(value)}
              options={optionRoom}
              style={{ minWidth: "150px", height: "35px" }}
              showSearch
              filterOption={(input, option) =>
                (option?.label ?? "")
                  .toString()
                  .toLowerCase()
                  .includes(input.toLowerCase())
              }
              filterSort={(optionA, optionB) =>
                (optionA?.label ?? "")
                  .toString()
                  .toLowerCase()
                  .localeCompare(
                    (optionB?.label ?? "").toString().toLowerCase()
                  )
              }
            />
          </div>
        </div>
      </div>
      <div className="flex justify-end">
        <Button
          type="default"
          onClick={handleOk}
          loading={loading}
          style={{ color: "black", fontWeight: "500" }}
        >
          Post
        </Button>
      </div>
    </Modal>
  );
};

export default ModalCreatePost;
