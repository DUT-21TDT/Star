import React, { useState } from "react";
import { Modal, Button, message } from "antd";
import { useFollowUser, useUnfollowUser } from "../../../hooks/user";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";

interface PeopleType {
  userId: string;
  avatarUrl: string | null;
  firstName: string | null;
  lastName: string | null;
  numberOfFollowers: number;
  username: string;
  followStatus: string;
}

interface IProps {
  dataPeople?: PeopleType;
  isModalOpen: boolean;
  setIsModalOpen: (value: boolean) => void;
}

const ModalConfirmFollow: React.FC<IProps> = ({
  dataPeople,
  isModalOpen,
  setIsModalOpen,
}) => {
  const { mutate: followUser } = useFollowUser();
  const { mutate: unFollowUser } = useUnfollowUser();
  const queryClient = useQueryClient();

  const [loading, setLoading] = useState(false);

  const handleFollowUser = () => {
    setLoading(true);
    followUser(dataPeople?.userId.toString() || "", {
      onSuccess: () => {
        message.success(`Followed ${dataPeople?.username}`);
        queryClient.invalidateQueries({
          queryKey: QUERY_KEY.fetchAllUsers(),
        });
        setIsModalOpen(false);
      },
      onError: () => {
        message.error(`Failed to follow ${dataPeople?.username}. Please try again later`);
      },
      onSettled: () => {
        setLoading(false);
      },
    });
  };

  const handleUnFollowUser = () => {
    setLoading(true);
    unFollowUser(dataPeople?.userId.toString() || "", {
      onSuccess: () => {
        message.success(`Unfollowed ${dataPeople?.username}`);
        queryClient.invalidateQueries({
          queryKey: QUERY_KEY.fetchAllUsers(),
        });
        setIsModalOpen(false);
      },
      onError: () => {
        message.error(`Failed to unfollow ${dataPeople?.username}. Please try again later`);
      },
      onSettled: () => {
        setLoading(false);
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
          dataPeople?.followStatus ? (
            <p className="text-center text-[18px]">Follow</p>
          ) : (
            <p className="text-center text-[18px]">Unfollow</p>
          )
        }
        open={isModalOpen}
        onCancel={handleCancel}
        footer={null}
        width={400}
      >
        {dataPeople && dataPeople.followStatus ? (
          <>
            <div className="flex flex-col justify-center items-center gap-2">
              <p className="text-center text-[15px] font-normal py-2">
                Do you want to leave {dataPeople?.username}?
              </p>
              <div>
                <Button
                  onClick={handleCancel}
                  style={{ marginLeft: 8 }}
                  disabled={loading}
                >
                  Cancel
                </Button>
                <Button
                  type="primary"
                  onClick={handleUnFollowUser}
                  loading={loading}
                  style={{ marginLeft: 12, backgroundColor: "black" }}
                >
                  Following
                </Button>
              </div>
            </div>
          </>
        ) : (
          <>
            <div className="flex flex-col justify-center items-center gap-2">
              <p className="text-center text-[15px] font-normal py-2">
                Do you want to join {dataPeople?.username}?
              </p>
              <div>
                <Button
                  onClick={handleCancel}
                  style={{ marginLeft: 8 }}
                  disabled={loading}
                >
                  Cancel
                </Button>
                <Button
                  type="primary"
                  onClick={handleFollowUser}
                  loading={loading}
                  style={{ marginLeft: 12, backgroundColor: "black" }}
                >
                  Follow
                </Button>
              </div>
            </div>
          </>
        )}
      </Modal>
    </>
  );
};

export default ModalConfirmFollow;
