import { Avatar, Button } from "antd";
import { IconInformation, IconLiked, IconReposted } from "./icon-activity";

const ActivityItem = () => {
  return (
    <div className="w-full flex items-start gap-4">
      <div
        style={{
          position: "relative",
        }}
      >
        <Avatar size={45} />
        {/* <IconFollowed /> */}
        {/* <IconLiked /> */}
        {/* <IconReposted /> */}
        <IconInformation />
      </div>
      <div
        className="flex items-center justify-between w-full"
        style={{
          borderBottom: "1px solid #bdbdbd",
          paddingBottom: "10px",
        }}
      >
        <div>
          <div className="font-semibold">
            Username{" "}
            <span
              style={{
                color: "#ababab",
                fontWeight: "400",
              }}
            >
              1d
            </span>
          </div>
          <div
            style={{
              fontSize: "16px",
              color: "#ababab",
              fontWeight: 400,
            }}
          >
            Follow suggestion
          </div>
        </div>
        <div>
          <Button
            style={{
              fontSize: "14px",
              color: "black",
              fontWeight: 500,
            }}
          >
            Follow
          </Button>
        </div>
      </div>
    </div>
  );
};
export default ActivityItem;
