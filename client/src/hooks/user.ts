import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  createNewUser,
  fetchAllUser,
  confirmAccount,
} from "../service/userAPI";

const useFetchAllUser = () => {
  return useQuery({
    queryKey: QUERY_KEY.getAllUser(),
    queryFn: fetchAllUser,
  });
};

const usePostNewUser = () => {
  return useMutation({
    mutationFn: createNewUser,
  });
};

const useConfirmAccount = (token: string | null) => {
  return useQuery({
    queryKey: QUERY_KEY.confirmNewUser(),
    queryFn: () => confirmAccount(token),
  });
};

export { useFetchAllUser, usePostNewUser, useConfirmAccount };
