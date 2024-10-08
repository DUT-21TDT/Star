import { Avatar, Button, Carousel, Image } from "antd";
import React from "react";
import { EllipsisOutlined } from "@ant-design/icons";
import ReactButton from "./react-button";
import "../../../../assets/css/posts.css";
import default_image from "../../../../assets/images/default_image.jpg";
import { timeAgo } from "../../../../utils/convertTime";
interface IProps {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
}
const Post: React.FC<IProps> = (props) => {
  const {
    avatarUrlOfCreator,
    createdAt,
    content,
    postImageUrls,
    usernameOfCreator,
    numberOfLikes,
    numberOfComments,
    numberOfReposts,
    liked,
  } = props;

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
        src={avatarUrlOfCreator || default_image}
      />
      <div style={{ width: "calc(100% - 65px)" }}>
        <div className="flex justify-between w-full">
          <div
            style={{
              fontWeight: "500",
              fontSize: "16px",
            }}
          >
            {usernameOfCreator}{" "}
            <span
              style={{
                color: "rgb(153,153,153)",
                fontSize: "14px",
              }}
            >
              {timeAgo(createdAt)}
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
            {content}
          </p>
        </div>
        {postImageUrls && postImageUrls.length > 0 && (
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
              {postImageUrls.map((url) => (
                <Image
                  key={url}
                  src={url}
                  style={{
                    objectFit: "cover",
                    objectPosition: "center",
                  }}
                  width={"100%"}
                  height={320}
                />
              ))}
            </Carousel>
          </div>
        )}

        <ReactButton
          numberOfLikes={numberOfLikes}
          numberOfComments={numberOfComments}
          numberOfReposts={numberOfReposts}
          liked={liked}
        />
      </div>
    </div>
  );
};
export default Post;
