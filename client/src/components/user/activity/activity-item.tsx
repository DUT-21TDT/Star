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
interface INotificationType {
  id: string;
  type: string;
  artifactId: string;
  artifactType: string;
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
    numberOfActors,
  }: {
    type: string;
    numberOfActors: number;
  }) => {
    const pluralSuffix =
      numberOfActors > 1
        ? ` and ${numberOfActors - 1} ${
            numberOfActors > 2 ? "others" : "other"
          }`
        : "";

    switch (type) {
      case "NEW_PENDING_POST":
        return `New pending post${pluralSuffix}`;
      case "APPROVE_POST":
        return "Your post has been approved";
      case "REJECT_POST":
        return "Your post has been rejected";
      case "LIKE_POST":
        return `Liked your post${pluralSuffix}`;
      case "REPLY_POST":
        return `Replied to your post${pluralSuffix}`;
      case "REPOST_POST":
        return `Reposted your post${pluralSuffix}`;
      case "FOLLOW":
        return `Followed you${pluralSuffix}`;
      case "REQUEST_FOLLOW":
        return `Sent you a follow request${pluralSuffix}`;
      case "ACCEPT_FOLLOW":
        return "Your follow request has been accepted";
      default:
        return "You have a new notification";
    }
  };

  const handleNavigateToArtifact = (
    e: React.MouseEvent,
    artifactType: string,
    artifactId: string
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
          navigate(`/room`);
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
        <Avatar size={45} src={avatarUrl} />
        {mapperIconWithNotificationType(type)}
      </div>
      <div
        className="flex items-center justify-between w-full pb-5"
        style={{
          paddingBottom: "10px",
          cursor: "pointer",
        }}
        onClick={(e) => handleNavigateToArtifact(e, artifactType, artifactId)}
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
              {username}{" "}
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
