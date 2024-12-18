import React, { useState } from "react";
import { Avatar, Modal, Input, Button, Upload, message } from "antd";
import type { GetProp, UploadFile, UploadProps } from "antd";
import { CloudUploadOutlined } from "@ant-design/icons";
import { useAppSelector } from "../../../../redux/store/hook";
import useEmblaCarousel from "embla-carousel-react";
import { PhotoProvider, PhotoView } from "react-photo-view";
import { useGetAllPresignedUrl, useReplyPost } from "../../../../hooks/post";
import "../../../../assets/css/modal-create-post.css";
import ViewPostInformationReplyPost from "./view-post-information-reply-post";
import PerfectScrollbar from "react-perfect-scrollbar";
import "react-perfect-scrollbar/dist/css/styles.css";
import "../../../../assets/css/modal-reply-post.css";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";
import { getPostDetailById } from "../../../../service/postAPI";
import EmojiPicker from "emoji-picker-react";
import default_avatar from "../../../../assets/images/default_image.jpg";
type FileType = Parameters<GetProp<UploadProps, "beforeUpload">>[0];

const getBase64 = (file: FileType): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });

interface IProps {
  postId: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  usernameOfCreator: string;
  idOfCreator: string | undefined;
  isModalOpen: boolean;
  setIsModalOpen: (value: boolean) => void;
  setCommentCount: (value: number | ((prev: number) => number)) => void;
}

const ModalReplyPost: React.FC<IProps> = ({
  postId,
  avatarUrlOfCreator,
  createdAt,
  content,
  postImageUrls,
  usernameOfCreator,
  idOfCreator,
  isModalOpen,
  setIsModalOpen,
  setCommentCount,
}) => {
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [fileListBase64, setFileListBase64] = useState<string[]>([]);
  const [textValue, setTextValue] = useState<string>("");
  const [loading, setLoading] = useState(false);
  const [emblaRef] = useEmblaCarousel();

  const isPostDetailRoute = window.location.pathname.startsWith("/post");

  const userData = useAppSelector((state) => state.user);
  const { mutate: createReplyPost } = useReplyPost();
  const { mutate: getPostPresignedURL } = useGetAllPresignedUrl();

  const queryClient = useQueryClient();

  const handleChange: UploadProps["onChange"] = ({ fileList: newFileList }) => {
    if (!beforeUpload(newFileList[newFileList.length - 1] as FileType)) {
      return;
    }
    setFileList(newFileList);
    Promise.all(
      newFileList.map((file) => getBase64(file.originFileObj as FileType))
    ).then((base64List) => setFileListBase64(base64List as string[]));
  };

  const handleOk = async () => {
    setLoading(true);
    if (!textValue && fileList.length === 0) {
      message.error("Please input content or upload an image");
      setLoading(false);
      return;
    }
    const dateTime = new Date().getTime();
    const postContent = {
      parentPostId: postId,
      content: textValue,
      imageFileNames:
        fileList.length > 0
          ? fileList.map((file) => `post/${file.uid}${dateTime}${file.name}`)
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
          await replyPost(postContent);
        },
        onError: () => {
          message.error("Reply this post failed. Please try again later");
          setLoading(false);
        },
      });
    } else {
      await replyPost(postContent);
    }
  };

  const replyPost = async (postContent: {
    parentPostId: string;
    content: string;
    imageFileNames: string[];
  }) => {
    createReplyPost(postContent, {
      onSuccess: async (response) => {
        if (isPostDetailRoute) {
          const detailPost = await getPostDetailById(response.id);
          const updatedPost = { ...detailPost, nameOfRoom: null };
          queryClient.setQueryData(
            [QUERY_KEY.fetchRepliesByPostId(postId), null],
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            (oldData: any) => {
              return {
                ...oldData,
                content: [updatedPost, ...oldData.content],
              };
            }
          );
        }
        message.success("Reply this post successfully");
        setCommentCount((prev: number) => prev + 1);
      },
      onError: () => {
        message.error("Reply this post failed. Please try again later");
        setLoading(false);
      },
    });
    resetModal();
  };

  const resetModal = () => {
    setTextValue("");
    setFileList([]);
    setFileListBase64([]);
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

  // handle add emoji
  const [showPicker, setShowPicker] = useState(false);
  const handleEmojiClick = (emojiObject: { emoji: string }) => {
    setTextValue((prevText) => prevText + emojiObject.emoji);
  };

  return (
    <Modal
      title={
        <p className="text-center text-[20px] font-bold mt-[15px]">Reply</p>
      }
      open={isModalOpen}
      onOk={handleOk}
      onCancel={handleCancel}
      centered
      width={650}
      footer={null}
      destroyOnClose
    >
      <PerfectScrollbar
        style={{ maxHeight: "700px" }}
        options={{ suppressScrollX: true }}
      >
        <ViewPostInformationReplyPost
          postId={postId}
          avatarUrlOfCreator={avatarUrlOfCreator}
          createdAt={createdAt}
          content={content}
          postImageUrls={postImageUrls}
          usernameOfCreator={usernameOfCreator}
          idOfCreator={idOfCreator}
        />
        <div className="flex items-start gap-3 p-3">
          <Avatar src={userData?.avatarUrl || default_avatar} size={45} />
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
          </div>
        </div>
      </PerfectScrollbar>
      <div className="flex items-center gap-3 ml-16">
        <Upload
          fileList={fileList}
          onChange={handleChange}
          showUploadList={false}
          beforeUpload={() => false}
        >
          <Button
            icon={<CloudUploadOutlined />}
            type="text"
            style={{ color: "gray", backgroundColor: "#f0f0f0" }}
          >
            Upload
          </Button>
        </Upload>
        <div>
          <Button
            type="text"
            style={{
              color: "gray",
              backgroundColor: "#f0f0f0",
              position: "relative",
            }}
            onClick={() => setShowPicker(!showPicker)}
          >
            Emoji
          </Button>
          {showPicker && (
            <EmojiPicker
              onEmojiClick={handleEmojiClick}
              style={{ position: "absolute" }}
            />
          )}
        </div>
      </div>

      <div className="flex justify-end mt-2">
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

export default ModalReplyPost;
