import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  createRoom,
  deleteRoom,
  editRoom,
  getAllRoom,
  getRoomDetails,
  getAllRoomForUser,
  joinRoom,
  leaveRoomForUser,
  addModeratorToRoom,
  removeModeratorFromRoom,
  getModeratorsOfRoom,
} from "../service/roomAPI";
import { format } from "date-fns";

interface DataType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
  isParticipant?: boolean;
  isModerator?: boolean;
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

const useFetchAllRoomSelectBox = () => {
  const result = useQuery({
    queryKey: QUERY_KEY.fetchAllRoom(),
    queryFn: getAllRoom,
  });

  let listData = Array.isArray(result.data)
    ? result.data.map((item: DataType) => ({
        value: item.id,
        label: item.name,
      }))
    : []; // Ensure it's an array even if result.data is undefined or not iterable

  listData = [{ value: 0, label: "All rooms" }, ...listData];

  return {
    dataRoom: listData,
  };
};

const useGetRoomDetails = (id: string) => {
  const response = useQuery({
    queryKey: QUERY_KEY.getRoomDetails(id),
    queryFn: () => getRoomDetails(id),
  });
  return {
    data: response.data,
    isLoading: response.isLoading,
    isError: response.isError,
  };
};

const useCreateRoom = () => {
  return useMutation({
    mutationFn: createRoom,
  });
};

const useDeleteRoom = () => {
  return useMutation({
    mutationFn: deleteRoom,
  });
};

const useEditRoom = () => {
  return useMutation({
    mutationFn: editRoom,
  });
};

const useJoinRoom = () => {
  return useMutation({
    mutationFn: joinRoom,
  });
};

const useGetAllRoomForUser = (enable: boolean) => {
  const response = useQuery({
    queryKey: QUERY_KEY.fetchAllRoomForUser(),
    queryFn: getAllRoomForUser,
    enabled: enable,
  });
  const listRoomJoined =
    response?.data?.filter((item: DataType) => item.isParticipant) || [];

  const listRoomNotJoined =
    response?.data?.filter((item: DataType) => !item.isParticipant) || [];

  const listRoomYourModerator =
    response?.data?.filter((item: DataType) => item.isModerator) || [];

  return {
    listRoomJoined: listRoomJoined,
    listRoomNotJoined: listRoomNotJoined,
    listRoomYourModerator: listRoomYourModerator,
    isLoading: response.isLoading,
    isError: response.isError,
  };
};

const useLeaveRoomForUser = () => {
  return useMutation({
    mutationFn: leaveRoomForUser,
  });
};

const useGetModerators = (roomId: string) => {
  const response = useQuery({
    queryKey: QUERY_KEY.getModerators(roomId),
    queryFn: () => getModeratorsOfRoom(roomId),
  });
  return {
    data: response.data,
    isLoading: response.isLoading,
    isError: response.isError,
    refetch: response.refetch,
  };
};

const useAddModerator = () => {
  return useMutation({
    mutationFn: ({ roomId, username }: { roomId: string; username: string }) =>
      addModeratorToRoom(roomId, username),
  });
};

const useRemoveModerator = () => {
  return useMutation({
    mutationFn: ({ roomId, userId }: { roomId: string; userId: string }) =>
      removeModeratorFromRoom(roomId, userId),
  });
};

export {
  useFetchAllRoom,
  useGetRoomDetails,
  useGetModerators,
  useCreateRoom,
  useDeleteRoom,
  useEditRoom,
  useJoinRoom,
  useGetAllRoomForUser,
  useLeaveRoomForUser,
  useAddModerator,
  useRemoveModerator,
  useFetchAllRoomSelectBox,
};
