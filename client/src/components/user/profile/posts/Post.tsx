import { Avatar, Button, Dropdown, MenuProps, message, Popover } from "antd";
import React, { useEffect, useRef, useState } from "react";
import { DeleteOutlined, EllipsisOutlined } from "@ant-design/icons";
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
import {
  useDeletePost,
  useRejectPostByModerator,
} from "../../../../hooks/post";
import DOMPurify from "dompurify";
import ModalConfirmReportPost from "./modal-confirm-report-post";
import { HiFlag } from "react-icons/hi";
import ModalConfirmRejectPost from "../../moderator/modal-confirm-reject";

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
  reposted: boolean;
  idOfCreator?: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  numberOfFollowers?: number;
  disableReactButton?: boolean;
  nameOfRoom?: string;
  handleDeletePostSuccess?: (id: string) => void;
  isRemoved?: boolean;
  isShowReplies?: boolean;
  canModerate?: boolean;
}

const ContentWithSeeMore: React.FC<{ sanitizedContent: string }> = ({
  sanitizedContent,
}) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [isLongContent, setIsLongContent] = useState(false);
  const contentRef = useRef<HTMLParagraphElement>(null);

  useEffect(() => {
    if (contentRef.current) {
      const previousMaxHeight = contentRef.current.style.maxHeight;
      const previousOverflow = contentRef.current.style.overflow;

      contentRef.current.style.maxHeight = "none";
      contentRef.current.style.overflow = "visible";

      const lineHeight = 22;
      const totalHeight = contentRef.current.offsetHeight;
      const maxHeight = lineHeight * 10;

      setIsLongContent(totalHeight > maxHeight);

      contentRef.current.style.maxHeight = previousMaxHeight;
      contentRef.current.style.overflow = previousOverflow;
    }
  }, [sanitizedContent]);

  return (
    <div>
      <p
        ref={contentRef}
        style={{
          lineHeight: "22px",
          fontSize: "15px",
          textAlign: "left",
          marginTop: "4px",
          wordBreak: "break-word",
          maxHeight: isExpanded ? "none" : "220px",
          overflow: isExpanded ? "visible" : "hidden",
        }}
        dangerouslySetInnerHTML={{ __html: sanitizedContent }}
      ></p>
      {isLongContent && !isExpanded && (
        <span
          onClick={() => setIsExpanded(true)}
          style={{
            display: "block",
            color: "#1B75D0",
            marginTop: "8px",
            cursor: "pointer",
            fontSize: "15px",
          }}
        >
          See more
        </span>
      )}
    </div>
  );
};

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
    reposted,
    idOfCreator,
    disableReactButton,
    nameOfRoom,
    isRemoved,
    handleDeletePostSuccess,
    isShowReplies,
    canModerate,
  } = props;

  const sanitizedContent = DOMPurify.sanitize(
    content
      .replace(
        /(https?:\/\/\S+)/g,
        '<a href="$1" target="_blank" rel="noopener noreferrer" class="text-[#1B75D0] hover:text-[#165ca3]">$1</a>'
      )
      .replace(/\n/g, "<br />"),
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
  const { mutate: rejectPost } = useRejectPostByModerator();

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

  const handleRejectPost = (reason: string) => {
    rejectPost(
      {
        postId: id,
        reason,
      },
      {
        onSuccess: () => {
          if (handleDeletePostSuccess) {
            handleDeletePostSuccess(id);
          }
        },
        onError: (error) => {
          console.error("Error:", error);
          message.error("Reject post failed");
        },
      }
    );
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
  ];

  // report post
  const [openModalReportPost, setOpenModalReportPost] = useState(false);
  const [openModalConfirmReject, setOpenModalConfirmReject] = useState(false);

  const itemsMenuReport: MenuProps["items"] = [
    {
      label: (
        <div className="w-[120px] h-[35px] text-[16px] flex gap-3 items-center">
          <HiFlag size={16} />
          <span>Report</span>
        </div>
      ),
      key: "1",
      onClick: () => {
        setOpenModalReportPost(true);
      },
    },
  ];
  if (canModerate) {
    itemsMenuReport.push({
      label: (
        <div className="w-[120px] h-[35px] text-[16px] flex gap-3 items-center">
          <DeleteOutlined
            style={{
              fontSize: "16px",
            }}
          />
          <span>Reject</span>
        </div>
      ),
      key: "2",
      onClick: () => {
        setOpenModalConfirmReject(true);
      },
    });
  }
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
        borderBottom: !isShowReplies ? "1px solid #f0f0f0" : "none",
        display: "flex",
        gap: "10px",
        padding: !isShowReplies ? "12px" : "12px 12px 4px 12px",
      }}
      onClick={(e) => {
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
      {!isShowReplies ? (
        <Avatar
          style={{
            width: "45px",
            height: "45px",
            cursor: "pointer",
          }}
          src={avatarUrlOfCreator || default_image}
          onClick={handleNavigateProfileUser(idOfCreator || "")}
        />
      ) : (
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            minHeight: "80px",
          }}
        >
          <Avatar
            style={{
              width: "45px",
              height: "45px",
              cursor: "pointer",
              flexShrink: 0,
            }}
            src={avatarUrlOfCreator || default_image}
            onClick={handleNavigateProfileUser(idOfCreator || "")}
          />
          <div
            style={{
              width: "3px",
              height: "calc(100% - 45px)",
              backgroundColor: "#e0e0e0",
              margin: "10px 0px 0px 0px",
            }}
          ></div>
        </div>
      )}

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
            <Dropdown
              menu={{ items }}
              placement="bottomRight"
              trigger={["click"]}
            >
              <Button
                icon={<EllipsisOutlined />}
                style={{ borderRadius: "50%", width: "25px", height: "25px" }}
              />
            </Dropdown>
          ) : (
            <Dropdown
              menu={{ items: itemsMenuReport }}
              placement="bottomRight"
              trigger={["click"]}
            >
              <Button
                icon={<EllipsisOutlined />}
                style={{ borderRadius: "50%", width: "25px", height: "25px" }}
              />
            </Dropdown>
          )}
        </div>
        <ContentWithSeeMore sanitizedContent={sanitizedContent} />
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
                      maxHeight: "400px",
                      maxWidth: "500px",
                    }}
                  >
                    <PhotoView key={index} src={url}>
                      <img
                        key={url}
                        src={url}
                        alt="Post Image"
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                          objectPosition: "center",
                          borderRadius: "15px",
                          aspectRatio: "auto",
                        }}
                        loading="lazy"
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
              reposted={reposted}
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
        <ModalConfirmReportPost
          openModal={openModalReportPost}
          setOpenModal={setOpenModalReportPost}
          dataDetailPost={{
            id: id,
            usernameOfCreator: usernameOfCreator,
            avatarUrlOfCreator: avatarUrlOfCreator,
            createdAt: createdAt,
            content: content,
            postImageUrls: postImageUrls,
            numberOfLikes: numberOfLikes,
            numberOfComments: numberOfComments,
            numberOfReposts: numberOfReposts,
            liked: liked,
            reposted: reposted,
            nameOfRoom: nameOfRoom || "",
            idOfCreator: idOfCreator || "",
          }}
        />

        <ModalConfirmRejectPost
          isOpenModal={openModalConfirmReject}
          setIsOpenModal={setOpenModalConfirmReject}
          dataDetailPost={{
            id: id,
            usernameOfCreator: usernameOfCreator,
            avatarUrlOfCreator: avatarUrlOfCreator,
            createdAt: createdAt,
            content: content,
            postImageUrls: postImageUrls,
            idOfCreator: idOfCreator,
          }}
          handleRejectPost={handleRejectPost}
        />
      </div>
    </div>
  );
};

export default Post;
