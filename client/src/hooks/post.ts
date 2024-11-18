import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  getAllPostOnNewsFeed,
  getPostOnProfileWall,
  createAPost,
  getPostPresignedURL,
  getAllPostInRoom,
  getAllPendingPostInUserWall,
  getAllPendingPostForModerator,
  changeStatusPostByModerator,
  deletePost,
  replyPost,
  getPostDetailById,
  getRepliesByPostId,
} from "../service/postAPI";
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

const useGetAllPostsInRoom = (
  roomId: string,
  config: configTypeProfileWall
) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllPostsInRoom(roomId),
    queryFn: () => getAllPostInRoom(roomId, config),
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
    mutationFn: likePost,
  });
};

const useUnlikePost = () => {
  return useMutation({
    mutationFn: unlikePost,
  });
};

const useCreateAPost = () => {
  return useMutation({
    mutationFn: ({
      roomId,
      content,
      imageFileNames,
    }: {
      roomId: string;
      content: string;
      imageFileNames: string[];
    }) => createAPost(roomId, content, imageFileNames),
  });
};

const useGetAllPresignedUrl = () => {
  return useMutation({
    mutationFn: (fileNames: string[]) => getPostPresignedURL(fileNames),
  });
};

const useGetAllPendingPostOnWall = (config: configTypeProfileWall) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllPendingPostOnWall(),
    queryFn: () => getAllPendingPostInUserWall(config),
    staleTime: 0,
  });
  return {
    dataPost: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextPost: !result.data?.last,
    afterTime: result.data?.content[result.data?.content.length - 1]?.createdAt,
  };
};

const useGetAllPendingPostForModerator = (
  config: configTypeProfileWall,
  roomId: string,
  status: string
) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllPendingPostForModerator(roomId, status),
    queryFn: () => getAllPendingPostForModerator(config, roomId, status),
    staleTime: 0,
  });
  return {
    dataPost: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextPost: !result.data?.last,
    afterTimeFinalPost:
      result.data?.content[result.data?.content.length - 1]?.createdAt,
  };
};

const useChangeStatusPostByModerator = () => {
  return useMutation({
    mutationFn: ({ status, postId }: { status: string; postId: string }) =>
      changeStatusPostByModerator(status, postId),
  });
};

const useDeletePost = () => {
  return useMutation({
    mutationFn: (postId: string) => deletePost(postId),
  });
};

const useReplyPost = () => {
  return useMutation({
    mutationFn: ({
      parentPostId,
      content,
      imageFileNames,
    }: {
      parentPostId: string;
      content: string;
      imageFileNames: string[];
    }) => replyPost(parentPostId, content, imageFileNames),
  });
};

const useGetPostDetailById = (postId: string) => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchPostDetailById(postId),
    queryFn: () => getPostDetailById(postId),
    staleTime: 0,
  });
  return {
    data: result.data || {},
    isLoading: result.isLoading,
    isError: result.isError,
  };
};

const useGetRepliesByPostId = (
  postId: string,
  config: configTypeProfileWall
) => {
  const result = useQuery({
    // queryKey: QUERY_KEY.fetchRepliesByPostId(postId),
    queryKey: [QUERY_KEY.fetchRepliesByPostId(postId), config.after],
    queryFn: () => getRepliesByPostId(postId, config),
    staleTime: 0,
  });
  return {
    dataPost: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextPost: !result.data?.last,
    afterTimeFinalPost:
      result.data?.content[result.data?.content.length - 1]?.createdAt,
  };
};

export {
  useFetchAllPostsOnWall,
  useFetchAllPostsOnNewsFeed,
  useLikePost,
  useUnlikePost,
  useCreateAPost,
  useGetAllPresignedUrl,
  useGetAllPostsInRoom,
  useGetAllPendingPostOnWall,
  useGetAllPendingPostForModerator,
  useChangeStatusPostByModerator,
  useDeletePost,
  useReplyPost,
  useGetPostDetailById,
  useGetRepliesByPostId,
};
