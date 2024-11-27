import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import CreatePost from "./create-post";
import Post from "./Post";
import { useFetchAllPostsOnWall } from "../../../../hooks/post";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";
import RemoveDuplicatePost from "../../../../utils/removeDuplicatePost";

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
  reposted: boolean;
  idOfCreator: string;
  nameOfRoom: string;
  isRemoved?: boolean;
}

interface IProps {
  isCurrentUser: boolean;
}

const PostOnWall: React.FC<IProps> = ({ isCurrentUser }) => {
  const { id } = useParams();
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
      setAllPosts((prevPosts: PostType[]) =>
        RemoveDuplicatePost([
          ...prevPosts,
          ...dataPost.map((post: PostType) => ({
            ...post,
            postImageUrls: post.postImageUrls || [],
            isRemoved: false,
          })),
        ])
      );
      const lastPost = dataPost[dataPost.length - 1];
      setAfterTime(lastPost.createdAt);
    }
  }, [dataPost]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost]);

  const handleDeletePostSuccess = (postId: string) => {
    setAllPosts((prevPosts) => {
      return prevPosts.map((post) => {
        if (post.id === postId) {
          return {
            ...post,
            isRemoved: true,
          };
        }
        return post;
      });
    });
  };

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: QUERY_KEY.fetchAllPostsOnWall(id || ""),
      });
    };
  }, []);

  return (
    <>
      {isCurrentUser && <CreatePost />}
      {isLoading ? (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      ) : allPosts.length === 0 ? (
        <div
          className="flex items-center justify-center text-xl"
          style={{ fontWeight: 450, marginTop: "25vh", color: "#999999" }}
        >
          No posts yet
        </div>
      ) : (
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
            reposted,
            idOfCreator,
            nameOfRoom,
            isRemoved,
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
              reposted={reposted}
              nameOfRoom={nameOfRoom}
              handleDeletePostSuccess={handleDeletePostSuccess}
              isRemoved={isRemoved}
            />
          );
        })
      )}
    </>
  );
};

export default PostOnWall;
