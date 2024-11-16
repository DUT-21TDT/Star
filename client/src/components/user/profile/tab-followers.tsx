import { Spin } from "antd";
import { useGetAllFollowersByUserId } from "../../../hooks/follow";
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
  lastName: string;
  userId: string;
  username: string;
  tab?: string;
}

const TabFollowers: React.FC<IProps> = ({
  userId,
  setIsOpenModalRequestFollow,
  fetchCount,
}) => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allFollowers, setAllFollowers] = useState<IFollowerType[]>([]);
  const { dataFollowers, isLoading, hasNextFollower } =
    useGetAllFollowersByUserId(userId, {
      limit: 10,
      after: afterTime,
    });

  const handleScrollYReachEnd = () => {
    if (hasNextFollower) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllFollowersByUserId(userId),
      });
    }
  };

  useEffect(() => {
    if (dataFollowers && dataFollowers.length > 0) {
      setAllFollowers((prevFollowers: IFollowerType[]) => [
        ...prevFollowers,
        ...dataFollowers.map((follower: IFollowerType) => ({
          ...follower,
          tab: "FOLLOWER",
        })),
      ]);
      const lastFollower = dataFollowers[dataFollowers.length - 1];
      setAfterTime(lastFollower.followAt);
    }
  }, [dataFollowers]);

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: QUERY_KEY.fetchAllFollowersByUserId(userId),
      });
    };
  }, []);

  if (isLoading)
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  if (allFollowers && allFollowers.length === 0)
    return (
      <div className="w-full flex justify-center text-[16px] text-[gray] font-semibold">
        The account doesn't have any followers yet.
      </div>
    );

  return (
    <PerfectScrollbar
      options={{ suppressScrollX: true }}
      onYReachEnd={handleScrollYReachEnd}
      style={{ maxHeight: "500px" }}
    >
      {allFollowers.map((item: IFollowerType) => (
        <FollowerItem
          key={item.userId}
          {...item}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
          currentProfileId={userId}
          setAllFollowers={setAllFollowers}
          fetchCount={fetchCount}
        />
      ))}
    </PerfectScrollbar>
  );
};

export default TabFollowers;
