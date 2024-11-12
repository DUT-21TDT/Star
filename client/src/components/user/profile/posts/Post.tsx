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
import { PhotoProvider, PhotoView } from "react-photo-view";
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
  } = props;

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
          <Button
            icon={<EllipsisOutlined />}
            style={{ borderRadius: "50%", width: "25px", height: "25px" }}
          />
        </div>
        <div>
          <p
            style={{
              lineHeight: "22px",
              fontSize: "15px",
              textAlign: "left",
              marginTop: "4px",
            }}
          >
            {content}
          </p>
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
          <div
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
            }}
          >
            <ReactButton
              postId={id}
              numberOfLikes={numberOfLikes}
              numberOfComments={numberOfComments}
              numberOfReposts={numberOfReposts}
              liked={liked}
            />
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
          </div>
        )}
      </div>
    </div>
  );
};

export default Post;
