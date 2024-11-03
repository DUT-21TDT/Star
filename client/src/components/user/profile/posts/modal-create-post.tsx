import React, { useState } from "react";
import { Avatar, Modal, Input, Button, Upload, message } from "antd";
import { CloudUploadOutlined } from "@ant-design/icons";
import type { GetProp, UploadFile, UploadProps } from "antd";
import useEmblaCarousel from "embla-carousel-react";
import { Select } from "antd";
import { useAppSelector } from "../../../../redux/store/hook";
import { PhotoProvider, PhotoView } from "react-photo-view";
import { useGetAllRoomForUser } from "../../../../hooks/room";
import { useCreateAPost } from "../../../../hooks/post";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";
type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];
interface RoomType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
  isParticipant?: boolean;
}

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
  const [emblaRef] = useEmblaCarousel();
  const [textValue, setTextValue] = useState<string>("");
  const handleChange: UploadProps["onChange"] = ({ fileList: newFileList }) => {
    setFileList(newFileList);
    Promise.all(
      newFileList.map((file) => getBase64(file.originFileObj as FileType))
    ).then((base64) => setFileListBase64(base64 as string[]));
  };
  const userData = useAppSelector((state) => state.user);
  const [optionSelected, setOptionSelected] = useState<string>("");
  const [loading, setLoading] = useState(false);

  const { TextArea } = Input;
  const { mutate: createAPost } = useCreateAPost();
  const queryClient = useQueryClient();
  const handleOk = () => {
    setLoading(true);
    if (textValue === "" && fileList.length === 0) {
      message.error("Please input content or upload an image");
      return;
    }
    createAPost(
      { roomId: optionSelected, content: textValue },
      {
        onSuccess: () => {
          message.success("Create post successfully");
          // queryClient.invalidateQueries({
          //   queryKey: QUERY_KEY.fetchAllPostsOnNewsFeed(),
          // });
          setIsModalOpen(false);
        },
        onError: () => {
          message.error("Create post failed. Please try again later");
        },
        onSettled: () => {
          setLoading(false);
        },
      }
    );
  };

  const handleCancel = () => {
    setTextValue("");
    setFileList([]);
    setFileListBase64([]);
    setIsModalOpen(false);
  };

  const titleModalCreatePost = () => {
    return (
      <>
        <p className="text-center text-[18px] font-bold">New post</p>
        <hr className="my-3" />
      </>
    );
  };
  const onChange = (value: string) => {
    setOptionSelected(value);
  };
  const handleDeleteImageUpload = (index: number) => {
    return () => {
      const newFileList = fileList.filter((_, i) => i !== index);
      setFileList(newFileList);
      setFileListBase64(fileListBase64.filter((_, i) => i !== index));
    };
  };
  const ImageUpload = () => {
    return (
      <>
        <PhotoProvider maskOpacity={0.7}>
          {fileListBase64.map((base64, index) => (
            <div
              className="embla__slide"
              style={{
                flex: "0 0 auto",
                marginRight: "15px",
                alignItems: "center",
                justifyContent: "center",
                backgroundColor: "white",
                position: "relative",
              }}
              key={index}
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
                }}
              >
                <svg
                  fill="white"
                  height="12px"
                  width="12px"
                  version="1.1"
                  id="Capa_1"
                  xmlns="http://www.w3.org/2000/svg"
                  xmlnsXlink="http://www.w3.org/1999/xlink"
                  viewBox="0 0 460.775 460.775"
                  xmlSpace="preserve"
                  style={{
                    cursor: "pointer",
                  }}
                  onClick={handleDeleteImageUpload(index)}
                >
                  <path
                    d="M285.08,230.397L456.218,59.27c6.076-6.077,6.076-15.911,0-21.986L423.511,4.565c-2.913-2.911-6.866-4.55-10.992-4.55
	c-4.127,0-8.08,1.639-10.993,4.55l-171.138,171.14L59.25,4.565c-2.913-2.911-6.866-4.55-10.993-4.55
	c-4.126,0-8.08,1.639-10.992,4.55L4.558,37.284c-6.077,6.075-6.077,15.909,0,21.986l171.138,171.128L4.575,401.505
	c-6.074,6.077-6.074,15.911,0,21.986l32.709,32.719c2.911,2.911,6.865,4.55,10.992,4.55c4.127,0,8.08-1.639,10.994-4.55
	l171.117-171.12l171.118,171.12c2.913,2.911,6.866,4.55,10.993,4.55c4.128,0,8.081-1.639,10.992-4.55l32.709-32.719
	c6.074-6.075,6.074-15.909,0-21.986L285.08,230.397z"
                  />
                </svg>
              </div>
              <PhotoView key={index} src={base64}>
                <img
                  key={index}
                  src={base64}
                  alt={`upload-${index}`}
                  style={{
                    maxHeight: "480px",
                    maxWidth: "350px",
                    width: "auto",
                    objectFit: "cover",
                    objectPosition: "center",
                    borderRadius: "15px",
                    border: "1px solid #ccc",
                  }}
                />
              </PhotoView>
            </div>
          ))}
        </PhotoProvider>
      </>
    );
  };

  const { listRoomJoined } = useGetAllRoomForUser();
  const optionRoom = listRoomJoined.map((room: RoomType) => ({
    value: room.id,
    label: room.name,
  }));

  return (
    <>
      <Modal
        title={titleModalCreatePost()}
        open={isModalOpen}
        onOk={handleOk}
        onCancel={handleCancel}
        centered
        width={650}
        footer={null}
      >
        <div className="flex items-start justify-start gap-5">
          <div className="w-[60px]">
            <Avatar src={userData?.avatarUrl} size={50} />
          </div>
          <div className="w-full">
            <p className="text-[16px] font-semibold">{userData?.username}</p>
            <TextArea
              placeholder="What's news?"
              autoSize={{ maxRows: 20 }}
              style={{
                border: "none",
                outline: "none",
                paddingLeft: "0",
                width: "100%",
                fontSize: "16px",
                maxWidth: "500px",
              }}
              value={textValue}
              onChange={(e) => setTextValue(e.target.value)}
            />
            <div
              className="embla mt-[10px] mb-[15px]"
              ref={emblaRef}
              style={{
                overflow: "hidden",
                maxHeight: "400px",
                maxWidth: "500px",
              }}
            >
              <div
                className="embla__container"
                style={{
                  display: "flex",
                }}
              >
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
                  icon={
                    <CloudUploadOutlined
                      style={{ fontSize: "20px", color: "gray" }}
                    />
                  }
                  type="text"
                  style={{
                    width: "100px",
                    height: "35px",
                    color: "gray",
                    padding: "0px 10px",
                    backgroundColor: "#f0f0f0",
                  }}
                >
                  Upload
                </Button>
              </Upload>
              <Select
                showSearch
                placeholder="Choose your room"
                optionFilterProp="label"
                onChange={onChange}
                options={optionRoom}
                style={{
                  minWidth: "150px",
                  width: "auto",
                  height: "35px",
                }}
              />
            </div>
          </div>
        </div>

        <div className="flex justify-end">
          <Button
            type="default"
            style={{
              marginLeft: 12,
              color: "black",
              width: "70px",
              height: "35px",
              fontWeight: "500",
            }}
            onClick={handleOk}
            loading={loading}
          >
            Post
          </Button>
        </div>
      </Modal>
    </>
  );
};

export default ModalCreatePost;
