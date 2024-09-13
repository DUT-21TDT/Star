import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { createRoom, getAllRoom } from "../service/roomAPI";
import { format } from "date-fns";

interface DataType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
}
const useFetchAllRoom = () => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllRoom(),
    queryFn: getAllRoom,
  });
  const listData =
    result?.data
      ?.map((item: DataType) => ({
        id: item.id,
        key: item.id,
        name: item.name,
        description: item.description,
        createdAt: new Date(item.createdAt),
        participantsCount: item.participantsCount,
      }))
      ?.sort(
        (a: DataType, b: DataType) =>
          b.createdAt.getTime() - a.createdAt.getTime()
      )
      ?.map((item: DataType) => ({
        ...item,
        createdAt: format(item.createdAt, "HH:mm dd-MM-yyyy"),
      })) || [];

  return {
    data: listData,
    isLoading: result.isLoading,
    isError: result.isError,
  };
};

const useCreateRoom = () => {
  return useMutation({
    mutationFn: createRoom,
  });
};
export { useFetchAllRoom, useCreateRoom };
