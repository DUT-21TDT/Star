import {useEffect, useState} from "react";
import {Spin} from "antd";
import {LoadingOutlined} from "@ant-design/icons";
import {useQueryClient} from "@tanstack/react-query";
import {QUERY_KEY} from "../../../utils/queriesKey";
import {useGetAllPendingPostForModerator} from "../../../hooks/post";
import {useParams} from "react-router-dom";
import PostModerator from "./post-moderator";
import {debounce} from "../../../utils/debounce";

interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  idOfCreator?: string;
  nameOfRoom?: string | null;
  idOfModerator?: string | null;
  usernameOfModerator?: string | null;
  moderatedAt?: string | null;
  numberOfReports: number;
  violenceScore: number;
  status: string;
}

const RemoveDuplicatePost = (posts: PostType[]): PostType[] => {
  const uniquePostsMap = new Map<string, PostType>();
  for (let i = posts.length - 1; i >= 0; i--) {
    const post = posts[i];
    if (!uniquePostsMap.has(post.id)) {
      uniquePostsMap.set(post.id, post);
    }
  }
  return Array.from(uniquePostsMap.values()).sort((a, b) => {
    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
  });
};

const ApprovedPosts = () => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const { roomId = "" } = useParams<{ roomId: string }>();
  const { dataPost, isLoading, hasNextPost } = useGetAllPendingPostForModerator(
    {
      limit: 10,
      after: afterTime,
    },
    roomId,
    "APPROVED"
  );
  const [allPosts, setAllPosts] = useState<PostType[]>([]);

  const handleScroll = debounce(() => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPendingPostForModerator(roomId, "APPROVED"),
      });
    }
  }, 300);

  useEffect(() => {
    setAllPosts([]);
    setAfterTime(null);
  }, [roomId]);

  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts: PostType[]) =>
        RemoveDuplicatePost([
          ...prevPosts,
          ...dataPost.map((post: PostType) => ({
            ...post,
            postImageUrls: post.postImageUrls || [],
            isChangeStatus: "",
          })),
        ])
      );
      const lastPost = dataPost[dataPost.length - 1];
      setAfterTime(lastPost.createdAt);
    }
  }, [dataPost]);

  const handleChangeStatusPostState = (postId: string, status: string) => {
    const newPosts = allPosts.map((post) => {
      if (post.id === postId) {
        return {
          ...post,
          isChangeStatus: status,
        };
      }
      return post;
    });
    setAllPosts(newPosts);
  };

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost]);

  return (
    <>
      {isLoading ? (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      ) : (
        allPosts.map((post) => (
          <PostModerator
            key={`approved-${post.id}`}
            postData={post}
            handleChangeStatusPostState={handleChangeStatusPostState}
          />
        ))
      )}
    </>
  );
};

export default ApprovedPosts;
