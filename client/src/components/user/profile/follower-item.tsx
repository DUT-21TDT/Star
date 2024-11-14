import { Button, Divider, Popover } from "antd";
import default_image from "../../../assets/images/default_image.jpg";
import ContainerInformationUser from "./posts/container-information-user";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
interface IFollowerType {
  avatarUrl: string;
  firstName: string;
  followAt: string;
  followStatus: string;
  lastName: string;
  userId: string;
  username: string;
  setIsOpenModalRequestFollow: (value: boolean) => void;
}
const FollowerItem: React.FC<IFollowerType> = ({
  avatarUrl,
  firstName,
  //   followAt,
  //   followStatus,
  lastName,
  userId,
  username,
  setIsOpenModalRequestFollow,
}) => {
  const navigate = useNavigate();
  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );
  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={userId || ""} />
      );
    }
  };
  return (
    <div style={{ cursor: "pointer" }}>
      <div className="flex items-center justify-between w-full">
        <div className="flex items-start">
          <img
            src={avatarUrl || default_image}
            alt="avatar"
            className="w-[40px] h-[40px] rounded-full mr-3 mt-1"
          />
          <div>
            <Popover
              content={popoverContent}
              placement="bottomLeft"
              trigger="hover"
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
                onClick={() => {
                  setIsOpenModalRequestFollow(false);
                  navigate(`/profile/${userId}`);
                }}
              >
                {username}
              </div>
            </Popover>

            <p className="text-[16px] mt-[2px] text-gray-500">
              {firstName || lastName ? (
                <div className="text-[16px] mt-[2px] text-gray-500">
                  {`${firstName || ""} ${lastName || ""}`}
                </div>
              ) : (
                <div className="text-[16px] mt-[2px] text-gray-500">
                  {username}
                </div>
              )}
            </p>
          </div>
        </div>

        <div>
          <Button className="w-[120px] h-[40px] p-[15px] rounded-xl font-semibold text-[#ababab] text-[16px] mr-5">
            Remove
          </Button>
        </div>
      </div>

      <Divider style={{ margin: "16px 0px 16px 0px" }} />
    </div>
  );
};
export default FollowerItem;
