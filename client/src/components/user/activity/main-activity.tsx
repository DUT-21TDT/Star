import { useQueryClient } from "@tanstack/react-query";
import ActivityItem from "./activity-item";
import { useEffect, useRef, useState } from "react";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useGetNotification } from "../../../hooks/notification";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { debounce } from "lodash";
import "../../../assets/css/activitiy.css";

interface INotificationType {
  id: string;
  type: string;
  artifactId: string;
  artifactType: string;
  lastActor: {
    id: string;
    username: string;
    avatarUrl: string;
  };
  numberOfActors: number;
  changeAt: string;
  read: boolean;
}

const MainContentActivity = () => {
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allNotifications, setAllNotifications] = useState<INotificationType[]>(
    []
  );
  const {
    dataNotification,
    isLoading,
    hasNextNotification,
    afterTimeFinalNotification,
  } = useGetNotification({
    limit: 15,
    after: afterTime,
  });

  const divRef = useRef(null);

  useEffect(() => {
    if (dataNotification && dataNotification.length > 0) {
      setAllNotifications((prevPosts: INotificationType[]) => [
        ...prevPosts,
        ...dataNotification,
      ]);
    }
  }, [dataNotification]);

  const handleScroll = debounce(() => {
    if (divRef.current) {
      const { scrollTop, scrollHeight, clientHeight } = divRef.current;
      const isBottom = scrollTop + clientHeight >= scrollHeight - 2;
      if (isBottom && hasNextNotification) {
        setAfterTime(afterTimeFinalNotification);
      }
    }
  }, 300);

  const queryClient = useQueryClient();

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: [QUERY_KEY.fetchAllNotification()],
      });
    };
  }, []);

  if (isLoading && allNotifications.length === 0) {
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  } else if (allNotifications && allNotifications.length === 0) {
    return (
      <div
        className="flex items-center justify-center text-xl"
        style={{ fontWeight: 450, marginTop: "25vh", color: "#999999" }}
      >
        No activities yet
      </div>
    );
  }
  console.log("allNotifications", allNotifications);
  return (
    <>
      <div
        style={{
          border: "1px solid #bdbdbd",
          marginTop: "20px",
          borderRadius: "30px",
          padding: "20px 0px 20px 0px",
          backgroundColor: "white",
          overflowY: "auto",
          maxHeight: "calc(100vh - 60px)",
        }}
        ref={divRef}
        onScroll={handleScroll}
        className="border-custom-scrollbar"
      >
        {allNotifications &&
          allNotifications.length > 0 &&
          allNotifications.map((notification: INotificationType) => {
            return (
              <ActivityItem key={notification.id} notification={notification} />
            );
          })}
      </div>
    </>
  );
};
export default MainContentActivity;
