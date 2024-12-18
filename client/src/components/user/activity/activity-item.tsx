import { Avatar, Popover } from "antd";
import {
  IconFollowed,
  IconInformation,
  IconLiked,
  IconReposted,
} from "./icon-activity";
import React, { useState } from "react";
import { timeAgo } from "../../../utils/convertTime";
import ContainerInformationUser from "../profile/posts/container-information-user";
import { useNavigate } from "react-router-dom";
import default_avatar from "../../../assets/images/default_image.jpg";
interface INotificationType {
  id: string;
  type: string;
  artifactId: string;
  artifactType: string;
  artifactPreview: string;
  lastActor: {
    id: string;
    username: string;
    avatarUrl: string;
  };
  numberOfActors: number;
  changeAt: string;
  read: boolean;
}
interface IProps {
  notification: INotificationType;
}
const ActivityItem: React.FC<IProps> = ({ notification }) => {
  const {
    type,
    artifactId,
    lastActor,
    numberOfActors,
    changeAt,
    artifactType,
    artifactPreview,
  } = notification;

  const { id: idOfLastActor, username, avatarUrl } = lastActor;
  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );

  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={idOfLastActor || ""} />
      );
    }
  };

  const navigate = useNavigate();
  const handleNavigateProfileUser = (e: React.MouseEvent, id: string) => {
    e.stopPropagation();
    if (id) {
      navigate(`/profile/${id}`);
      window.scrollTo(0, 0);
    }
  };

  const mapperIconWithNotificationType = (type: string) => {
    switch (type) {
      case "FOLLOW || REQUEST_FOLLOW":
        return <IconFollowed />;
      case "LIKE_POST":
        return <IconLiked />;
      case "REPOST_POST":
        return <IconReposted />;
      default:
        return <IconInformation />;
    }
  };

  const mapperMessageWithNotificationType = ({
    type,
  }: {
    type: string;
    numberOfActors: number;
  }) => {
    switch (type) {
      case "NEW_PENDING_POST":
        return `New pending post to review`;
      case "APPROVE_POST":
        return "Your post has been approved";
      case "REJECT_POST":
        return "Your post has been rejected";
      case "LIKE_POST":
        return artifactPreview ? artifactPreview : `Liked your post`;
      case "REPLY_POST":
        return artifactPreview ? artifactPreview : `Replied to your post`;
      case "REPOST_POST":
        return artifactPreview ? artifactPreview : `Reposted your post`;
      case "FOLLOW":
        return artifactPreview ? artifactPreview : `Followed you`;
      case "REQUEST_FOLLOW":
        return artifactPreview ? artifactPreview : `Sent you a follow request`;
      case "ACCEPT_FOLLOW":
        return "Your follow request has been accepted";
      default:
        return "You have a new notification";
    }
  };

  const handleNavigateToArtifact = (
    e: React.MouseEvent,
    artifactType: string,
    artifactId: string,
    artifactPreview: string
  ) => {
    e.stopPropagation();
    switch (artifactType) {
      case "POST": {
        navigate(`/post/${artifactId}`);
        window.scrollTo(0, 0);
        break;
      }
      case "ROOM": {
        if (type === "NEW_PENDING_POST") {
          navigate(`/moderator/${artifactId}/pending`, {
            state: { roomName: artifactPreview },
          });
          window.scrollTo(0, 0);
          break;
        } else {
          navigate(`/room/${artifactId}/posts`);
          window.scrollTo(0, 0);
          break;
        }
      }
      case "USER": {
        navigate(`/profile/${artifactId}`);
        window.scrollTo(0, 0);
        break;
      }
      case "SYSTEM": {
        e.preventDefault();
        break;
      }
      default:
        return "Unknown notification type";
    }
  };

  const pluralSuffix =
    numberOfActors > 1
      ? ` and ${numberOfActors - 1} other${numberOfActors > 2 ? "s" : ""}`
      : "";

  return (
    <div
      className="w-full flex items-start gap-4 my-3"
      style={{
        borderBottom: "1px solid rgb(240,240,240)",
        paddingBottom: "5px",
      }}
    >
      <div
        style={{
          position: "relative",
          marginLeft: "20px",
        }}
      >
        <Avatar size={45} src={avatarUrl || default_avatar} />
        {mapperIconWithNotificationType(type)}
      </div>
      <div
        className="flex items-center justify-between w-full pb-5"
        style={{
          paddingBottom: "10px",
          cursor: "pointer",
        }}
        onClick={(e) =>
          handleNavigateToArtifact(e, artifactType, artifactId, artifactPreview)
        }
      >
        <div>
          <div
            className="font-semibold"
            style={{
              cursor: "pointer",
            }}
            onClick={(e) => handleNavigateProfileUser(e, idOfLastActor)}
          >
            <Popover
              content={popoverContent}
              placement="bottomLeft"
              trigger="hover"
              overlayClassName="custom-popover"
              arrow={false}
              open={isPopoverVisibleUsername}
              onOpenChange={handlePopoverUsernameVisibilityChange}
            >
              {username} {pluralSuffix} {""}
              <span
                style={{
                  color: "#ababab",
                  fontWeight: "400",
                }}
              >
                {timeAgo(changeAt)}
              </span>
            </Popover>
          </div>
          <div
            style={{
              fontSize: "16px",
              color: "#ababab",
              fontWeight: 400,
              display: "-webkit-box",
              WebkitLineClamp: 1,
              WebkitBoxOrient: "vertical",
              overflow: "hidden",
              textOverflow: "ellipsis",
            }}
          >
            {mapperMessageWithNotificationType({ type, numberOfActors })}
          </div>
        </div>
      </div>
    </div>
  );
};
export default ActivityItem;
