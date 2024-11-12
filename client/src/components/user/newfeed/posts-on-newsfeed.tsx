import React, { useEffect, useState } from "react";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useFetchAllPostsOnNewsFeed } from "../../../hooks/post";
import Post from "../profile/posts/Post";
import RemoveDuplicatePost from "../../../utils/removeDuplicatePost";
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
  nameOfRoom: string;
}

const PostsOnNewsFeed: React.FC = () => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<PostType[]>([]);
  const { dataPost, isLoading, hasNextPost } = useFetchAllPostsOnNewsFeed({
    limit: 10,
    after: afterTime,
  });

  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPostsOnNewsFeed(),
      });
    }
  };
  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts: PostType[]) =>
        RemoveDuplicatePost([
          ...prevPosts,
          ...dataPost.map((post: PostType) => ({
            ...post,
            postImageUrls: post.postImageUrls || [],
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

  return (
    <>
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
            nameOfRoom,
          } = post;
          return (
            <Post
              key={id}
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
              idOfCreator={idOfCreator}
              nameOfRoom={nameOfRoom}
            />
          );
        })}
    </>
  );
};
export default PostsOnNewsFeed;
