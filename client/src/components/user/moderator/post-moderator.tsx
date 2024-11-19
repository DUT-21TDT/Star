import React, { useState } from "react";
import { Avatar, Button, message, Popover } from "antd";
import { useNavigate, useParams } from "react-router-dom";
import { PhotoProvider, PhotoView } from "react-photo-view";
import ContainerInformationUser from "../profile/posts/container-information-user";
import useEmblaCarousel from "embla-carousel-react";
import default_image from "../../../assets/images/default_image.jpg";
import { timeAgo } from "../../../utils/convertTime";
import "../../../assets/css/post-moderator.css";
import { useChangeStatusPostByModerator } from "../../../hooks/post";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import DOMPurify from "dompurify";
interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  idOfCreator?: string;
  nameOfRoom?: string | null;
  idOfModerator?: string | null;
  usernameOfModerator?: string | null;
  moderatedAt?: string | null;
  violenceScore: number;
  status: string;
  isChangeStatus?: string;
}

interface IProps {
  postData: PostType;
  handleChangeStatusPostState: (postId: string, status: string) => void;
}

const PostModerator: React.FC<IProps> = (props) => {
  const { postData, handleChangeStatusPostState } = props;
  const { roomId = "" } = useParams<{ roomId: string }>();
  const {
    id,
    avatarUrlOfCreator,
    createdAt,
    content,
    postImageUrls,
    usernameOfCreator,
    idOfCreator,
    nameOfRoom,
    usernameOfModerator,
    moderatedAt,
    violenceScore,
    status,
    isChangeStatus,
  } = postData;

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

  const [emblaRef] = useEmblaCarousel({ loop: false });
  const [isDraggingImg, setIsDraggingImg] = useState(false);

  const handleMouseDown = () => setIsDraggingImg(true);
  const handleMouseUp = () => setIsDraggingImg(false);

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

  const convertDateTime = (dateStr: string) => {
    const dateObj = new Date(dateStr);

    const day = String(dateObj.getDate()).padStart(2, "0");
    const month = String(dateObj.getMonth() + 1).padStart(2, "0");
    const year = dateObj.getFullYear();

    let hours = dateObj.getHours();
    const minutes = String(dateObj.getMinutes()).padStart(2, "0");
    const seconds = String(dateObj.getSeconds()).padStart(2, "0");
    const ampm = hours >= 12 ? "PM" : "AM";
    hours = hours % 12 || 12;
    const formattedDate = `${day}/${month}/${year}, ${hours}:${minutes}:${seconds} ${ampm}`;
    return formattedDate;
  };

  const getViolenceScoreColor = (score: number | undefined) => {
    if (score === undefined) return "#999";
    if (score > 80) return "red";
    if (score >= 21) return "#ffcc00";
    return "green";
  };

  const { mutate: changeStatusPost } = useChangeStatusPostByModerator();
  const [loading, setLoading] = useState(false);
  const queryClient = useQueryClient();
  const handleChangeStatusPostByModerator = (statusToChange: string) => {
    setLoading(true);
    changeStatusPost(
      { postId: id, status: statusToChange },
      {
        onSuccess: () => {
          queryClient.invalidateQueries({
            queryKey: QUERY_KEY.fetchAllPendingPostForModerator(roomId, status),
          });
          handleChangeStatusPostState(id, statusToChange);
          setLoading(false);
        },
        onError: (error) => {
          console.error(error);
          message.error("Change status post failed");
          setLoading(false);
        },
      }
    );
  };

  if (isChangeStatus === "APPROVED") {
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
          This post has been approved
        </div>
      </div>
    );
  }

  if (isChangeStatus === "REJECTED") {
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
          This post has been rejected
        </div>
      </div>
    );
  }

  if (isChangeStatus === "PENDING") {
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
          This post has been moved to pending
        </div>
      </div>
    );
  }
  return (
    <div
      className="p-3"
      style={{
        borderBottom: "1px solid #f0f0f0",
        display: "flex",
        gap: "10px",
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
          {violenceScore !== undefined && (
            <div
              style={{
                marginTop: "8px",
                fontSize: "14px",
                color: getViolenceScoreColor(violenceScore),
              }}
            >
              Violence Score: {violenceScore}
            </div>
          )}
        </div>

        {nameOfRoom && (
          <div style={{ color: "#999", fontSize: "14px", marginTop: "4px" }}>
            Room: {nameOfRoom}
          </div>
        )}

        <div className="py-2">
          <p
            style={{
              lineHeight: "20px",
              fontSize: "15px",
              textAlign: "left",
              wordBreak: "break-word",
            }}
            dangerouslySetInnerHTML={{ __html: sanitizedContent }}
          ></p>
        </div>

        {postImageUrls && postImageUrls.length > 0 && (
          <div
            className="embla"
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

        {usernameOfModerator && moderatedAt && (
          <div style={{ marginTop: "8px", fontSize: "14px", color: "#999" }}>
            {status === "APPROVED"
              ? "Approved"
              : status === "REJECTED"
              ? "Rejected"
              : "Move to pending"}{" "}
            by {usernameOfModerator} on {convertDateTime(moderatedAt)}
          </div>
        )}

        <div
          style={{
            width: "100%",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            gap: "20px",
            marginTop: "10px",
          }}
        >
          {status === "PENDING" && (
            <>
              <Button
                style={{
                  width: "50%",
                  height: "45px",
                  fontWeight: "500",
                  fontSize: "16px",
                }}
                className="buttonChangeStatus"
                loading={loading}
                onClick={() => {
                  handleChangeStatusPostByModerator("APPROVED");
                }}
              >
                Approve
              </Button>
              <Button
                style={{
                  width: "50%",
                  height: "45px",
                  fontWeight: "500",
                  fontSize: "16px",
                }}
                className="buttonChangeStatus"
                onClick={() => {
                  handleChangeStatusPostByModerator("REJECTED");
                }}
              >
                Reject
              </Button>
            </>
          )}
          {status === "APPROVED" && (
            <>
              <Button
                style={{
                  width: "100%",
                  height: "45px",
                  fontWeight: "500",
                  fontSize: "16px",
                }}
                className="buttonChangeStatus"
                onClick={() => {
                  handleChangeStatusPostByModerator("PENDING");
                }}
              >
                Move to Pending
              </Button>
              <Button
                style={{
                  width: "100%",
                  height: "45px",
                  fontWeight: "500",
                  fontSize: "16px",
                }}
                className="buttonChangeStatus"
                onClick={() => {
                  handleChangeStatusPostByModerator("REJECTED");
                }}
              >
                Reject
              </Button>
            </>
          )}
          {status === "REJECTED" && (
            <>
              <Button
                style={{
                  width: "100%",
                  height: "45px",
                  fontWeight: "500",
                  fontSize: "16px",
                }}
                className="buttonChangeStatus"
                onClick={() => {
                  handleChangeStatusPostByModerator("PENDING");
                }}
              >
                Move to Pending
              </Button>
              <Button
                style={{
                  width: "100%",
                  height: "45px",
                  fontWeight: "500",
                  fontSize: "16px",
                }}
                className="buttonChangeStatus"
                onClick={() => {
                  handleChangeStatusPostByModerator("APPROVED");
                }}
              >
                Approve
              </Button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default PostModerator;
