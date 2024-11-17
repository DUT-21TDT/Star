import { Avatar, Button, Dropdown, MenuProps, message, Popover } from "antd";
import React, { useState } from "react";
import {
  DeleteOutlined,
  EditOutlined,
  EllipsisOutlined,
} from "@ant-design/icons";
import ReactButton from "./react-button";
import "../../../../assets/css/posts.css";
import default_image from "../../../../assets/images/default_image.jpg";
import { timeAgo } from "../../../../utils/convertTime";
import { useNavigate } from "react-router-dom";
import ContainerInformationUser from "./container-information-user";
import useEmblaCarousel from "embla-carousel-react";
import { PhotoProvider, PhotoView } from "react-photo-view";
import { useAppSelector } from "../../../../redux/store/hook";
import ModalConfirmDeletePost from "./modal-confirm-delete-post";
import { useDeletePost } from "../../../../hooks/post";
import DOMPurify from "dompurify";

interface IProps {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  idOfCreator?: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  numberOfFollowers?: number;
  disableReactButton?: boolean;
  nameOfRoom?: string;
  handleDeletePostSuccess?: (id: string) => void;
  isRemoved?: boolean;
}

const Post: React.FC<IProps> = (props) => {
  const {
    id,
    avatarUrlOfCreator,
    createdAt,
    content,
    postImageUrls,
    usernameOfCreator,
    numberOfLikes,
    numberOfComments,
    numberOfReposts,
    liked,
    idOfCreator,
    disableReactButton,
    nameOfRoom,
    isRemoved,
    handleDeletePostSuccess,
  } = props;

  const sanitizedContent = DOMPurify.sanitize(
    content.replace(
      /(https?:\/\/\S+)/g,
      '<a href="$1" target="_blank" rel="noopener noreferrer" class="text-[#1B75D0] hover:text-[#165ca3]">$1</a>'
    ),
    {
      ADD_ATTR: ["target"],
      FORBID_TAGS: ["style"],
    }
  );

  const navigate = useNavigate();
  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );

  // Dragging image feature
  const [emblaRef] = useEmblaCarousel({ loop: false });

  const [isDraggingImg, setIsDraggingImg] = useState(false);

  const handleMouseDown = () => {
    setIsDraggingImg(true);
  };

  const handleMouseUp = () => {
    setIsDraggingImg(false);
  };

  const handleNavigateProfileUser = (id: string) => () => {
    if (id) {
      navigate(`/profile/${id}`);
      window.scrollTo(0, 0);
    }
  };

  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={idOfCreator || ""} />
      );
    }
  };

  // delete post
  const [openModalDeletePost, setOpenModalDeletePost] = useState(false);
  const currentUserId = useAppSelector((state) => state.user.id);
  const { mutate: deletePost } = useDeletePost();

  const handleDeletePost = () => {
    deletePost(id, {
      onSuccess: () => {
        if (handleDeletePostSuccess) {
          handleDeletePostSuccess(id);
        }
        setOpenModalDeletePost(false);
      },
      onError: () => {
        message.error("Delete post failed");
        setOpenModalDeletePost(false);
      },
    });
  };
  const items: MenuProps["items"] = [
    {
      label: (
        <div className="w-[120px] h-[35px] text-[16px] flex gap-3 items-center">
          <DeleteOutlined
            style={{
              fontSize: "16px",
            }}
          />
          <span>Delete</span>
        </div>
      ),
      key: "1",
      onClick: () => {
        setOpenModalDeletePost(true);
      },
    },
    {
      label: (
        <div className="w-[120px] h-[35px] text-[16px] flex gap-3 items-center">
          <EditOutlined
            style={{
              fontSize: "16px",
            }}
          />
          <span>Edit</span>
        </div>
      ),
      key: "2",
    },
  ];

  if (isRemoved) {
    return (
      <div
        style={{
          height: "70px",
          padding: "10px 0px",
          borderBottom: "1px solid #f0f0f0",
        }}
      >
        <div
          style={{
            backgroundColor: "#f5f5f5",
            height: "50px",
            paddingLeft: "20px",
            display: "flex",
            alignItems: "center",
            borderRadius: "10px",
            color: "#adadad",
          }}
        >
          This post has been deleted
        </div>
      </div>
    );
  }

  return (
    <div
      className="p-3 hover:cursor-pointer"
      style={{
        borderBottom: "1px solid #f0f0f0",
        display: "flex",
        gap: "10px",
      }}
      onClick={(e) => {
        console.log(e.target as HTMLElement);
        console.log(e.currentTarget);
        if (
          e.target === e.currentTarget ||
          (e.target as HTMLElement).nodeName === "P" ||
          (e.target as HTMLElement).className.includes(
            "flex justify-between w-full"
          ) ||
          (e.target as HTMLElement).className.includes(
            "flex items-center justify-between"
          )
        ) {
          navigate(`/post/${id}`);
        }
      }}
    >
      <Avatar
        style={{
          width: "45px",
          height: "45px",
          cursor: "pointer",
        }}
        src={avatarUrlOfCreator || default_image}
        onClick={handleNavigateProfileUser(idOfCreator || "")}
      />
      <div style={{ width: "calc(100% - 65px)" }}>
        <div className="flex justify-between w-full">
          <Popover
            content={popoverContent}
            placement="bottomLeft"
            trigger="hover"
            mouseEnterDelay={0.35}
            overlayClassName="custom-popover"
            arrow={false}
            open={isPopoverVisibleUsername}
            onOpenChange={handlePopoverUsernameVisibilityChange}
          >
            <div
              style={{
                fontWeight: "500",
                fontSize: "16px",
                cursor: "pointer",
              }}
              onClick={handleNavigateProfileUser(idOfCreator || "")}
            >
              {usernameOfCreator}{" "}
              <span
                style={{
                  color: "rgb(153,153,153)",
                  fontSize: "14px",
                }}
              >
                {timeAgo(createdAt)}
              </span>
            </div>
          </Popover>

          {currentUserId === idOfCreator ? (
            <Dropdown menu={{ items }} placement="bottomRight">
              <Button
                icon={<EllipsisOutlined />}
                style={{ borderRadius: "50%", width: "25px", height: "25px" }}
              />
            </Dropdown>
          ) : (
            <Button
              icon={<EllipsisOutlined />}
              style={{ borderRadius: "50%", width: "25px", height: "25px" }}
            />
          )}
        </div>
        <div>
          <p
            style={{
              lineHeight: "22px",
              fontSize: "15px",
              textAlign: "left",
              marginTop: "4px",
            }}
            dangerouslySetInnerHTML={{ __html: sanitizedContent }}
          ></p>
        </div>
        {postImageUrls && postImageUrls.length > 0 && (
          <div
            className="embla mt-2"
            ref={emblaRef}
            style={{ overflow: "hidden", maxHeight: "400px" }}
          >
            <div
              className="embla__container"
              style={{
                display: "flex",
                cursor: isDraggingImg ? "grabbing" : "grab",
              }}
              onMouseDown={handleMouseDown}
              onMouseUp={handleMouseUp}
            >
              <PhotoProvider maskOpacity={0.7}>
                {postImageUrls.map((url, index) => (
                  <div
                    className="embla__slide"
                    key={url}
                    style={{
                      flex: "0 0 auto",
                      marginRight: "15px",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      backgroundColor: "white",
                    }}
                  >
                    <PhotoView key={index} src={url}>
                      <img
                        key={url}
                        src={url}
                        alt="Post Image"
                        style={{
                          maxHeight: "400px",
                          maxWidth: "560px",
                          width: "auto",
                          objectFit: "cover",
                          objectPosition: "center",
                          borderRadius: "15px",
                        }}
                      />
                    </PhotoView>
                  </div>
                ))}
              </PhotoProvider>
            </div>
          </div>
        )}
        {!disableReactButton && (
          <div className="flex items-center justify-between">
            <ReactButton
              postId={id}
              avatarUrlOfCreator={avatarUrlOfCreator}
              createdAt={createdAt}
              content={content}
              postImageUrls={postImageUrls}
              usernameOfCreator={usernameOfCreator}
              idOfCreator={idOfCreator}
              numberOfLikes={numberOfLikes}
              numberOfComments={numberOfComments}
              numberOfReposts={numberOfReposts}
              liked={liked}
            />
            {nameOfRoom && (
              <div
                style={{
                  marginTop: "5px",
                }}
              >
                <span
                  style={{
                    fontStyle: "italic",
                    marginTop: "5px",
                    fontWeight: "400",
                    color: "rgb(153,153,153)",
                  }}
                >
                  - in {""}
                  {nameOfRoom}
                </span>
              </div>
            )}
          </div>
        )}

        {disableReactButton && (
          <div
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "flex-end",
            }}
          >
            {nameOfRoom && (
              <div
                style={{
                  marginTop: "5px",
                }}
              >
                <span
                  style={{
                    fontStyle: "italic",
                    marginTop: "5px",
                    fontWeight: "400",
                    color: "rgb(153,153,153)",
                  }}
                >
                  - in {""}
                  {nameOfRoom}
                </span>
              </div>
            )}
          </div>
        )}
        <ModalConfirmDeletePost
          openModal={openModalDeletePost}
          setOpenModal={setOpenModalDeletePost}
          handleDeletePost={handleDeletePost}
        />
      </div>
    </div>
  );
};

export default Post;
