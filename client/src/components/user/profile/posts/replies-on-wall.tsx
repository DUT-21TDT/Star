import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useGetRepliesByUserIdOnWall } from "../../../../hooks/post";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import Post from "./Post";
import { debounce } from "../../../../utils/debounce";

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

interface PostReplyType {
  parentPost: PostType | null;
  reply: PostType;
}
const RepliesOnWall = () => {
  const { id: userId } = useParams<{ id: string }>();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<PostReplyType[]>([]);
  const { dataPost, isLoading, hasNextPost, afterTimeFinalPost } =
    useGetRepliesByUserIdOnWall(userId || "", {
      limit: 10,
      after: afterTime,
    });

  useEffect(() => {
    setAfterTime(null);
    setAllPosts([]);
  }, [userId]);

  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts: PostReplyType[]) => [
        ...prevPosts,
        ...dataPost.map((post: PostReplyType) => {
          const { parentPost, reply } = post;
          return {
            parentPost: parentPost
              ? {
                  ...post.parentPost,
                  postImageUrls: parentPost.postImageUrls || [],
                  isRemoved: false,
                }
              : null,
            reply: {
              ...reply,
              postImageUrls: reply.postImageUrls || [],
              isRemoved: false,
            },
          };
        }),
      ]);
    }
  }, [dataPost]);

  const handleScroll = debounce(() => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;
    if (isBottom && hasNextPost) {
      setAfterTime(afterTimeFinalPost);
    }
  }, 300);

  const queryClient = useQueryClient();

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost, afterTimeFinalPost]);

  const handleDeletePostSuccess = (postIdRemoved: string) => {
    queryClient.invalidateQueries({
      queryKey: QUERY_KEY.fetchRepliesByUserId(userId || ""),
    });
    setAllPosts((prevPosts) => {
      return prevPosts.map((post) => {
        const { parentPost, reply } = post;
        if (parentPost) {
          if (parentPost.id === postIdRemoved) {
            return {
              ...post,
              parentPost: {
                ...parentPost,
                isRemoved: true,
              },
            };
          }
        }
        if (reply.id === postIdRemoved) {
          return {
            ...post,
            reply: {
              ...reply,
              isRemoved: true,
            },
            parentPost: parentPost
              ? {
                  ...parentPost,
                  isRemoved: true,
                }
              : null,
          };
        }
        return post;
      });
    });
  };

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: [QUERY_KEY.fetchRepliesByUserId(userId || "")],
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
    <div>
      {allPosts &&
        allPosts.map((post) => {
          const { parentPost, reply } = post;
          if (parentPost) {
            return (
              <div>
                <Post
                  key={parentPost.id}
                  {...parentPost}
                  isShowReplies={true}
                  handleDeletePostSuccess={handleDeletePostSuccess}
                />
                <Post
                  key={reply.id}
                  {...reply}
                  isShowReplies={false}
                  handleDeletePostSuccess={handleDeletePostSuccess}
                />
              </div>
            );
          } else {
            return (
              <Post
                key={reply.id}
                {...reply}
                isShowReplies={false}
                handleDeletePostSuccess={handleDeletePostSuccess}
              />
            );
          }
        })}
    </div>
  );
};
export default RepliesOnWall;
