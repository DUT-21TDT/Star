import { Spin } from "antd";
import { useGetAllFollowingByUserId } from "../../../hooks/follow";
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
}

const TabFollowing: React.FC<IProps> = ({
  userId,
  setIsOpenModalRequestFollow,
  fetchCount,
}) => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allFollowing, setAllFollowing] = useState<IFollowerType[]>([]);
  const { dataFollowing, isLoading, hasNextFollowing } =
    useGetAllFollowingByUserId(userId, {
      limit: 10,
      after: afterTime,
    });

  const handleScrollYReachEnd = () => {
    if (hasNextFollowing) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllFollowingByUserId(userId),
      });
    }
  };

  useEffect(() => {
    if (dataFollowing && dataFollowing.length > 0) {
      setAllFollowing((prevFollowing: IFollowerType[]) => [
        ...prevFollowing,
        ...dataFollowing.map((follower: IFollowerType) => ({
          ...follower,
        })),
      ]);
      const lastFollowing = dataFollowing[dataFollowing.length - 1];
      setAfterTime(lastFollowing.followAt);
    }
  }, [dataFollowing]);

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: QUERY_KEY.fetchAllFollowingByUserId(userId),
      });
    };
  }, []);

  if (isLoading)
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  if (allFollowing && allFollowing.length === 0)
    return (
      <div className="w-full flex justify-center text-[16px] text-[gray] font-semibold">
        The account is not following anyone
      </div>
    );

  return (
    <PerfectScrollbar
      options={{ suppressScrollX: true }}
      onYReachEnd={handleScrollYReachEnd}
      style={{ maxHeight: "500px" }}
    >
      {allFollowing.map((item: IFollowerType) => (
        <FollowerItem
          key={item.userId}
          {...item}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
          currentProfileId={userId}
          setAllFollowers={setAllFollowing}
          fetchCount={fetchCount}
        />
      ))}
    </PerfectScrollbar>
  );
};

export default TabFollowing;
