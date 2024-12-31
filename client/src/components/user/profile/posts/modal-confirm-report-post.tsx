import React, { useState } from "react";
import { Modal, Button, Input, Radio, message, Avatar, Popover } from "antd";
import { useReportPost } from "../../../../hooks/post";
import default_image from "../../../../assets/images/default_image.jpg";
import { useNavigate } from "react-router-dom";
import ContainerInformationUser from "./container-information-user";
import DOMPurify from "dompurify";
import { timeAgo } from "../../../../utils/convertTime";
type DataDetailPost = {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  reposted: boolean;
  nameOfRoom: string;
  idOfCreator: string;
};
interface IProps {
  dataDetailPost: DataDetailPost;
  openModal: boolean;
  setOpenModal: (value: boolean) => void;
}

const ModalConfirmReportPost: React.FC<IProps> = ({
  dataDetailPost,
  openModal,
  setOpenModal,
}) => {
  const [selectedReason, setSelectedReason] = useState<string>("");
  const [otherReason, setOtherReason] = useState<string>("");
  const { mutate: postReport } = useReportPost();
  const TextArea = Input.TextArea;
  const {
    id: postId,
    usernameOfCreator,
    avatarUrlOfCreator,
    createdAt,
    content,
    idOfCreator,
  } = dataDetailPost;

  const handleCancel = () => {
    setOpenModal(false);
    setSelectedReason("");
    setOtherReason("");
  };

  const handleReport = () => {
    if (!selectedReason) {
      message.error("Please select a reason for reporting.");
      return;
    }

    if (selectedReason === "Other" && !otherReason.trim()) {
      message.error("Please provide a reason for reporting.");
      return;
    }
    const finalReason =
      selectedReason === "Other" ? otherReason : selectedReason;

    postReport(
      {
        postId: postId,
        reason: finalReason,
      },
      {
        onSuccess: () => {
          message.success("Report submitted successfully.");
          setOpenModal(false);
        },
        onError: (error) => {
          console.error("Error:", error);
          message.error("An error occurred. Please try again later.");
        },
      }
    );
  };

  const reasons = [
    "Contains misleading or fraudulent information.",
    "Includes offensive language or inappropriate symbols.",
    "Involves intimidation or targeted harassment.",
    "Depicts violence or promotes harmful behavior.",
    "Other",
  ];

  const navigate = useNavigate();
  const handleNavigateProfileUser = (id: string) => () => {
    if (id) {
      navigate(`/profile/${id}`);
      window.scrollTo(0, 0);
    }
  };
  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );
  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={idOfCreator || ""} />
      );
    }
  };

  const sanitizedContent = DOMPurify.sanitize(
    content
      .replace(
        /(https?:\/\/\S+)/g,
        '<a href="$1" target="_blank" rel="noopener noreferrer" class="text-[#1B75D0] hover:text-[#165ca3]">$1</a>'
      )
      .replace(/\n/g, "<br />"),
    {
      ADD_ATTR: ["target"],
      FORBID_TAGS: ["style"],
    }
  );

  return (
    <Modal
      title={<p className="text-center text-[18px]">Report post?</p>}
      open={openModal}
      onCancel={handleCancel}
      footer={null}
      width={500}
    >
      <div className="flex flex-col justify-center items-center gap-4">
        <div
          className="w-full h-[90px] mt-5"
          style={{
            border: "1px solid #bdbdbd",
            borderRadius: "10px",
          }}
        >
          <div
            className="p-3 hover:cursor-pointer"
            style={{
              display: "flex",
              gap: "10px",
            }}
          >
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                minHeight: "80px",
              }}
            >
              <Avatar
                style={{
                  width: "45px",
                  height: "45px",
                  cursor: "pointer",
                  flexShrink: 0,
                }}
                src={avatarUrlOfCreator || default_image}
                onClick={handleNavigateProfileUser(idOfCreator || "")}
              />
            </div>

            <div style={{ width: "calc(100% - 65px)" }}>
              <div className="flex justify-between w-full">
                <Popover
                  content={popoverContent}
                  placement="bottomLeft"
                  trigger="hover"
                  mouseEnterDelay={0.35}
                  overlayClassName="custom-popover"
                  arrow={false}
                  open={isPopoverVisibleUsername}
                  onOpenChange={handlePopoverUsernameVisibilityChange}
                >
                  <div
                    style={{
                      fontWeight: "500",
                      fontSize: "16px",
                      cursor: "pointer",
                    }}
                    onClick={handleNavigateProfileUser(idOfCreator || "")}
                  >
                    {usernameOfCreator}{" "}
                    <span
                      style={{
                        color: "rgb(153,153,153)",
                        fontSize: "14px",
                      }}
                    >
                      {timeAgo(createdAt)}
                    </span>
                  </div>
                </Popover>
              </div>
              <p
                style={{
                  lineHeight: "22px",
                  fontSize: "15px",
                  textAlign: "left",
                  marginTop: "4px",
                  wordBreak: "break-word",
                  display: "-webkit-box",
                  WebkitLineClamp: 1,
                  WebkitBoxOrient: "vertical",
                  overflow: "hidden",
                  textOverflow: "ellipsis",
                }}
                dangerouslySetInnerHTML={{ __html: sanitizedContent }}
              ></p>
            </div>
          </div>
        </div>
        <p className="text-[16px] font-normal py-2 w-full px-2 font-semibold">
          Choose the reason why you report this post:
        </p>
        <div className="w-full">
          <Radio.Group
            onChange={(e) => setSelectedReason(e.target.value)}
            value={selectedReason}
            className="flex flex-col gap-2"
          >
            {reasons.map((reason, index) => (
              <Radio
                key={index}
                value={reason}
                style={{
                  whiteSpace: "normal",
                  wordWrap: "break-word",
                  fontSize: "16px",
                  fontWeight: 400,
                }}
              >
                {reason}
              </Radio>
            ))}
          </Radio.Group>
        </div>
        {selectedReason === "Other" && (
          <TextArea
            rows={5}
            maxLength={255}
            value={otherReason}
            onChange={(e) => setOtherReason(e.target.value)}
            placeholder="Please specify your reason (max 255 characters)"
            className="mt-2"
            showCount
          />
        )}
        <div className="flex justify-end w-full mt-2">
          <Button
            type="primary"
            onClick={handleReport}
            style={{ backgroundColor: "black" }}
          >
            Report
          </Button>
        </div>
      </div>
    </Modal>
  );
};

export default ModalConfirmReportPost;
