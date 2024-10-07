import React from "react";
import { Modal, Button, message } from "antd";
import { useJoinRoom, useLeaveRoomForUser } from "../../../hooks/room";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";

interface RoomType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
  isParticipant?: boolean;
}

interface IProps {
  dataRoom?: RoomType;
  isModalOpen: boolean;
  setIsModalOpen: (value: boolean) => void;
}

const ModalConfirmJoinRoom: React.FC<IProps> = ({
  dataRoom,
  isModalOpen,
  setIsModalOpen,
}) => {
  const { mutate: joinRoom } = useJoinRoom();
  const { mutate: leaveRoom } = useLeaveRoomForUser();
  const queryClient = useQueryClient();
  const handleJoinRoom = () => {
    joinRoom(dataRoom?.id.toString() || "", {
      onSuccess: () => {
        message.success("Join room successfully");
        queryClient.invalidateQueries({
          queryKey: QUERY_KEY.fetchAllRoomForUser(),
        });
        setIsModalOpen(false);
      },
      onError: () => {
        message.error("Join room failed. Please try again later");
      },
    });
  };
  const handleLeaveRoom = () => {
    leaveRoom(dataRoom?.id.toString() || "", {
      onSuccess: () => {
        message.success("Leave room successfully");
        queryClient.invalidateQueries({
          queryKey: QUERY_KEY.fetchAllRoomForUser(),
        });
        setIsModalOpen(false);
      },
      onError: () => {
        message.error("Leave room failed. Please try again later");
      },
    });
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      <Modal
        title={
          dataRoom?.isParticipant ? (
            <p className="text-center text-[18px]">Leave Room</p>
          ) : (
            <p className="text-center text-[18px]">Join Room</p>
          )
        }
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        width={400}
      >
        {dataRoom && dataRoom.isParticipant ? (
          <>
            <div className="flex flex-col justify-center items-center gap-2">
              <p className="text-center text-[15px] font-normal py-2">
                Do you want to leave {dataRoom?.name}?
              </p>
              <div>
                <Button onClick={handleCancel} style={{ marginLeft: 8 }}>
                  Cancel
                </Button>
                <Button
                  type="primary"
                  onClick={handleLeaveRoom}
                  style={{ marginLeft: 12, backgroundColor: "black" }}
                >
                  Leave Room
                </Button>
              </div>
            </div>
          </>
        ) : (
          <>
            <div className="flex flex-col justify-center items-center gap-2">
              <p className="text-center text-[15px] font-normal py-2">
                Do you want to join {dataRoom?.name}?
              </p>
              <div>
                <Button onClick={handleCancel} style={{ marginLeft: 8 }}>
                  Cancel
                </Button>
                <Button
                  type="primary"
                  onClick={handleJoinRoom}
                  style={{ marginLeft: 12, backgroundColor: "black" }}
                >
                  Join Room
                </Button>
              </div>
            </div>
          </>
        )}
      </Modal>
    </>
  );
};

export default ModalConfirmJoinRoom;
