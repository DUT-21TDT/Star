import { Button, Image } from "antd";
import React, { useState } from "react";
import default_image from "../../../assets/images/default_image.jpg";
import ModalEditProfile from "./modal-edit-profile";
import { useFollowUser, useUnfollowUser } from "../../../hooks/user";
import "../../../assets/css/user-profile.css";

interface IProps {
  isCurrentUser: boolean;
  followStatus: string;
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
  const { publicProfile } = props;
  const [openModal, setOpenModal] = useState(false);

  const [followStatus, setFollowStatus] = useState(props.followStatus);

  const { mutate: followUser } = useFollowUser();
  const { mutate: unfollowUser } = useUnfollowUser();

  const handleFollowUser = () => {
    console.log("followStatus", followStatus);
    if (followStatus === "NOT_FOLLOWING") {
      followUser(props.userId, {
        onSuccess: () => {
          setFollowStatus(
            publicProfile.privateProfile ? "REQUESTED" : "FOLLOWING"
          );
        },
        onError: (error: any) => {
          console.error("Error following user:", error);
        },
      });
    } else if (followStatus === "REQUESTED" || followStatus === "FOLLOWING") {
      unfollowUser(props.userId, {
        onSuccess: () => {
          setFollowStatus("NOT_FOLLOWING");
        },
        onError: (error: any) => {
          console.error("Error unfollowing user:", error);
        },
      });
    }
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
          {publicProfile.numberOfFollowers} followers
        </div>

        {/* Follow/Edit Profile Buttons */}
        <div>
          {props.isCurrentUser ? (
            <Button
              className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]"
              onClick={() => setOpenModal(true)}
            >
              Edit profile
            </Button>
          ) : (
            <>
              {followStatus === "FOLLOWING" ? (
                <Button
                  className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]"
                  onClick={handleFollowUser}
                >
                  Following
                </Button>
              ) : followStatus === "REQUESTED" ? (
                <Button
                  className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]"
                  onClick={handleFollowUser}
                >
                  Requested
                </Button>
              ) : (
                <div className="flex space-x-2">
                  <Button
                    className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-black text-white follow-btn"
                    onClick={handleFollowUser}
                  >
                    Follow
                  </Button>
                  <Button className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]">
                    Mention
                  </Button>
                </div>
              )}
            </>
          )}
        </div>
      </div>

      {/* Modal to edit the profile */}
      {props.isCurrentUser && (
        <ModalEditProfile openModal={openModal} setOpenModal={setOpenModal} />
      )}
    </>
  );
};

export default UserProfile;
