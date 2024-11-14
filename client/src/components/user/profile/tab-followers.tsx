// import { Spin } from "antd";

// import { useGetAllFollowersByUserId } from "../../../hooks/follow";
// import { useEffect, useRef, useState } from "react";
// import { QUERY_KEY } from "../../../utils/queriesKey";
// import { useQueryClient } from "@tanstack/react-query";
// import { LoadingOutlined } from "@ant-design/icons";
// import FollowerItem from "./follower-item";
// interface IProps {
//   userId: string;
//   setCountFollower: React.Dispatch<React.SetStateAction<number>>;
// }
// interface IFollowerType {
//   avatarUrl: string;
//   firstName: string;
//   followAt: string;
//   followStatus: string;
//   lastName: string;
//   userId: string;
//   username: string;
// }
// const TabFollowers: React.FC<IProps> = ({ userId, setCountFollower }) => {
//   const queryClient = useQueryClient();
//   const [afterTime, setAfterTime] = useState<string | null>(null);
//   const [allFollowers, setAllFollowers] = useState<IFollowerType[]>([]);
//   const { dataFollowers, isLoading, hasNextFollower } =
//     useGetAllFollowersByUserId(userId, {
//       limit: 10,
//       after: afterTime,
//     });

//   const scrollContainerRef = useRef<HTMLDivElement>(null);

//   const handleScroll = () => {
//     const container = scrollContainerRef.current;
//     if (!container) return;

//     const isBottom =
//       container.scrollHeight - container.scrollTop <=
//       container.clientHeight + 1;

//     if (isBottom && hasNextFollower) {
//       queryClient.invalidateQueries({
//         queryKey: QUERY_KEY.fetchAllFollowersByUserId(userId),
//       });
//     }
//   };
//   useEffect(() => {
//     if (dataFollowers && dataFollowers.length > 0) {
//       setAllFollowers((prevFollowers: IFollowerType[]) => [
//         ...prevFollowers,
//         ...dataFollowers.map((follower: IFollowerType) => ({
//           ...follower,
//         })),
//       ]);
//       const lastFollower = dataFollowers[dataFollowers.length - 1];
//       setAfterTime(lastFollower.followAt);
//       setCountFollower(allFollowers.length + dataFollowers.length);
//     }
//   }, [dataFollowers]);

//   useEffect(() => {
//     const container = scrollContainerRef.current;
//     if (container) {
//       container.addEventListener("scroll", handleScroll);
//     }
//     return () => {
//       if (container) {
//         container.removeEventListener("scroll", handleScroll);
//       }
//     };
//   }, [hasNextFollower]);

//   useEffect(() => {
//     return () => {
//       queryClient.resetQueries({
//         queryKey: QUERY_KEY.fetchAllFollowersByUserId(userId),
//       });
//     };
//   }, []);

//   console.log("dataFollowers", dataFollowers);
//   console.log("hasNextFollower", hasNextFollower);
//   console.log("scrollContainerRef", scrollContainerRef);

//   if (isLoading)
//     return (
//       <div className="flex items-center justify-center mt-8">
//         <Spin indicator={<LoadingOutlined spin />} size="large" />
//       </div>
//     );
//   if (allFollowers && allFollowers.length === 0)
//     return (
//       <div className="w-full flex justify-center text-[16px] text-[gray] font-semibold">
//         The account doesn't have any followers yet.
//       </div>
//     );

//   return (
//     <div
//       ref={scrollContainerRef}
//       style={{ maxHeight: "700px", overflowY: "auto" }}
//     >
//       {allFollowers.map((item: IFollowerType) => (
//         <FollowerItem key={item.userId} {...item} />
//       ))}
//     </div>
//   );
// };
// export default TabFollowers;

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
  setCountFollower: React.Dispatch<React.SetStateAction<number>>;
  setIsOpenModalRequestFollow: (value: boolean) => void;
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

const TabFollowers: React.FC<IProps> = ({
  userId,
  setCountFollower,
  setIsOpenModalRequestFollow,
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
        })),
      ]);
      const lastFollower = dataFollowers[dataFollowers.length - 1];
      setAfterTime(lastFollower.followAt);
      // setCountFollower(allFollowers.length + dataFollowers.length);
      setCountFollower(0);
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
      style={{ maxHeight: "700px" }}
    >
      {allFollowers.map((item: IFollowerType) => (
        <FollowerItem
          key={item.userId}
          {...item}
          setIsOpenModalRequestFollow={setIsOpenModalRequestFollow}
        />
      ))}
    </PerfectScrollbar>
  );
};

export default TabFollowers;
