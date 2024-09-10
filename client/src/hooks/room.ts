import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { getAllRoom } from "../service/roomAPI";
import { format } from "date-fns";

interface DataType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: string;
  participantsCount: number;
}
const useFetchAllRoom = () => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllRoom(),
    queryFn: getAllRoom,
  });
  return {
    data:
      result?.data?.map((item: DataType) => {
        return {
          id: item.id,
          key: item.id,
          name: item.name,
          description: item.description,
          createdAt: format(new Date(item.createdAt), "HH:mm dd-MM-yyyy"),
          participantsCount: item.participantsCount,
        };
      }) || [],
    isLoading: result.isLoading,
    isError: result.isError,
  };
};
export { useFetchAllRoom };
