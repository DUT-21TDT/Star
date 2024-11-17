import {Button, Divider, message, Popover, Image} from "antd";
import default_image from "../../../assets/images/default_image.jpg";
import ContainerInformationUser from "../profile/posts/container-information-user.tsx";
import {useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {useAppSelector} from "../../../redux/store/hook";
import {useFollowUser, useUnfollowUser} from "../../../hooks/user";

interface IPeopleType {
  userId: string;
  username: string;
  avatarUrl: string | null;
  firstName: string | null;
  lastName: string | null;
  followStatus: string;
  numberOfFollowers: number;
}

const PeopleItem: React.FC<IPeopleType> = ({
                                             avatarUrl,
                                             firstName,
                                             followStatus,
                                             lastName,
                                             userId,
                                             username,
                                             numberOfFollowers,
                                           }) => {
  const navigate = useNavigate();

  const [currentFollowStatus, setCurrentFollowStatus] = useState<string>(followStatus);

  useEffect(() => {
    setCurrentFollowStatus(followStatus);
  }, [followStatus]);

  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] = useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(<div></div>);

  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={userId || ""} parentFollowStatus={currentFollowStatus}
                                  updateParentFollowStatus={setCurrentFollowStatus}/>
      );
    }
  };

  useEffect(() => {
    if (isPopoverVisibleUsername) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={userId || ""} parentFollowStatus={currentFollowStatus}
                                  updateParentFollowStatus={setCurrentFollowStatus}/>
      );
    }
  }, [currentFollowStatus, isPopoverVisibleUsername, userId]);

  const isCurrentPeopleItem =
    userId === useAppSelector((state) => state.user.id);

  const {mutate: unfollowUser} = useUnfollowUser();
  const {mutate: followUser} = useFollowUser();

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

  return (
    <div key={userId} style={{cursor: "pointer"}}>
      <div className="flex items-center justify-between w-full">
        <div className="flex items-start">
          <Image
            src={avatarUrl || default_image}
            width={40}
            height={40}
            style={{
              borderRadius: "50%",
              position: "absolute",
              objectFit: "cover",
              objectPosition: "center",
              left: 0,
              top: 0,
              marginTop: "4px",
            }}
            preview={false}
          />
          <div className="ml-3">
            <Popover
              content={popoverContent}
              placement="bottomLeft"
              trigger="hover"
              overlayClassName="custom-popover"
              arrow={false}
              open={isPopoverVisibleUsername}
              onOpenChange={handlePopoverUsernameVisibilityChange}
            >
              <p
                className="text-[16px] font-semibold"
                onClick={() => navigate(`/profile/${userId}`)}
              >
                {username}
              </p>
            </Popover>
            <p
              className="text-gray-400 text-[14px] mt-[2px]"
              style={{lineHeight: "18px"}}
            >
              {firstName && lastName ?
                `${firstName} ${lastName}` :
                firstName || lastName ?
                  firstName || lastName :
                  username}
            </p>
            <div className="mt-2 text-[15px]">
              {numberOfFollowers} followers
            </div>
          </div>
        </div>

        <div>
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
              borderRadius: "16px",
              padding: "10px 0px",
            }}
            onClick={() => {
              if (
                currentFollowStatus === "FOLLOWING" ||
                currentFollowStatus === "REQUESTED"
              ) {
                handleUnfollowUser(userId);
              } else {
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

      <Divider style={{margin: "8px 0px"}}/>
    </div>
  );
};
export default PeopleItem;
