import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  getActivitiesOnPostDetail,
  getNotifications,
} from "../service/notification";
type configType = {
  limit: number;
  after: string | null;
};
const useGetNotification = (config: configType) => {
  const result = useQuery({
    queryKey: [QUERY_KEY.fetchAllNotification(), config.after],
    queryFn: () => getNotifications(config),
  });
  return {
    dataNotification: result.data?.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextNotification: !result.data?.last,
    afterTimeFinalNotification:
      result.data?.content[result.data?.content.length - 1]?.changeAt,
    totalElements: result.data?.totalElements,
  };
};

const useGetAllActivitiesOnDetailPost = (
  postId: string,
  config: configType
) => {
  const result = useQuery({
    queryKey: [QUERY_KEY.fetchAllActivitiesOnPostDetail(postId), config.after],
    queryFn: () => getActivitiesOnPostDetail(postId, config),
  });
  return {
    dataNotification: result.data?.actors.content || [],
    isLoading: result.isLoading,
    isError: result.isError,
    hasNextNotification: !result.data?.actors.last,
    afterTimeFinalNotification:
      result.data?.actors?.content[result.data?.actors?.content.length - 1]
        ?.interactAt,
    viewsCount: result.data?.viewsCount,
    likesCount: result.data?.likesCount,
    repostsCount: result.data?.repostsCount,
  };
};

export { useGetNotification, useGetAllActivitiesOnDetailPost };
