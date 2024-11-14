import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  getAllFollowersByUserId,
  getAllFollowingsByUserId,
} from "../service/followAPI";
type configType = {
  limit: number;
  after: string | null;
};
const useGetAllFollowersByUserId = (userId: string, config: configType) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllFollowersByUserId(userId),
    queryFn: () => getAllFollowersByUserId(userId, config),
  });
  return {
    dataFollowers: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextFollower: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.createdAt,
    totalElements: result.data?.totalElements,
  };
};

const useGetAllFollowingByUserId = (userId: string, config: configType) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllFollowingByUserId(userId),
    queryFn: () => getAllFollowingsByUserId(userId, config),
  });
  return {
    dataPost: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextPost: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.createdAt,
  };
};

export { useGetAllFollowersByUserId, useGetAllFollowingByUserId };
