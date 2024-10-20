import React, { useState } from "react";
import { useLikePost, useUnlikePost } from "../../../../hooks/post";
import { message } from "antd";

interface IProps {
  postId: string;
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
}
const ReactButton: React.FC<IProps> = ({
  postId,
  numberOfLikes,
  numberOfComments,
  numberOfReposts,
  liked,
}) => {

  const [isLiked, setIsLiked] = useState(liked);
  const [likesCount, setLikesCount] = useState(numberOfLikes);

  const [selectedButton, setSelectedButton] = useState<string>("");

  const { mutate: likePost } = useLikePost();
  const { mutate: unlikePost } = useUnlikePost();

  const handleLikeToggle = () => {
    if (isLiked) {
      unlikePost(
        postId, // assuming postId is needed to unlike the post
        {
          onSuccess: () => {
            setIsLiked(false);
            setLikesCount((prev) => prev - 1); // Decrease like count
          },
          onError: (error: any) => {
            if (error.response?.status === 404) {
              setIsLiked(false);
              setLikesCount((prev) => prev - 1); // Decrease like count
            }
          }
        }
      );
    } else {
      likePost(
        postId, // assuming postId is needed to like the post
        {
          onSuccess: () => {
            setIsLiked(true);
            setLikesCount((prev) => prev + 1); // Increase like count
          },
          onError: (error: any) => {
            if (error.response?.status === 409) {
              setIsLiked(true);
              setLikesCount((prev) => prev + 1); // Increase like count
            } else if (error.response?.status === 404) {
              message.error("The post you are trying to like does not exist");
            }
          }
        }
      );
    }
  };

  return (
    <div className="flex items-center gap-2" style={{ userSelect: "none" }}>
      <div
        className={`min-w-9 w-fit h-[35px] cursor-pointer rounded-[40%] flex items-center justify-center hover:bg-[#efefef] p-2 gap-1 transition-all ease-in-out duration-[180] ${selectedButton == "like" ? "scale-[.80]" : ""}`} // Apply scale on press
        onMouseDown={() => setSelectedButton("like")}
        onMouseUp={() => setSelectedButton("")}
        onMouseLeave={() => setSelectedButton("")}
        onClick={handleLikeToggle}
      >
        <svg
          className="transition-colors duration-[180]"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill={isLiked ? "#ff0034" : "none"}
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M18.4999 4.96676C15.7806 2.79127 12.9999 4.96675 11.9999 5.96675C10.9999 4.96675 8.2193 2.79127 5.49996 4.96676C2.78062 7.14224 2.18961 11.6564 5.99996 15.4667C9.81031 19.2771 11.9999 19.9667 11.9999 19.9667C11.9999 19.9667 14.1896 19.2771 17.9999 15.4667C21.8103 11.6564 21.2193 7.14224 18.4999 4.96676Z"
            stroke={isLiked ? "#fa1e48" : "black"}
            strokeWidth="1"
          />
        </svg>
        <span>{likesCount !== 0 ? likesCount : ""}</span>
      </div>

      <div
        className={`min-w-9 w-fit h-[35px] cursor-pointer rounded-[40%] flex items-center justify-center hover:bg-[#efefef] p-2 gap-1 transition-all ease-in-out duration-[180] ${selectedButton == "comment" ? "scale-[.80]" : ""}`} // Apply scale on press
        onMouseDown={() => setSelectedButton("comment")}
        onMouseUp={() => setSelectedButton("")}
        onMouseLeave={() => setSelectedButton("")}
      >
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M4 12C4 16.4183 7.58172 20 12 20C13.2552 20 14.4428 19.7109 15.5 19.1958L19.5 20L19 15.876C19.6372 14.7278 20 13.4063 20 12C20 7.58172 16.4183 4 12 4C7.58172 4 4 7.58172 4 12Z"
            stroke="black"
            strokeWidth="1"
          />
        </svg>
        <span>{numberOfComments !== 0 ? numberOfComments : ""}</span>
      </div>
      <div
        className={`min-w-9 w-fit h-[35px] cursor-pointer rounded-[40%] flex items-center justify-center hover:bg-[#efefef] p-2 gap-1 transition-all ease-in-out duration-[180] ${selectedButton == "repost" ? "scale-[.80]" : ""}`} // Apply scale on press
        onMouseDown={() => setSelectedButton("repost")}
        onMouseUp={() => setSelectedButton("")}
        onMouseLeave={() => setSelectedButton("")}
      >        <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
          <path
            d="M5 13.5V9C5 7.34315 6.34315 6 8 6H15.5M15.5 6L12.5 3M15.5 6L12.5 9M19 10.5V15C19 16.6569 17.6569 18 16 18L8.5 18M8.5 18L11.5 21M8.5 18L11.5 15"
            stroke="black"
            strokeWidth="1"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
        {numberOfReposts !== 0 ? numberOfReposts : ""}
      </div>
    </div>
  );
};
export default ReactButton;
