import { Button, Image } from "antd";
import React, {useEffect, useState} from "react";
import default_image from "../../../assets/images/default_image.jpg";
import ModalEditProfile from "./modal-edit-profile";
import { useFollowUser, useUnfollowUser } from "../../../hooks/user";
import "../../../assets/css/user-profile.css";
import ModalConfirmUnfollow from "./posts/modal-confirm-unfollow.tsx";

interface IProps {
  isCurrentUser: boolean;
  followStatus: string;
  setFollowStatus: (value: string) => void;
  publicProfile: {
    avatarUrl: string | null;
    bio: string | null;
    firstName: string | null;
    lastName: string | null;
    numberOfFollowers: number;
    privateProfile: boolean;
    username: string;
  };
  userId: string;
}

const UserProfile: React.FC<IProps> = (props) => {

  const { publicProfile, followStatus, setFollowStatus } = props;
  const [numberOfFollowers, setNumberOfFollowers] = useState<number>(publicProfile.numberOfFollowers);

  console.log("numberOfFollowers", numberOfFollowers);

  const [openModal, setOpenModal] = useState(false);

  const [confirmUnfollowModal, setConfirmUnfollowModal] = useState(false); // New state for the confirmation modal

  const { mutate: followUser } = useFollowUser();
  const { mutate: unfollowUser } = useUnfollowUser();

  useEffect(() => {
    setFollowStatus(props?.followStatus);
    setNumberOfFollowers(props?.publicProfile?.numberOfFollowers);
  }, [props?.followStatus, props?.publicProfile, setFollowStatus]);

  const handleFollowUser = () => {
    followUser(props.userId, {
      onSuccess: (response) => {
        if (response?.followStatus === "REQUESTED") {
          setFollowStatus("REQUESTED");
        } else if (response?.followStatus === "FOLLOWING") {
          setFollowStatus("FOLLOWING");
          setNumberOfFollowers(prevState => prevState + 1);
        }
      },
      onError: (error: any) => {
        console.error("Error following user:", error);
      },
    });
  }

  const handleUnfollowUser = () => {
    // Show the confirmation modal if the profile is private
    if (publicProfile.privateProfile && followStatus === "FOLLOWING") {
      setConfirmUnfollowModal(true);
    } else {
      proceedWithUnfollow();
    }
  };

  const proceedWithUnfollow = () => {
    unfollowUser(props.userId, {
      onSuccess: () => {
        setFollowStatus("NOT_FOLLOWING");
        setNumberOfFollowers(prevState => prevState - 1);
      },
      onError: (error: any) => {
        console.error("Error unfollowing user:", error);
      },
    });
  };

  return (
    <>
      {/* Profile Information */}
      <div className="px-5 pt-3 flex flex-col gap-3">
        <div className="flex justify-between items-center">
          <div>
            {publicProfile.firstName || publicProfile.lastName ? (
              <div className="text-[22px] font-bold">
                {`${publicProfile.firstName || ""} ${
                  publicProfile.lastName || ""
                }`}
              </div>
            ) : (
              <div className="text-[22px] font-bold">
                {publicProfile.username}
              </div>
            )}
            <div>{publicProfile.username}</div>
          </div>
          <div
            className="w-[80px] h-[80px]"
            style={{
              borderRadius: "50%",
              position: "relative",
            }}
          >
            <Image
              src={publicProfile.avatarUrl || default_image}
              width={80}
              height={80}
              style={{
                borderRadius: "50%",
                position: "absolute",
                top: 0,
                left: 0,
                objectFit: "cover",
                objectPosition: "center",
              }}
              id="avatar-profile"
            />
          </div>
        </div>

        <div className="text-[15px]">{publicProfile.bio || ""}</div>
        <div className="text-[#a1a1a1]">
          {numberOfFollowers} followers
        </div>

        {/* Follow/Edit Profile Buttons */}
        <div>
          {props.isCurrentUser ? (
            <Button
              className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white] my-2"
              onClick={() => setOpenModal(true)}
            >
              Edit profile
            </Button>
          ) : (
              followStatus && (
                <div className="flex space-x-2 my-2">
                { followStatus === "FOLLOWING" ? (
                  <Button
                    className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]"
                    onClick={handleUnfollowUser}
                  >
                    Following
                  </Button>
                  ) : followStatus === "REQUESTED" ? (
                  <Button
                    className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]"
                    onClick={handleUnfollowUser}
                  >
                    Requested
                  </Button>
                  ) : (
                  <Button
                    className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-black text-white follow-btn"
                    onClick={handleFollowUser}
                  >
                    Follow
                  </Button>
                )}
                <Button className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]">
                  Message
                </Button>
              </div>
              )
          )}
        </div>
      </div>

       {/*Modal to edit the profile */}
      {props.isCurrentUser ? (
        <ModalEditProfile openModal={openModal} setOpenModal={setOpenModal} />
      ) : (
        //   Modal to confirm unfollowing a private profile
        <ModalConfirmUnfollow
          username={publicProfile.username}
          proceedWithUnfollow={proceedWithUnfollow}
          confirmUnfollowModal={confirmUnfollowModal}
          setConfirmUnfollowModal={setConfirmUnfollowModal} />
      )}

    </>
  );
};

export default UserProfile;
