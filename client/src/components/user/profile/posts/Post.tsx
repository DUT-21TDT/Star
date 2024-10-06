import { Avatar, Button, Carousel, Image } from "antd";
import React from "react";
import { EllipsisOutlined } from "@ant-design/icons";
import ReactButton from "./react-button";
import "../../../../assets/css/posts.css";
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
const Post: React.FC<IProps> = ({ publicProfile }) => {
  return (
    <div
      className="p-3"
      style={{
        borderBottom: "1px solid #f0f0f0",
        display: "flex",
        gap: "10px",
      }}
    >
      <Avatar
        style={{
          width: "45px",
          height: "45px",
        }}
        src={publicProfile?.avatarUrl}
      />
      <div style={{ width: "calc(100% - 65px)" }}>
        <div className="flex justify-between w-full">
          <div
            style={{
              fontWeight: "500",
              fontSize: "16px",
            }}
          >
            __thuchoang__{" "}
            <span
              style={{
                color: "rgb(153,153,153)",
                fontSize: "14px",
              }}
            >
              3m
            </span>
          </div>
          <Button
            icon={<EllipsisOutlined />}
            style={{ borderRadius: "50%", width: "25px", height: "25px" }}
          />
        </div>
        <div className="py-2">
          <p
            style={{
              lineHeight: "20px",
              fontSize: "15px",
              textAlign: "left",
            }}
          >
            Lorem ipsum, dolor sit amet consectetur adipisicing elit.
            Reprehenderit, eum provident harum recusandae autem assumenda sed
            esse tenetur saepe. Repudiandae cumque eligendi quidem ipsam tenetur
            alias aliquam iusto vel iste?
          </p>
        </div>
        <div
          style={{
            height: "320px",
            width: "100%",
            margin: "10px 0px",
            display: "flex",
            gap: "10px",
          }}
        >
          <Carousel arrows>
            <Image
              src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqGK3diR3Zi-mnOXEaj-3ewmFyRYVxGzVzZw&s"
              style={{
                objectFit: "cover",
                objectPosition: "center",
              }}
              width={"100%"}
              height={320}
            />
            <Image
              src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqGK3diR3Zi-mnOXEaj-3ewmFyRYVxGzVzZw&s"
              style={{
                objectFit: "cover",
                objectPosition: "center",
              }}
              width={"100%"}
              height={320}
            />
          </Carousel>
        </div>
        <ReactButton />
      </div>
    </div>
  );
};
export default Post;
