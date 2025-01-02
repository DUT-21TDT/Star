import {Avatar, Popover} from "antd";
import {
  IconApprove,
  IconFollowed,
  IconInformation,
  IconLiked,
  IconReject,
  IconReply,
  IconReport,
  IconReposted,
} from "./icon-activity";
import React, {useState} from "react";
import {timeAgo} from "../../../utils/convertTime";
import ContainerInformationUser from "../profile/posts/container-information-user";
import {useNavigate} from "react-router-dom";
import default_avatar from "../../../assets/images/default_image.jpg";
import starLogo from "../../../assets/images/starLogoWhite.svg";
import {useMarkNotificationAsRead} from "../../../hooks/notification";

interface INotificationType {
  id: string;
  type: string;
  artifactId: string;
  artifactType: string;
  artifactPreview: string;
  lastActor?: {
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
    id,
    type,
    artifactId,
    lastActor,
    numberOfActors,
    changeAt,
    artifactType,
    artifactPreview,
    read
  } = notification;

  const idOfLastActor = lastActor ? lastActor.id : null;
  const username = lastActor ? lastActor.username : null;
  const avatarUrl = lastActor ? lastActor.avatarUrl : null;

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

  const { mutate: markNotificationAsRead } = useMarkNotificationAsRead();

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
      case "FOLLOW":
      case "REQUEST_FOLLOW":
        return <IconFollowed />;
      case "LIKE_POST":
        return <IconLiked />;
      case "REPOST_POST":
        return <IconReposted />;
      case "APPROVE_POST":
        return <IconApprove />;
      case "REJECT_POST":
        return <IconReject />;
      case "REPLY_POST":
        return <IconReply />;
      case "REPORT_POST":
        return <IconReport />;
      default:
        return <IconInformation />;
    }
  };

  const mapperMessageWithNotificationType = (type: string) => {
    switch (type) {
      case "NEW_PENDING_POST":
        return `Have posted in ${artifactPreview}`;
      case "APPROVE_POST":
        // return artifactPreview ? artifactPreview : "Your post has been approved";
        return `Approve your post "${artifactPreview}"`;
      case "REJECT_POST":
        // return artifactPreview ? artifactPreview : "Your post has been rejected";
        return `Reject your post "${artifactPreview}"`;
      case "LIKE_POST":
        // return artifactPreview ? artifactPreview : `Liked your post`;
        return `Like your post "${artifactPreview}"`;
      case "REPLY_POST":
        // return artifactPreview ? artifactPreview : `Replied to your post`;
        return `Reply to your post "${artifactPreview}"`;
      case "REPOST_POST":
        // return artifactPreview ? artifactPreview : `Reposted your post`;
        return `Repost your post "${artifactPreview}"`;
      case "REPORT_POST":
        return `Report a post "${artifactPreview}"`;
      case "FOLLOW":
        // return artifactPreview ? artifactPreview : `Followed you`;
        return `Followed you`;
      case "REQUEST_FOLLOW":
        // return artifactPreview ? artifactPreview : `Sent you a follow request`;
        return `Sent you a follow request`;
      case "ACCEPT_FOLLOW":
        // return artifactPreview ? artifactPreview : `Accepted your follow request`;
        return `Accepted your follow request`;
      default:
        return "You have a new notification";
    }
  };

  const handleClickOnNotification = (
    e: React.MouseEvent,
    id: string,
    artifactId: string,
    artifactType: string,
    artifactPreview: string,
    read: boolean
  ) => {
    if (!read) {
      handleMarkNotificationAsRead(id);
    }
    handleNavigateToArtifact(e, artifactType, artifactId, artifactPreview);
  };

  const handleMarkNotificationAsRead = (notificationId: string) => {
    markNotificationAsRead(notificationId);
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
        if (type === "NEW_PENDING_POST" ) {
          navigate(`/moderator/${artifactId}/pending`, {
            state: { roomName: artifactPreview },
          });
          window.scrollTo(0, 0);
          break;
        }
        else {
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
      className={`w-full flex items-start gap-4 py-3 ${!read ? "bg-neutral-100" : ""}`}
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
        <Avatar size={45} src={lastActor ? (avatarUrl || default_avatar) : starLogo} />
        <div className="absolute -bottom-1.5 right-0 rounded-full">
          {mapperIconWithNotificationType(type)}
        </div>
      </div>
      <div
        className="flex items-center justify-between w-full pb-5"
        style={{
          paddingBottom: "10px",
          cursor: "pointer",
        }}
        onClick={(e) =>
          handleClickOnNotification(e, id, artifactId, artifactType, artifactPreview, read)
        }
      >
        <div>
          {lastActor && idOfLastActor ? (
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
                <span className="text-[#ababab] font-normal">
                  {timeAgo(changeAt)}
                </span>
              </Popover>
            </div>
          ) : (
            <div className="flex font-semibold cursor-pointer gap-1">
              {"Moderators"}
              <span className="text-[#ababab] font-normal">
                {timeAgo(changeAt)}
              </span>
            </div>
          )}
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
            {mapperMessageWithNotificationType(type)}
          </div>
        </div>
        {!read && (
          <span className="text-red-500 mr-8 text-2xl">•</span>
        )}
      </div>
    </div>
  );
};
export default ActivityItem;
