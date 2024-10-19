import { Avatar, Button, Popover } from "antd";
import React, { useState } from "react";
import { EllipsisOutlined } from "@ant-design/icons";
import ReactButton from "./react-button";
import "../../../../assets/css/posts.css";
import default_image from "../../../../assets/images/default_image.jpg";
import { timeAgo } from "../../../../utils/convertTime";
import { useNavigate } from "react-router-dom";
import ContainerInformationUser from "./container-information-user";
import useEmblaCarousel from "embla-carousel-react";

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
}

const Post: React.FC<IProps> = (props) => {
  const {
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
  } = props;

  const navigate = useNavigate();
  const [isPopoverVisibleAvatar, setIsPopoverVisibleAvatar] = useState(false);
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
    }
  };

  const handlePopoverAvatarVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleAvatar(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={idOfCreator || ""} />
      );
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

  return (
    <div
      className="p-3"
      style={{
        borderBottom: "1px solid #f0f0f0",
        display: "flex",
        gap: "10px",
      }}
    >
      <Popover
        content={popoverContent}
        placement="bottomLeft"
        trigger="hover"
        overlayClassName="custom-popover"
        arrow={false}
        open={isPopoverVisibleAvatar}
        onOpenChange={handlePopoverAvatarVisibilityChange}
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
      </Popover>
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
          <Button
            icon={<EllipsisOutlined />}
            style={{ borderRadius: "50%", width: "25px", height: "25px" }}
          />
        </div>
        <div className="py-2">
          <p
            style={{
              lineHeight: "20px",
              fontSize: "15px",
              textAlign: "left",
            }}
          >
            {content}
          </p>
        </div>
        {postImageUrls && postImageUrls.length > 0 && (
          <div className="embla" ref={emblaRef} style={{ overflow: "hidden", maxHeight: "400px" }}>
            <div className="embla__container"
              style={{ display: "flex", cursor: isDraggingImg ? "grabbing" : "grab" }}
              onMouseDown={handleMouseDown}
              onMouseUp={handleMouseUp}
            >
              {postImageUrls.map((url) => (
                <div className="embla__slide" key={url} style={{
                  flex: "0 0 auto",
                  marginRight: "15px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  backgroundColor: "white"
                }}>
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
                      borderRadius: "15px", /* Rounded corners */
                      // transition: "transform 0.3s ease", /* Smooth scaling effect */
                    }}
                  />
                </div>
              ))}
            </div>
          </div>
        )}

        <ReactButton
          numberOfLikes={numberOfLikes}
          numberOfComments={numberOfComments}
          numberOfReposts={numberOfReposts}
          liked={liked}
        />
      </div>
    </div>
  );
};

export default Post;
