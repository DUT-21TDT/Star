import React, { useCallback, useEffect, useState } from "react";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useGetAllPostsInRoom } from "../../../hooks/post";
import Post from "../profile/posts/Post";
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
  roomId: string;
}

const PostsInRoom: React.FC<IProps> = ({ roomId }) => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const { dataPost, isLoading, hasNextPost } = useGetAllPostsInRoom(roomId, {
    limit: 10,
    after: afterTime,
  });
  const [allPosts, setAllPosts] = useState<PostType[]>([]);

  const fetchMorePosts = useCallback(() => {
    if (hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPostsInRoom(roomId),
      });
    }
  }, [queryClient, roomId, hasNextPost]);

  const handleScroll = useCallback(() => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom) fetchMorePosts();
  }, [fetchMorePosts]);

  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts) => [
        ...prevPosts,
        ...dataPost.map((post: PostType) => ({
          ...post,
          postImageUrls: post.postImageUrls || [],
          isRemoved: false,
        })),
      ]);
      const lastPost = dataPost[dataPost.length - 1];
      setAfterTime(lastPost.createdAt);
    }
  }, [dataPost]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [handleScroll]);

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
        queryKey: QUERY_KEY.fetchAllPostsInRoom(roomId),
      });
    };
  }, []);
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
            reposted,
            idOfCreator,
            nameOfRoom,
            isRemoved,
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
              reposted={reposted}
              idOfCreator={idOfCreator}
              nameOfRoom={nameOfRoom}
              isRemoved={isRemoved}
              handleDeletePostSuccess={handleDeletePostSuccess}
            />
          );
        })}
    </>
  );
};
export default PostsInRoom;
