import React, { useEffect, useState } from "react";
import { useGetProfileUser } from "../../../../hooks/user";
import { useParams } from "react-router-dom";
import CreatePost from "./create-post";
import Post from "./Post";
import { useFetchAllPostsOnWall } from "../../../../hooks/post";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";
interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[];
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  idOfCreator: string;
}

const PostOnWall: React.FC = () => {
  const { id } = useParams();
  const { data } = useGetProfileUser(id || "");
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const { dataPost, isLoading, hasNextPost } = useFetchAllPostsOnWall(
    id || "",
    {
      limit: 10,
      after: afterTime,
    }
  );
  const [allPosts, setAllPosts] = useState<PostType[]>([]);

  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPostsOnWall(id || ""),
      });
    }
  };
  useEffect(() => {
    setAllPosts([]);
    setAfterTime(null);
  }, [id]);
  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts: PostType[]) => [
        ...prevPosts,
        ...dataPost.map((post: PostType) => ({
          ...post,
          postImageUrls: post.postImageUrls || [],
        })),
      ]);
      const lastPost = dataPost[dataPost.length - 1];
      setAfterTime(lastPost.createdAt);
    }
  }, [dataPost]);
  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost]);
  return (
    <>
      <CreatePost
        isCurrentUser={data?.isCurrentUser}
        isFollowing={data?.isFollowing}
        publicProfile={data?.publicProfile}
      />
      {isLoading && (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      )}
      {allPosts &&
        allPosts.length > 0 &&
        allPosts.map((post) => {
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
            idOfCreator,
          } = post;
          const getTime = new Date(createdAt).getTime();
          return (
            <Post
              key={`${id}-${getTime}`}
              id={id}
              usernameOfCreator={usernameOfCreator}
              avatarUrlOfCreator={avatarUrlOfCreator}
              idOfCreator={idOfCreator}
              createdAt={createdAt}
              content={content}
              postImageUrls={postImageUrls}
              numberOfLikes={numberOfLikes}
              numberOfComments={numberOfComments}
              numberOfReposts={numberOfReposts}
              liked={liked}
            />
          );
        })}
    </>
  );
};
export default PostOnWall;
