import React, { useEffect, useState } from "react";
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
  idOfCreator: string;
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
  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPostsInRoom(roomId),
      });
    }
  };
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
    return () => {
      setAllPosts([]);
      window.removeEventListener("scroll", handleScroll);
    };
  }, [hasNextPost, roomId]);
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
            />
          );
        })}
    </>
  );
};
export default PostsInRoom;
