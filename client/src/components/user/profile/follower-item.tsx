import { Button, Divider, message, Popover } from "antd";
import default_image from "../../../assets/images/default_image.jpg";
import ContainerInformationUser from "./posts/container-information-user";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useAppSelector } from "../../../redux/store/hook";
import {
  useAcceptFollowRequest,
  useDeleteFollowerByUserId,
} from "../../../hooks/follow";
import { useFollowUser, useUnfollowUser } from "../../../hooks/user";
interface IFollowerType {
  avatarUrl: string;
  firstName: string;
  followAt: string;
  followStatus: string;
  lastName: string;
  userId: string;
  username: string;
  followingId?: string;
  tab?: string;
}

interface IProps {
  setIsOpenModalRequestFollow: (value: boolean) => void;
  currentProfileId: string;
  setAllFollowers: React.Dispatch<React.SetStateAction<IFollowerType[]>>;
}
const FollowerItem: React.FC<IFollowerType & IProps> = ({
  avatarUrl,
  firstName,
  followStatus,
  lastName,
  userId,
  username,
  setIsOpenModalRequestFollow,
  currentProfileId,
  followingId,
  tab,
  setAllFollowers,
}) => {
  const navigate = useNavigate();
  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );
  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={userId || ""} />
      );
    }
  };

  const isCurrentUser =
    currentProfileId === useAppSelector((state) => state.user.id);

  const { mutate: removeFollower } = useDeleteFollowerByUserId();
  const { mutate: unfollowUser } = useUnfollowUser();
  const { mutate: followeUser } = useFollowUser();
  const { mutate: acceptFollowRequest } = useAcceptFollowRequest();

  const handleRemoveFollower = () => {
    removeFollower(userId, {
      onSuccess: () => {
        setAllFollowers((prevFollowers) =>
          prevFollowers.filter((follower) => follower.userId !== userId)
        );
      },
      onError: () => {
        message.error("Error removing follower");
      },
    });
  };

  const handleUnFollow = () => {
    unfollowUser(userId, {
      onSuccess: () => {
        setAllFollowers((prevFollowers) =>
          prevFollowers.map((follower) =>
            follower.userId === userId
              ? { ...follower, followStatus: "NOT_FOLLOWING" }
              : follower
          )
        );
      },
      onError: () => {
        message.error("Error unfollowing user");
      },
    });
  };

  const handleAcceptFollowRequest = () => {
    if (followingId) {
      acceptFollowRequest(followingId, {
        onSuccess: () => {
          setAllFollowers((prevFollowers) =>
            prevFollowers.filter(
              (follower) => follower.followingId !== followingId
            )
          );
        },
        onError: () => {
          message.error("Error accept user");
        },
      });
    } else {
      message.error("Invalid following ID");
    }
  };

  const handleFollowerUser = () => {
    followeUser(userId, {
      onSuccess: (response) => {
        setAllFollowers((prevFollowers) =>
          prevFollowers.map((follower) =>
            follower.userId === userId
              ? { ...follower, followStatus: response?.followStatus }
              : follower
          )
        );
      },
      onError: () => {
        message.error("Error following user");
      },
    });
  };

  return (
    <div style={{ cursor: "pointer" }}>
      <div className="flex items-center justify-between w-full">
        <div className="flex items-start">
          <img
            src={avatarUrl || default_image}
            alt="avatar"
            className="w-[40px] h-[40px] rounded-full mr-3 mt-1"
          />
          <div>
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
                onClick={() => {
                  setIsOpenModalRequestFollow(false);
                  navigate(`/profile/${userId}`);
                }}
              >
                {username}
              </div>
            </Popover>

            <p className="text-[16px] mt-[2px] text-gray-500">
              {firstName || lastName ? (
                <div className="text-[16px] mt-[2px] text-gray-500">
                  {`${firstName || ""} ${lastName || ""}`}
                </div>
              ) : (
                <div className="text-[16px] mt-[2px] text-gray-500">
                  {username}
                </div>
              )}
            </p>
          </div>
        </div>

        <div>
          {isCurrentUser && tab === "FOLLOWER" && (
            <Button
              className="w-[100px] h-[35px] p-[15px] rounded-lg font-semibold text-[#ababab] text-[16px] mr-5 "
              onClick={handleRemoveFollower}
            >
              Remove
            </Button>
          )}
          {isCurrentUser && tab === "REQUEST" && (
            <div>
              <Button
                className="w-[90px] h-[35px] p-[15px] rounded-lg font-semibold text-[#ababab] text-[16px] mr-2 "
                onClick={handleRemoveFollower}
              >
                Reject
              </Button>
              <Button
                className="w-[90px] h-[35px] p-[15px] rounded-lg font-semibold text-[#ababab] text-[16px] mr-5 "
                onClick={handleAcceptFollowRequest}
              >
                Accept
              </Button>
            </div>
          )}

          {!isCurrentUser && followStatus === "NOT_FOLLOWING" && (
            <Button
              className="w-[100px] h-[35px] p-[15px] rounded-lg font-semibold text-[#ababab] text-[16px] mr-5 "
              onClick={handleFollowerUser}
            >
              Follow
            </Button>
          )}
          {followStatus === "FOLLOWING" && (
            <Button
              className="w-[100px] h-[35px] p-[15px] rounded-lg font-semibold text-[#ababab] text-[16px] mr-5 "
              onClick={handleUnFollow}
            >
              Following
            </Button>
          )}

          {followStatus === "REQUESTED" && (
            <Button
              className="w-[100px] h-[35px] p-[15px] rounded-lg font-semibold text-[#ababab] text-[16px] mr-5"
              onClick={handleUnFollow}
            >
              Requested
            </Button>
          )}
        </div>
      </div>

      <Divider style={{ margin: "16px 0px 16px 0px" }} />
    </div>
  );
};
export default FollowerItem;
