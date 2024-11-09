import React, { useEffect, useState } from "react";
import Post from "./Post";
import { useGetAllPendingPostOnWall } from "../../../../hooks/post";
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

interface IProps {
  isCurrentUser: boolean;
}

const PendingPostOnWall: React.FC<IProps> = ({ isCurrentUser }) => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const { dataPost, isLoading, hasNextPost } = useGetAllPendingPostOnWall({
    limit: 10,
    after: afterTime,
  });
  const [allPosts, setAllPosts] = useState<PostType[]>([]);

  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPendingPostOnWall(),
      });
    }
  };

  useEffect(() => {
    setAllPosts([]);
    setAfterTime(null);
  }, []);

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

  if (!isCurrentUser) {
    return (
      <div
        className="flex justify-center text-[16px]"
        style={{ fontWeight: 450, marginTop: "25vh", color: "#999999" }}
      >
        You can't see this section
      </div>
    );
  }
  return (
    <>
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
            idOfCreator,
          } = post;
          return (
            <Post
              key={id}
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
              disableReactButton={true}
            />
          );
        })
      )}
    </>
  );
};

export default PendingPostOnWall;