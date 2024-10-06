import { Avatar, Button, Input } from "antd";
import React from "react";
import default_image from "../../../../assets/images/default_image.jpg";
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
const CreatePost: React.FC<IProps> = ({ publicProfile }) => {
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "50px 1fr auto",
        padding: "5px 15px 15px 15px",
        alignItems: "center",
        borderBottom: "1px solid #f0f0f0",
      }}
    >
      <Avatar
        src={publicProfile?.avatarUrl || default_image}
        style={{
          width: "45px",
          height: "45px",
        }}
      />
      <Input
        type="text"
        placeholder="What's on your mind?"
        style={{
          border: "none",
          backgroundColor: "white",
          fontSize: "16px",
          fontWeight: "400",
          color: "rgb(153,153,153)",
        }}
      />
      <Button
        style={{
          width: "65px",
          height: "35px",
          fontWeight: "500",
          fontSize: "16px",
          lineHeight: "35px",
          borderRadius: "10px",
        }}
      >
        Post
      </Button>
    </div>
  );
};
export default CreatePost;
