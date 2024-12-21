import { Avatar, Button, message, Popover } from "antd";
import React, { useEffect, useState } from "react";
import ContainerInformationUser from "../profile/posts/container-information-user";
import { useNavigate } from "react-router-dom";
import default_avatar from "../../../assets/images/default_image.jpg";
import { useFollowUser, useUnfollowUser } from "../../../hooks/user";

interface ISuggestionItem {
  userId: string;
  username: string;
  avatarUrl: string;
  firstName: string;
  lastName: string;
  commonRoomRelation: {
    repId: string;
    repName: string;
    count: number;
    score: number;
  };
  mutualFollowingRelation: {
    repId: string;
    repName: string;
    count: number;
    score: number;
  };
  mutualFriendRelation: {
    repId: string;
    repName: string;
    count: number;
    score: number;
  };
  suggestType: string;
  totalRelationScore: number;
}

interface IProps {
  data: ISuggestionItem;
}
const SuggestionItem: React.FC<IProps> = ({ data }) => {
  const {
    userId,
    username,
    avatarUrl,
    commonRoomRelation,
    mutualFollowingRelation,
    mutualFriendRelation,
    suggestType,
  } = data;

  const followStatus = "NOT_FOLLOWING";
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

  const { mutate: unfollowUser } = useUnfollowUser();
  const { mutate: followUser } = useFollowUser();

  const handleUnfollowUser = (userId: string) => {
    unfollowUser(userId, {
      onSuccess: () => {
        setCurrentFollowStatus("NOT_FOLLOWING");
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
        } else if (response?.followStatus === "REQUESTED") {
          setCurrentFollowStatus("REQUESTED");
        }
      },
      onError: () => {
        message.error("Error following user:");
      },
    });
  };

  const mapperSuggestType = (suggestType: string) => {
    switch (suggestType) {
      case "COMMON_ROOM": {
        const { repName, count } = commonRoomRelation;
        return (
          `Connected through ${repName}'s room` +
          (count > 1 ? ` + ${count - 1} More` : "")
        );
      }
      case "MUTUAL_FOLLOWING": {
        const { repName, count } = mutualFollowingRelation;
        return (
          `Followed by ${repName}` + (count > 1 ? ` + ${count - 1} More` : "")
        );
      }
      case "MUTUAL_FRIEND": {
        const { repName, count } = mutualFriendRelation;
        return (
          `Followed by ${repName}` + (count > 1 ? ` + ${count - 1} More` : "")
        );
      }
      default:
        return "Suggested for you";
    }
  };

  return (
    <div
      className="w-full flex items-start gap-3 my-3"
      style={{
        paddingBottom: "5px",
      }}
    >
      <div
        style={{
          position: "relative",
        }}
      >
        <Avatar size={45} src={avatarUrl || default_avatar} />
      </div>
      <div
        className="flex items-center justify-between w-full pb-5 gap-2"
        style={{
          paddingBottom: "10px",
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
            title={mapperSuggestType(suggestType)}
          >
            <div className="text-[14px] text-gray-500">
              {mapperSuggestType(suggestType)}
            </div>
          </div>
        </div>
        <Button
          style={{
            color:
              currentFollowStatus === "FOLLOWING"
                ? "#bdbdbd"
                : currentFollowStatus === "REQUESTED"
                ? "#9e9e9e"
                : "black",
            fontWeight: 500,
            flexShrink: 0,
            width: "80px",
            borderRadius: "8px",
            padding: "10px 0px",
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
export default SuggestionItem;
