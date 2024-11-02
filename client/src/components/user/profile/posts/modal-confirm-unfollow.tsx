import React from "react";
import { Modal, Button, } from "antd";

interface IProps {
  username: string;
  proceedWithUnfollow: () => void;
  confirmUnfollowModal: boolean;
  setConfirmUnfollowModal: (value: boolean) => void;
}

const ModalConfirmUnfollow: React.FC<IProps> = ({
  username,
  proceedWithUnfollow,
  confirmUnfollowModal,
  setConfirmUnfollowModal,
}) => {
  const handleCancel = () => {
      setConfirmUnfollowModal(false);
  };

  const handleUnfollow = () => {
    proceedWithUnfollow();
    setConfirmUnfollowModal(false);
  }

  return (
    <>
      <Modal
        title={
          <p className="text-center text-[18px]">Unfollow {username}?</p>
        }
          open={confirmUnfollowModal}
          onCancel={handleCancel}
          footer={null}
          width={300}
        >
        <>
          <div className="flex flex-col justify-center items-center gap-2">
            <p className="text-center text-[15px] font-normal py-2">
              You will have to send a follow request to see their posts again.
            </p>
            <div>
              <Button onClick={handleCancel} style={{ marginLeft: 8 }}>
                Cancel
              </Button>
              <Button
                type="primary"
                onClick={handleUnfollow}
                style={{ marginLeft: 12, backgroundColor: "black" }}
              >
                Unfollow
              </Button>
            </div>
          </div>
        </>
      </Modal>
    </>
    );
};

export default ModalConfirmUnfollow;
