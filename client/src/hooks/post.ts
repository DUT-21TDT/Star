import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { getAllPostOnNewsFeed, getPostOnProfileWall,   createAPost } from "../service/postAPI";
import { likePost, unlikePost } from "../service/postAPI";
type configTypeProfileWall = {
  limit: number;
  after: string | null;
};

const useFetchAllPostsOnWall = (
  userId: string,
  config: configTypeProfileWall
) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllPostsOnWall(userId),
    queryFn: () => getPostOnProfileWall(userId, config),
  });
  return {
    dataPost: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextPost: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.createdAt,
  };
};

const useFetchAllPostsOnNewsFeed = (config: configTypeProfileWall) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllPostsOnNewsFeed(),
    queryFn: () => getAllPostOnNewsFeed(config),
  });
  return {
    dataPost: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextPost: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.createdAt,
  };
};

const useLikePost = () => {
  return useMutation({
    mutationFn: likePost
  });
}

const useUnlikePost = () => {
  return useMutation({
    mutationFn: unlikePost
  });
}

const useCreateAPost = () => {
    return useMutation({
        mutationFn: ({ roomId, content }: { roomId: string; content: string }) =>
            createAPost(roomId, content),
    });
};

export {
  useFetchAllPostsOnWall,
  useFetchAllPostsOnNewsFeed,
  useLikePost,
  useUnlikePost, useCreateAPost
};
