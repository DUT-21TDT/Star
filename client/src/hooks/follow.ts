import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  acceptFollowRequest,
  deleteFollowerByUserId,
  getAllFollowersByUserId,
  getAllFollowingsByUserId,
  getAllFollowRequest,
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
    afterTime: result.data?.content[result.data?.content.length - 1]?.followAt,
    totalElements: result.data?.totalElements,
  };
};

const useGetAllFollowingByUserId = (userId: string, config: configType) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllFollowingByUserId(userId),
    queryFn: () => getAllFollowingsByUserId(userId, config),
  });
  return {
    dataFollowing: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextFollowing: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.followAt,
    totalElements: result.data?.totalElements,
  };
};

const useDeleteFollowerByUserId = () => {
  return useMutation({
    mutationFn: (userId: string) => deleteFollowerByUserId(userId),
  });
};

const useGetAllFollowRequest = (config: configType) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllFollowRequest(),
    queryFn: () => getAllFollowRequest(config),
  });
  return {
    dataRequest: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextRequest: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.followAt,
    totalElements: result.data?.totalElements,
  };
};

const useAcceptFollowRequest = () => {
  return useMutation({
    mutationFn: (followingId: string) => acceptFollowRequest(followingId),
  });
};

export {
  useGetAllFollowersByUserId,
  useGetAllFollowingByUserId,
  useDeleteFollowerByUserId,
  useGetAllFollowRequest,
  useAcceptFollowRequest,
};
