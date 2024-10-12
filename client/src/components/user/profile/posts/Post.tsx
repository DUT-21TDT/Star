import { Avatar, Button, Carousel, Image, Popover } from "antd";
import React from "react";
import { EllipsisOutlined } from "@ant-design/icons";
import ReactButton from "./react-button";
import "../../../../assets/css/posts.css";
import default_image from "../../../../assets/images/default_image.jpg";
import { timeAgo } from "../../../../utils/convertTime";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../../redux/store/hook";

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
  idOfCreator?: string;
  firstName?: string;
  lastName?: string;
  bio?: string;
  numberOfFollowers?: number;
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
    idOfCreator,
    firstName,
    lastName,
    bio,
    numberOfFollowers,
  } = props;

  const navigate = useNavigate();

  const handleNavigateProfileUser = (id: string) => () => {
    if (id) {
      navigate(`/profile/${id}`);
    }
  };
  const userId = useAppSelector((state) => state.user.id);

  const containerInformationUser = () => {
    return (
      <div
        className="w-[300px] h-[full]"
        style={{
          padding: "15px 10px",
        }}
        onClick={handleNavigateProfileUser(idOfCreator || "")}
      >
        <div className="flex justify-between items-center cursor-pointer">
          <div>
            {firstName || lastName ? (
              <div className="text-[18px] font-semibold">
                {`${firstName || ""} ${lastName || ""}`}
              </div>
            ) : (
              <div className="text-[18px] font-semibold">
                {usernameOfCreator}
              </div>
            )}
            <div className="text-[15px]">{usernameOfCreator}</div>
          </div>
          <div
            className="w-[60px] h-[60px]"
            style={{
              borderRadius: "50%",
              position: "relative",
            }}
          >
            <Image
              src={`${avatarUrlOfCreator || default_image}`}
              width={60}
              height={60}
              style={{
                borderRadius: "50%",
              }}
              id="avatar-profile"
            />
          </div>
        </div>
        <div className="text-[15px] font-normal mt-3 text-left">
          {bio || ""}
        </div>
        <div className="text-[#a1a1a1] font-normal text-[15px]">
          {numberOfFollowers || 0} followers
        </div>
        {userId !== idOfCreator && (
          <button className="font-semibold w-full h-[35px] text-[15px] border rounded-[10px] bg-[black] text-[white] mt-2">
            Follow
          </button>
        )}
      </div>
    );
  };

  return (
    <div
      className="p-3"
      style={{
        borderBottom: "1px solid #f0f0f0",
        display: "flex",
        gap: "10px",
      }}
    >
      <Popover
        content={containerInformationUser}
        placement="bottomLeft"
        trigger="hover"
        overlayClassName="custom-popover"
        arrow={false}
      >
        <Avatar
          style={{
            width: "45px",
            height: "45px",
            cursor: "pointer",
          }}
          src={avatarUrlOfCreator || default_image}
          onClick={handleNavigateProfileUser(idOfCreator || "")}
        />
      </Popover>
      <div style={{ width: "calc(100% - 65px)" }}>
        <div className="flex justify-between w-full">
          <Popover
            content={containerInformationUser}
            placement="bottomLeft"
            trigger="hover"
            overlayClassName="custom-popover"
            arrow={false}
          >
            <div
              style={{
                fontWeight: "500",
                fontSize: "16px",
                cursor: "pointer",
              }}
              onClick={handleNavigateProfileUser(idOfCreator || "")}
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
          </Popover>
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
