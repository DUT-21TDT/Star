import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { getAllRoom } from "../service/roomAPI";

const useFetchAllRoom = () => {
  return useQuery({
    queryKey: QUERY_KEY.fetchAllRoom(),
    queryFn: getAllRoom,
  });
};
export { useFetchAllRoom };
