import { Avatar, Button, Input } from "antd";
import React, { useState } from "react";
import default_image from "../../../../assets/images/default_image.jpg";
import { useAppSelector } from "../../../../redux/store/hook";
import ModalCreatePost from "./modal-create-post";

const CreatePost: React.FC = () => {
  const publicProfile = useAppSelector((state) => state.user);
  const [isModalOpen, setIsModalOpen] = useState(false);
  return (
    <>
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
          onClick={() => setIsModalOpen(true)}
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
          onClick={() => setIsModalOpen(true)}
        >
          Post
        </Button>
        <ModalCreatePost
          isModalOpen={isModalOpen}
          setIsModalOpen={setIsModalOpen}
        />
      </div>
    </>
  );
};
export default CreatePost;
