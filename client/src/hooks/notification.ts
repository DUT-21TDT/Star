import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { getNotifications } from "../service/notification";
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
export { useGetNotification };
