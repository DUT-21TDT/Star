import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { getAllPostOnNewsFeed, getPostOnProfileWall } from "../service/postAPI";
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
export { useFetchAllPostsOnWall, useFetchAllPostsOnNewsFeed };
