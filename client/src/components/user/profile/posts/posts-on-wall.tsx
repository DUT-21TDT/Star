import React from "react";
import { useGetProfileUser } from "../../../../hooks/user";
import { useParams } from "react-router-dom";
import CreatePost from "./create-post";
import Post from "./Post";

const PostOnWall: React.FC = () => {
  const { id } = useParams();
  const { data } = useGetProfileUser(id || "");

  return (
    <>
      <CreatePost
        isCurrentUser={data?.isCurrentUser}
        isFollowing={data?.isFollowing}
        publicProfile={data?.publicProfile}
      />
      <Post
        isCurrentUser={data?.isCurrentUser}
        isFollowing={data?.isFollowing}
        publicProfile={data?.publicProfile}
      />
    </>
  );
};
export default PostOnWall;
