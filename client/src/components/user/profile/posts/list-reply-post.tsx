import { useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { useGetRepliesByPostId } from "../../../../hooks/post";
import { QUERY_KEY } from "../../../../utils/queriesKey";
import Post from "./Post";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";

interface IProps {
  postId: string;
}
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
  isRemoved?: boolean;
}
const ListReplyPost: React.FC<IProps> = (props) => {
  const { postId } = props;

  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<PostType[]>([]);
  const { dataPost, isLoading, hasNextPost } = useGetRepliesByPostId(postId, {
    limit: 10,
    after: afterTime,
  });
  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchRepliesByPostId(postId),
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
          isRemoved: false,
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
        queryKey: QUERY_KEY.fetchRepliesByPostId(postId),
      });
    };
  }, [postId]);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  return (
    <div className="mt-4">
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
              idOfCreator={idOfCreator}
              nameOfRoom={nameOfRoom}
              isRemoved={isRemoved}
              handleDeletePostSuccess={handleDeletePostSuccess}
            />
          );
        })}
    </div>
  );
};
export default ListReplyPost;
