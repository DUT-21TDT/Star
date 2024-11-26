import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  createNewUser,
  confirmAccount,
  getTokenFromCode,
  getInformationUserFromId,
  editProfile,
  getPersonalInformation,
  getPresignedURL,
  followUser,
  unfollowUser,
  getAllUsers,
} from "../service/userAPI";

interface DataType {
  userId: string;
  username: string;
  firstName: string;
  lastName: string;
  avatarUrl: string;
  numberOfFollowers: number;
  followStatus: string;
}

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

const useFollowUser = () => {
  return useMutation({
    mutationFn: followUser,
  });
}

const useUnfollowUser = () => {
  return useMutation({
    mutationFn: unfollowUser,
  });
};

const useFetchAllUsers = (keyword: string) => {
  const result = useQuery({
    queryKey: [QUERY_KEY.fetchAllUsers(), keyword],
    queryFn: () => getAllUsers(keyword),
  });

  const dataArray = Array.isArray(result.data)
    ? result.data[0]
    : Object.values(result.data || {})[0];

  const listData = Array.isArray(dataArray)
    ? dataArray.map((item: DataType) => ({
        userId: item.userId,
        username: item.username,
        firstName: item.firstName,
        lastName: item.lastName,
        avatarUrl: item.avatarUrl,
        numberOfFollowers: item.numberOfFollowers,
        followStatus: item.followStatus,
      }))
    : [];


  return {
    data: listData,
    isLoading: result.isLoading,
    isError: result.isError,
  };
};

const useGetUserDetails = (userId: string) => {
  console.log(userId)
  return {
    data: {
      name: "John Doe",
      description: "Software Engineer",
      participantsCount: 100,
      userCount: 50,
    },
    isLoading: false,
    isError: false,
    error: null,
  };
  }


export {
  usePostNewUser,
  useConfirmAccount,
  useGetTokenFromCode,
  useGoogleLogin,
  useGetProfileUser,
  useEditProfile,
  useGetPersonalInformation,
  useGetPresignedUrl,
  useFollowUser,
  useUnfollowUser,
  useFetchAllUsers,
  useGetUserDetails,
};
