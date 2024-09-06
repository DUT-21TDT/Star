import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  createNewUser,
  fetchAllUser,
  confirmAccount,
  getTokenFromCode,
  getDataCurrentUser,
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

const useGetTokenFromCode = (code: string) => {
  const { data, isLoading, isError } = useQuery({
    queryKey: QUERY_KEY.getTokenFromCode(),
    queryFn: () => getTokenFromCode(code),
  });
  return {
    access_token: data?.access_token,
    refresh_token: data?.refresh_token,
    id_token: data?.id_token,
    isLoading,
    isError,
  };
};
const useGetCurrentUserFromToken = () => {
  const { data, isLoading, isError } = useQuery({
    queryKey: QUERY_KEY.getDataCurrentUser(),
    queryFn: getDataCurrentUser,
  });
  return {
    data: {
      role: data?.authorities[0]?.authority,
      name: data?.name,
    },
    isLoading,
    isError,
  };
};
export {
  useFetchAllUser,
  usePostNewUser,
  useConfirmAccount,
  useGetTokenFromCode,
  useGetCurrentUserFromToken,
};
