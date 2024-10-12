import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  createNewUser,
  confirmAccount,
  getTokenFromCode,
  // getDataCurrentUser,
  getCurrentUserFromToken,
  getInformationUserFromId,
  editProfile,
  getPersonalInformation,
  getPresignedURL,
} from "../service/userAPI";

const usePostNewUser = () => {
  return useMutation({
    mutationFn: createNewUser,
  });
};

const useConfirmAccount = (token: string | null) => {
  return useQuery({
    queryKey: QUERY_KEY.confirmNewUser(token),
    queryFn: () => confirmAccount(token),
  });
};

const useGetTokenFromCode = (code: string) => {
  const { data, isLoading, isError } = useQuery({
    queryKey: QUERY_KEY.getTokenFromCode(code),
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

const useGetUserFromToken = (token: string | null) => {
  return useQuery({
    queryKey: QUERY_KEY.getCurrentUserFromToken(token),
    queryFn: () => getCurrentUserFromToken(token),
  });
};

const useGoogleLogin = () => {
  const urlAuthLogin = `${
    import.meta.env.VITE_BACKEND_AUTH_URL
  }/oauth2/authorize?response_type=code&client_id=${
    import.meta.env.VITE_CLIENT_ID
  }&redirect_uri=${import.meta.env.VITE_REDIRECT_URI}&scope=openid`;

  const handleGoogleLogin = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    if (sessionStorage.getItem("isGoogleLogin") === null) {
      sessionStorage.setItem("isGoogleLogin", "true");
      window.location.href = urlAuthLogin;
    }
  };

  return { handleGoogleLogin };
};

const useGetProfileUser = (id: string) => {
  return useQuery({
    queryKey: QUERY_KEY.getProfileUser(id),
    queryFn: () => getInformationUserFromId(id),
  });
};
const useEditProfile = () => {
  return useMutation({
    mutationFn: editProfile,
  });
};

const useGetPersonalInformation = () => {
  return useQuery({
    queryKey: QUERY_KEY.getPersonalInformation(),
    queryFn: () => getPersonalInformation(),
  });
};

const useGetPresignedUrl = () => {
  return useMutation({
    mutationFn: getPresignedURL,
  });
};
export {
  usePostNewUser,
  useConfirmAccount,
  useGetTokenFromCode,
  // useGetCurrentUserFromToken,
  useGetUserFromToken,
  useGoogleLogin,
  useGetProfileUser,
  useEditProfile,
  useGetPersonalInformation,
  useGetPresignedUrl,
};
