import { useMutation, useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "../utils/queriesKey";
import {
  createRoom,
  deleteRoom,
  editRoom,
  getAllRoom,
  getAllRoomForUser,
  joinRoom,
  leaveRoomForUser,
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
const useGetAllRoomForUser = () => {
  const response = useQuery({
    queryKey: QUERY_KEY.fetchAllRoomForUser(),
    queryFn: getAllRoomForUser,
  });
  const listRoomJoined =
    response?.data?.filter((item: DataType) => item.isParticipant) || [];

  const listRoomNotJoined =
    response?.data?.filter((item: DataType) => !item.isParticipant) || [];
  return {
    listRoomJoined: listRoomJoined,
    listRoomNotJoined: listRoomNotJoined,
    isLoading: response.isLoading,
    isError: response.isError,
  };
};

const useLeaveRoomForUser = () => {
  return useMutation({
    mutationFn: leaveRoomForUser,
  });
};
export {
  useFetchAllRoom,
  useCreateRoom,
  useDeleteRoom,
  useEditRoom,
  useJoinRoom,
  useGetAllRoomForUser,
  useLeaveRoomForUser,
};
