import { Avatar, Popover } from "antd";

import React, { useState } from "react";

import { useNavigate } from "react-router-dom";
import default_avatar from "../../../../assets/images/default_image.jpg";
import { timeAgo } from "../../../../utils/convertTime";
import ContainerInformationUser from "./container-information-user";

interface IReportedItem {
  postId: string;
  reason: string;
  reportAt: string;
  reportId: string;
  reporterAvatar: string;
  reporterId: string;
  reporterUsername: string;
}

interface IProps {
  data: IReportedItem;
}

const ReportedItem: React.FC<IProps> = ({ data }) => {
  const { reason, reportAt, reporterAvatar, reporterId, reporterUsername } =
    data;

  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );

  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={reporterId || ""} />
      );
    }
  };

  const navigate = useNavigate();
  const handleNavigateProfileUser = (e: React.MouseEvent, id: string) => {
    e.stopPropagation();
    if (id) {
      navigate(`/profile/${id}`);
      window.scrollTo(0, 0);
    }
  };

  return (
    <div
      className="w-full flex items-start gap-3 my-3"
      style={{
        // borderBottom: "1px solid rgb(240,240,240)",
        paddingBottom: "5px",
      }}
    >
      <div
        style={{
          position: "relative",
          marginLeft: "20px",
        }}
      >
        <Avatar size={45} src={reporterAvatar || default_avatar} />
      </div>
      <div
        className="flex items-center justify-between w-full pb-5"
        style={{
          paddingBottom: "10px",
          borderBottom: "1px solid rgb(240,240,240)",
        }}
      >
        <div>
          <div
            className="font-semibold"
            style={{
              cursor: "pointer",
            }}
            onClick={(e) => handleNavigateProfileUser(e, reporterId)}
          >
            <Popover
              content={popoverContent}
              placement="bottomLeft"
              trigger="hover"
              overlayClassName="custom-popover"
              arrow={false}
              open={isPopoverVisibleUsername}
              onOpenChange={handlePopoverUsernameVisibilityChange}
            >
              {reporterUsername}{" "}
              <span
                style={{
                  color: "#ababab",
                  fontWeight: "400",
                }}
              >
                {timeAgo(reportAt)}
              </span>
            </Popover>
          </div>
          <div
            style={{
              fontSize: "16px",
              color: "#ababab",
              fontWeight: 400,
              wordBreak: "break-word",
            }}
          >
            <div className="text-[14px] text-gray-500">{reason}</div>
          </div>
        </div>
      </div>
    </div>
  );
};
export default ReportedItem;
