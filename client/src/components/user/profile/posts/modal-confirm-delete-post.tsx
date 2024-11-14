import React from "react";
import { Modal, Button } from "antd";

interface IProps {
  openModal: boolean;
  setOpenModal: (value: boolean) => void;
  handleDeletePost: () => void;
}

const ModalConfirmDeletePost: React.FC<IProps> = ({
  openModal,
  setOpenModal,
  handleDeletePost,
}) => {
  const handleCancel = () => {
    setOpenModal(false);
  };

  return (
    <>
      <Modal
        title={<p className="text-center text-[18px]">Delete post?</p>}
        open={openModal}
        onCancel={handleCancel}
        footer={null}
        width={300}
      >
        <>
          <div className="flex flex-col justify-center items-center gap-2">
            <p className="text-center text-[15px] font-normal py-2">
              If you delete this post, you won't be able to restore it.
            </p>
            <div>
              <Button onClick={handleCancel} style={{ marginLeft: 8 }}>
                Cancel
              </Button>
              <Button
                type="primary"
                style={{ marginLeft: 12, backgroundColor: "black" }}
                onClick={handleDeletePost}
              >
                Delete
              </Button>
            </div>
          </div>
        </>
      </Modal>
    </>
  );
};

export default ModalConfirmDeletePost;
