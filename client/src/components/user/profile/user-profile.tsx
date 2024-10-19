import { Button, Image } from "antd";
import React, { useState } from "react";
import default_image from "../../../assets/images/default_image.jpg";
import ModalEditProfile from "./modal-edit-profile";
import "../../../assets/css/user-profile.css";
interface IProps {
  isCurrentUser: boolean;
  isFollowing: boolean;
  publicProfile: {
    avatarUrl: string | null;
    bio: string | null;
    firstName: string | null;
    lastName: string | null;
    numberOfFollowers: number;
    privateProfile: boolean;
    username: string;
  };
}
const UserProfile: React.FC<IProps> = (props) => {
  const { publicProfile } = props;
  const [openModal, setOpenModal] = useState(false);
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
              src={`${publicProfile.avatarUrl || default_image}`}
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
        <div>
          <Button
            className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[white]"
            onClick={() => setOpenModal(true)}
          >
            Edit profile
          </Button>
        </div>
      </div>
      <ModalEditProfile openModal={openModal} setOpenModal={setOpenModal} />
    </>
  );
};
export default UserProfile;
