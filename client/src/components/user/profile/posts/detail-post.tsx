import { useParams } from "react-router-dom";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import Post from "./Post";
import ListReplyPost from "./list-reply-post";
import React from "react";

interface PostDetail {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string;
  createdAt: string;
  content: string;
  postImageUrls: string[];
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  nameOfRoom: string;
  idOfCreator: string;
}

interface IProps {
  dataPostDetail: PostDetail;
  isLoading: boolean;
  isError: boolean;
}

const MainContentDetailPost : React.FC<IProps> = (props) => {
  const postId = useParams<{ postId: string }>().postId || "";

  const {
    dataPostDetail,
    isLoading,
    isError,
  } = props;

  const {
    id,
    usernameOfCreator,
    avatarUrlOfCreator,
    createdAt,
    content,
    postImageUrls,
    numberOfLikes,
    numberOfComments,
    numberOfReposts,
    liked,
    nameOfRoom,
    idOfCreator,
  } = dataPostDetail;

  if (isLoading) {
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  if (isError) {
    return <div>Something went wrong</div>;
  }

  return (
    <>
      <div className="px-[20px]">
        <Post
          id={id}
          usernameOfCreator={usernameOfCreator}
          avatarUrlOfCreator={avatarUrlOfCreator}
          createdAt={createdAt}
          content={content}
          postImageUrls={postImageUrls}
          numberOfLikes={numberOfLikes}
          numberOfComments={numberOfComments}
          numberOfReposts={numberOfReposts}
          liked={liked}
          nameOfRoom={nameOfRoom}
          idOfCreator={idOfCreator}
        />
      </div>
      <div
        className="w-full flex justify-between items-center py-3 px-[20px]"
        style={{
          borderBottom: "1px solid #f0f0f0",
        }}
      >
        <div className="font-[500] text-[15px]">Replies</div>
        <div className=" text-[15px] text-[#c3c3c3] hover:cursor-pointer">
          View activity
        </div>
      </div>
      <ListReplyPost postId={postId} />
    </>
  );
};

export default MainContentDetailPost;
