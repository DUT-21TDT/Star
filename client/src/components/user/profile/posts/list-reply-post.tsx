import { useEffect, useState } from "react";
import { useGetRepliesByPostId } from "../../../../hooks/post";
import Post from "./Post";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import RemoveDuplicatePost from "../../../../utils/removeDuplicatePost";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";

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
  reposted: boolean;
  idOfCreator: string;
  nameOfRoom: string;
  isRemoved?: boolean;
}

const ListReplyPost: React.FC<IProps> = (props) => {
  const { postId } = props;

  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<PostType[]>([]);
  const { dataPost, isLoading, hasNextPost, afterTimeFinalPost } =
    useGetRepliesByPostId(postId, {
      limit: 10,
      after: afterTime,
    });

  useEffect(() => {
    setAfterTime(null);
    setAllPosts([]);
  }, [postId]);

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
    }
  }, [dataPost]);

  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      setAfterTime(afterTimeFinalPost);
    }
  };

  const queryClient = useQueryClient();

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost, afterTimeFinalPost]);

  const handleDeletePostSuccess = (postIdRemoved: string) => {
    queryClient.invalidateQueries({
      queryKey: QUERY_KEY.fetchPostDetailById(postId),
    });
    setAllPosts((prevPosts) => {
      return prevPosts.map((post) => {
        if (post.id === postIdRemoved) {
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
        queryKey: [QUERY_KEY.fetchRepliesByPostId(postId)],
      });
    };
  }, []);

  if (isLoading && allPosts.length === 0) {
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  return (
    <div className="mt-4">
      {allPosts.map((post) => (
        <Post
          key={post.id}
          {...post}
          handleDeletePostSuccess={handleDeletePostSuccess}
        />
      ))}
    </div>
  );
};
export default ListReplyPost;
