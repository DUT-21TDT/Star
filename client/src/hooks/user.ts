import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import { createNewUser, fetchAllUser } from "../service/userAPI";

const useFetchAllUser = () => {
  return useQuery({
    queryKey: QUERY_KEY.getAllUser(),
    queryFn: fetchAllUser,
  });
};

const usePostNewUser = () => {
  return useMutation({
    mutationKey: QUERY_KEY.postNewUser(),
    mutationFn: createNewUser,
  });
};

export { useFetchAllUser, usePostNewUser };
