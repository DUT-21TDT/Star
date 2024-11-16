import { Spin } from "antd";
import { useGetAllFollowRequest } from "../../../hooks/follow";
import { useEffect, useState } from "react";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useQueryClient } from "@tanstack/react-query";
import { LoadingOutlined } from "@ant-design/icons";
import FollowerItem from "./follower-item";
import PerfectScrollbar from "react-perfect-scrollbar";
import "react-perfect-scrollbar/dist/css/styles.css";

interface IProps {
  userId: string;
  setIsOpenModalRequestFollow: (value: boolean) => void;
  fetchCount: () => void;
}
interface IFollowerType {
  avatarUrl: string;
  firstName: string;
  followAt: string;
  followStatus: string;
  followingId?: string;
  lastName: string;
  userId: string;
  username: string;
  tab?: string;
}

const TabRequested: React.FC<IProps> = ({
  userId,
  setIsOpenModalRequestFollow,
  fetchCount,
}) => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allRequested, setAllRequested] = useState<IFollowerType[]>([]);
  const { dataRequest, isLoading, hasNextRequest } = useGetAllFollowRequest({
    limit: 10,
    after: afterTime,
  });

  const handleScrollYReachEnd = () => {
    if (hasNextRequest) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllFollowRequest(),
      });
    }
  };

  useEffect(() => {
    if (dataRequest && dataRequest.length > 0) {
      setAllRequested((prevRequest: IFollowerType[]) => [
        ...prevRequest,
        ...dataRequest.map((follower: IFollowerType) => ({
          ...follower,
          tab: "REQUEST",
        })),
      ]);
      const lastRequest = dataRequest[dataRequest.length - 1];
      setAfterTime(lastRequest.followAt);
    }
  }, [dataRequest]);

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: QUERY_KEY.fetchAllFollowRequest(),
      });
    };
  }, []);

  if (isLoading)
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  if (allRequested && allRequested.length === 0)
    return (
      <div className="w-full flex justify-center text-[16px] text-[gray] font-semibold">
        The user has no request
      </div>
    );

  return (
    <PerfectScrollbar
      options={{ suppressScrollX: true }}
      onYReachEnd={handleScrollYReachEnd}
      style={{ maxHeight: "500px", minHeight: "400px" }}
    >
      {allRequested.map((item: IFollowerType) => (
        <FollowerItem
          key={item.userId}
          {...item}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
          currentProfileId={userId}
          setAllFollowers={setAllRequested}
          fetchCount={fetchCount}
        />
      ))}
    </PerfectScrollbar>
  );
};

export default TabRequested;
