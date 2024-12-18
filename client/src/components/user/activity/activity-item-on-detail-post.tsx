import { Avatar, Button, message, Popover } from "antd";
import {
  IconFollowed,
  IconInformation,
  IconLiked,
  IconReposted,
} from "./icon-activity";
import React, { useEffect, useState } from "react";
import { timeAgo } from "../../../utils/convertTime";
import ContainerInformationUser from "../profile/posts/container-information-user";
import { useNavigate } from "react-router-dom";
import default_avatar from "../../../assets/images/default_image.jpg";
import { useFollowUser, useUnfollowUser } from "../../../hooks/user";
import { useAppSelector } from "../../../redux/store/hook";

interface IActivityItemPostDetail {
  userId: string;
  username: string;
  avatarUrl: string;
  firstName: string;
  lastName: string;
  interactType: string;
  interactAt: string;
  followStatus: string;
}

interface IProps {
  data: IActivityItemPostDetail;
  setAllActivitiesOnDetailPost: React.Dispatch<
    React.SetStateAction<IActivityItemPostDetail[]>
  >;
}
const ActivityItemOnPostDetail: React.FC<IProps> = ({
  data,
  setAllActivitiesOnDetailPost,
}) => {
  const {
    userId,
    username,
    avatarUrl,
    firstName,
    lastName,
    interactType,
    interactAt,
    followStatus,
  } = data;

  const [currentFollowStatus, setCurrentFollowStatus] =
    useState<string>(followStatus);

  useEffect(() => {
    setCurrentFollowStatus(followStatus);
  }, [followStatus]);

  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );

  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser
          idOfCreator={userId || ""}
          parentFollowStatus={currentFollowStatus}
          updateParentFollowStatus={setCurrentFollowStatus}
          isDetailPostPage={true}
          setAllActivitiesOnDetailPost={setAllActivitiesOnDetailPost}
        />
      );
    }
  };

  useEffect(() => {
    if (isPopoverVisibleUsername) {
      setPopoverContent(
        <ContainerInformationUser
          idOfCreator={userId || ""}
          parentFollowStatus={currentFollowStatus}
          updateParentFollowStatus={setCurrentFollowStatus}
          isDetailPostPage={true}
          setAllActivitiesOnDetailPost={setAllActivitiesOnDetailPost}
        />
      );
    }
  }, [currentFollowStatus, isPopoverVisibleUsername, userId]);

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
      case "REPLY":
        return <IconInformation />;
      case "LIKE":
        return <IconLiked />;
      case "REPOST":
        return <IconReposted />;
      default:
        return <IconFollowed />;
    }
  };

  const { mutate: unfollowUser } = useUnfollowUser();
  const { mutate: followUser } = useFollowUser();

  const isCurrentPeopleItem =
    useAppSelector((state) => state.user.id) === userId;

  const handleUnfollowUser = (userId: string) => {
    unfollowUser(userId, {
      onSuccess: () => {
        setCurrentFollowStatus("NOT_FOLLOWING");
        setAllActivitiesOnDetailPost((prev) =>
          prev.map((item) =>
            item.userId === userId
              ? { ...item, followStatus: "NOT_FOLLOWING" }
              : item
          )
        );
      },
      onError: (error: Error) => {
        console.error("Error unfollowing user:", error);
      },
    });
  };

  const handleFollowUser = (userId: string) => {
    followUser(userId, {
      onSuccess: (response) => {
        if (response?.followStatus === "FOLLOWING") {
          setCurrentFollowStatus("FOLLOWING");
          setAllActivitiesOnDetailPost((prev) =>
            prev.map((item) =>
              item.userId === userId
                ? { ...item, followStatus: "FOLLOWING" }
                : item
            )
          );
        } else if (response?.followStatus === "REQUESTED") {
          setCurrentFollowStatus("REQUESTED");
          setAllActivitiesOnDetailPost((prev) =>
            prev.map((item) =>
              item.userId === userId
                ? { ...item, followStatus: "REQUESTED" }
                : item
            )
          );
        }
      },
      onError: () => {
        message.error("Error following user:");
      },
    });
  };

  return (
    <div
      className="w-full flex items-start gap-3 my-3"
      style={{
        // borderBottom: "1px solid rgb(240,240,240)",
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
        {mapperIconWithNotificationType(interactType)}
      </div>
      <div
        className="flex items-center justify-between w-full pb-5"
        style={{
          paddingBottom: "10px",
          borderBottom: "1px solid rgb(240,240,240)",
        }}
      >
        <div>
          <div
            className="font-semibold"
            style={{
              cursor: "pointer",
            }}
            onClick={(e) => handleNavigateProfileUser(e, userId)}
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
                {timeAgo(interactAt)}
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
            {firstName || lastName ? (
              <div className="text-[14px] text-gray-500">
                {`${firstName || ""} ${lastName || ""}`}
              </div>
            ) : (
              <div className="text-[14px] text-gray-500">{username}</div>
            )}
          </div>
        </div>
        <Button
          hidden={isCurrentPeopleItem}
          style={{
            color:
              currentFollowStatus === "FOLLOWING"
                ? "#bdbdbd"
                : currentFollowStatus === "REQUESTED"
                ? "#9e9e9e"
                : "black",
            fontWeight: 500,
            width: "120px",
            borderRadius: "8px",
            padding: "10px 0px",
            marginRight: "5px",
          }}
          onClick={(e) => {
            if (
              currentFollowStatus === "FOLLOWING" ||
              currentFollowStatus === "REQUESTED"
            ) {
              e.stopPropagation();
              handleUnfollowUser(userId);
            } else {
              e.stopPropagation();
              handleFollowUser(userId);
            }
          }}
        >
          {currentFollowStatus === "FOLLOWING"
            ? "Following"
            : currentFollowStatus === "REQUESTED"
            ? "Requested"
            : "Follow"}
        </Button>
      </div>
    </div>
  );
};
export default ActivityItemOnPostDetail;
