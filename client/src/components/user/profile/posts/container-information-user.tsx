import { Image, Spin } from "antd";
import {
  useFollowUser,
  useGetProfileUser,
  useUnfollowUser,
} from "../../../../hooks/user";
import default_image from "../../../../assets/images/default_image.jpg";
import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import ModalConfirmUnfollow from "./modal-confirm-unfollow.tsx";
import { LoadingOutlined } from "@ant-design/icons";

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
interface ContainerInformationUserProps {
  idOfCreator: string;
  parentFollowStatus?: string;
  updateParentFollowStatus?: React.Dispatch<React.SetStateAction<string>>;

  isDetailPostPage?: boolean;
  setAllActivitiesOnDetailPost?: React.Dispatch<
    React.SetStateAction<IActivityItemPostDetail[]>
  >;
}

const ContainerInformationUser: React.FC<ContainerInformationUserProps> = (
  props
) => {
  const {
    idOfCreator,
    parentFollowStatus,
    updateParentFollowStatus,
    isDetailPostPage,
    setAllActivitiesOnDetailPost,
  } = props;
  const { data, isLoading } = useGetProfileUser(idOfCreator || "");
  const navigate = useNavigate();

  const [followStatus, setFollowStatus] = useState<string>(
    data?.followStatus || ""
  );

  const _followStatus = parentFollowStatus || followStatus;
  const _setFollowStatus = updateParentFollowStatus || setFollowStatus;

  const [numberOfFollowers, setNumberOfFollowers] = useState<number>(
    data?.publicProfile?.numberOfFollowers || 0
  );

  const [confirmUnfollowModal, setConfirmUnfollowModal] = useState(false); // New state for the confirmation modal

  const { mutate: followUser } = useFollowUser();
  const { mutate: unfollowUser } = useUnfollowUser();

  useEffect(() => {
    if (data && !isLoading) {
      _setFollowStatus(data.followStatus);
      setNumberOfFollowers(data.publicProfile.numberOfFollowers);
    }
  }, [_setFollowStatus, data, isLoading]);

  const handleFollowUser = () => {
    followUser(idOfCreator, {
      onSuccess: (response) => {
        if (response?.followStatus === "REQUESTED") {
          _setFollowStatus("REQUESTED");
          if (isDetailPostPage) {
            setAllActivitiesOnDetailPost?.((prevState) => {
              return prevState.map((item) => {
                if (item.userId === idOfCreator) {
                  return {
                    ...item,
                    followStatus: "REQUESTED",
                  };
                }
                return item;
              });
            });
          }
        } else if (response?.followStatus === "FOLLOWING") {
          _setFollowStatus("FOLLOWING");
          setNumberOfFollowers((prevState) => prevState + 1);
          if (isDetailPostPage) {
            setAllActivitiesOnDetailPost?.((prevState) => {
              return prevState.map((item) => {
                if (item.userId === idOfCreator) {
                  return {
                    ...item,
                    followStatus: "FOLLOWING",
                  };
                }
                return item;
              });
            });
          }
        }
      },
      onError: (error: Error) => {
        console.error("Error following user:", error);
      },
    });
  };

  const handleUnfollowUser = () => {
    // Show the confirmation modal if the profile is private
    if (data?.publicProfile?.privateProfile && _followStatus === "FOLLOWING") {
      setConfirmUnfollowModal(true);
    } else {
      proceedWithUnfollow();
    }
  };

  const proceedWithUnfollow = () => {
    unfollowUser(idOfCreator, {
      onSuccess: () => {
        if (_followStatus === "FOLLOWING") {
          _setFollowStatus("NOT_FOLLOWING");
          setNumberOfFollowers((prevState) => prevState - 1);
          if (isDetailPostPage) {
            setAllActivitiesOnDetailPost?.((prevState) => {
              return prevState.map((item) => {
                if (item.userId === idOfCreator) {
                  return {
                    ...item,
                    followStatus: "NOT_FOLLOWING",
                  };
                }
                return item;
              });
            });
          }
        } else if (_followStatus === "REQUESTED") {
          _setFollowStatus("NOT_FOLLOWING");
          if (isDetailPostPage) {
            setAllActivitiesOnDetailPost?.((prevState) => {
              return prevState.map((item) => {
                if (item.userId === idOfCreator) {
                  return {
                    ...item,
                    followStatus: "NOT_FOLLOWING",
                  };
                }
                return item;
              });
            });
          }
        }
      },
      onError: (error: Error) => {
        console.error("Error unfollowing user:", error);
      },
    });
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center mt-8 w-full">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  return (
    <>
      <div
        className="w-[300px] h-[full]"
        style={{
          padding: "15px 10px",
        }}
        onClick={() => {
          navigate(`/profile/${idOfCreator}`);
          window.scrollTo(0, 0);
        }}
      >
        <div className="flex justify-between items-center cursor-pointer">
          <div>
            {data?.publicProfile?.firstName || data?.publicProfile?.lastName ? (
              <div className="text-[18px] font-semibold">
                {`${data?.publicProfile?.firstName || ""} ${
                  data?.publicProfile?.lastName || ""
                }`}
              </div>
            ) : (
              <div className="text-[18px] font-semibold">
                {data?.publicProfile?.username}
              </div>
            )}
            <div className="text-[15px]"> {data?.publicProfile?.username}</div>
          </div>
          <div
            className="w-[60px] h-[60px]"
            style={{
              borderRadius: "50%",
              position: "relative",
            }}
          >
            <Image
              src={`${data?.publicProfile?.avatarUrl || default_image}`}
              width={60}
              height={60}
              style={{
                borderRadius: "50%",
                position: "absolute",
                objectFit: "cover",
                objectPosition: "center",
                left: 0,
                top: 0,
              }}
              id="avatar-profile"
              preview={false}
              loading="lazy"
            />
          </div>
        </div>
        <div className="text-[15px] font-normal mt-3 text-left">
          {data?.publicProfile?.bio || ""}
        </div>
        <div className="text-[#a1a1a1] font-normal text-[15px] mt-2">
          {numberOfFollowers || 0} followers
        </div>
      </div>
      {!data?.isCurrentUser &&
        (_followStatus === "NOT_FOLLOWING" ? (
          <button
            className="font-semibold w-full h-[40px] text-[15px] border rounded-[10px] bg-[black] text-[white]"
            onClick={(e) => {
              e.stopPropagation();
              handleFollowUser();
            }}
          >
            Follow
          </button>
        ) : _followStatus === "REQUESTED" ? (
          <button
            className="font-semibold w-full h-[40px] text-[15px] border rounded-[10px] bg-[white]"
            onClick={(e) => {
              e.stopPropagation();
              handleUnfollowUser();
            }}
          >
            Requested
          </button>
        ) : _followStatus === "FOLLOWING" ? (
          <button
            className="font-semibold w-full h-[40px] text-[15px] border rounded-[10px] bg-[white]"
            onClick={(e) => {
              e.stopPropagation();
              handleUnfollowUser();
            }}
          >
            Following
          </button>
        ) : null)}

      {!data?.isCurrentUser && (
        <ModalConfirmUnfollow
          username={data?.publicProfile?.username}
          proceedWithUnfollow={proceedWithUnfollow}
          confirmUnfollowModal={confirmUnfollowModal}
          setConfirmUnfollowModal={setConfirmUnfollowModal}
        />
      )}
    </>
  );
};

export default ContainerInformationUser;
