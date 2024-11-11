import { Button, Modal } from "antd";
import { useState } from "react";

interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  idOfCreator?: string;
  nameOfRoom?: string | null;
  idOfModerator?: string | null;
  usernameOfModerator?: string | null;
  moderatedAt?: string | null;
  violenceScore: number;
  status: string;
}

interface IProps {
  isOpenModal: boolean;
  setIsOpenModal: (value: boolean) => void;
  dataPost: PostType | null;
  statusWantToChange: string;
}

const ModalConfirmApprovePost: React.FC<IProps> = ({
  isOpenModal,
  setIsOpenModal,
  dataPost,
  statusWantToChange,
}) => {
  const [loading, setLoading] = useState<boolean>(false);

  const handleCancel = () => {
    setIsOpenModal(false);
  };

  const handleStatusChange = (newStatus: string) => {
    console.log(newStatus);
    console.log(dataPost);
    setLoading(true);
  };

  const renderTitle = () => {
    switch (statusWantToChange) {
      case "PENDING":
        return "Pending post";
      case "APPROVED":
        return "Approve post";
      case "REJECTED":
        return "Reject post";
      default:
        return "";
    }
  };

  const renderContent = () => {
    switch (statusWantToChange) {
      case "PENDING":
        return (
          <div className="flex flex-col justify-center items-center gap-2">
            <p className="text-center text-[15px] font-normal py-2">
              Do you want to move this post to pending?
            </p>
            <div>
              <Button onClick={() => setIsOpenModal(true)} loading={loading}>
                Cancel
              </Button>
              <Button
                onClick={() => handleStatusChange("PENDING")}
                type="primary"
                loading={loading}
                style={{
                  backgroundColor: "black",
                  marginLeft: 8,
                  color: "white",
                }}
              >
                OK
              </Button>
            </div>
          </div>
        );
      case "APPROVED":
        return (
          <div className="flex flex-col justify-center items-center gap-2">
            <p className="text-center text-[15px] font-normal py-2">
              Do you want to approve this post?
            </p>
            <div>
              <Button
                onClick={() => setIsOpenModal(true)}
                loading={loading}
                style={{ marginRight: 8 }}
              >
                Cancel
              </Button>
              <Button
                onClick={() => handleStatusChange("APPROVED")}
                loading={loading}
                style={{
                  backgroundColor: "black",
                  marginLeft: 8,
                  color: "white",
                }}
              >
                OK
              </Button>
            </div>
          </div>
        );
      case "REJECTED":
        return (
          <div className="flex flex-col justify-center items-center gap-2">
            <p className="text-center text-[15px] font-normal py-2">
              Do you want to reject this post?
            </p>
            <div>
              <Button
                onClick={() => setIsOpenModal(true)}
                loading={loading}
                style={{ marginRight: 8 }}
              >
                Cancel
              </Button>
              <Button
                onClick={() => handleStatusChange("REJECTED")}
                loading={loading}
                type="primary"
                style={{
                  backgroundColor: "black",
                  marginLeft: 8,
                  color: "white",
                }}
              >
                OK
              </Button>
            </div>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <Modal
      title={<p className="text-center text-[18px]">{renderTitle()}</p>}
      open={isOpenModal}
      onCancel={handleCancel}
      footer={null}
      width={400}
    >
      {renderContent()}
    </Modal>
  );
};

export default ModalConfirmApprovePost;
