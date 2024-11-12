import React, {useEffect, useState} from "react";
import { Button, Input, Divider, Spin, Popover } from "antd";
import { SearchOutlined, LoadingOutlined } from "@ant-design/icons";
import default_image from "../../../assets/images/default_image.jpg";
import {
  useFetchAllUsers,
  useFollowUser,
  useUnfollowUser,
} from "../../../hooks/user";
import { useNavigate } from "react-router-dom";
import ContainerInformationUser from "../profile/posts/container-information-user";
import {getAllUsers} from "../../../service/userAPI.ts";

interface PeopleType {
  userId: string;
  avatarUrl: string | null;
  firstName: string | null;
  lastName: string | null;
  numberOfFollowers: number;
  username: string;
  followStatus: string;
}

const MainPeopleContent: React.FC = () => {
  const [searchValue, setSearchValue] = useState<string>("");
  const { data, isLoading, isError } = useFetchAllUsers("");
  const { mutate: followUser } = useFollowUser();
  const { mutate: unfollowUser } = useUnfollowUser();
  const navigate = useNavigate();

  const [followStatusMap, setFollowStatusMap] = useState<
    Record<string, string>
  >({});

  const [resultList, setResultList] = useState<PeopleType[]>(data);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
        // Call the API to get filtered members based on search term
        getAllUsers(searchValue)
          .then((res) => {
            setResultList(res.content);
            // setIsLoading(false);
          })
          .catch((error) => {
            console.error("Error fetching members:", error);
            setResultList([]);
          });
    }, 300); // Debounce duration: 300ms

    return () => clearTimeout(timeoutId); // Cleanup timeout on unmount or searchTerm change
  }, [searchValue]);


  const handleFollowUser = (userId: string) => {
    followUser(userId, {
      onSuccess: (response) => {
        if (response?.followStatus === "FOLLOWING") {
          setFollowStatusMap((prev) => ({
            ...prev,
            [userId]: "FOLLOWING",
          }));
        } else if (response?.followStatus === "REQUESTED") {
          setFollowStatusMap((prev) => ({
            ...prev,
            [userId]: "REQUESTED",
          }));
        }
      },
      onError: (error: Error) => {
        console.error("Error following user:", error);
      },
    });
  };

  const handleUnfollowUser = (userId: string) => {
    unfollowUser(userId, {
      onSuccess: () => {
        setFollowStatusMap((prev) => ({
          ...prev,
          [userId]: "NOT_FOLLOWING",
        }));
      },
      onError: (error: Error) => {
        console.error("Error unfollowing user:", error);
      },
    });
  };

  return (
    <>
      {isLoading ? (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      ) : isError ? (
        <div>Something went wrong</div>
      ) : (
        <>
          <div>
            <Input
              placeholder="Search"
              prefix={<SearchOutlined style={{ color: "#ccc" }} />}
              className="h-[40px] border rounded-2xl bg-[#fafafa] text-[16px] pl-5"
              value={searchValue}
              autoFocus
              onChange={(e) => setSearchValue(e.target.value)}
            />
          </div>
          <div className="flex flex-col mt-6 w-full">
            {resultList.map((item: PeopleType) => {
              const currentFollowStatus =
                followStatusMap[item.userId] || item.followStatus;
              return (
                <div key={item.userId} style={{ cursor: "pointer" }}>
                  <div className="flex items-center justify-between w-full">
                    <div className="flex items-start">
                      <img
                        src={item.avatarUrl || default_image}
                        alt="avatar"
                        className="w-[40px] h-[40px] rounded-full mr-3 mt-1"
                      />
                      <div>
                        <Popover
                          content={
                            <ContainerInformationUser
                              idOfCreator={item.userId}
                            />
                          }
                          placement="bottomLeft"
                          trigger="hover"
                          overlayClassName="custom-popover"
                          arrow={false}
                        >
                          <p
                            className="text-[16px] font-semibold"
                            onClick={() => navigate(`/profile/${item.userId}`)}
                          >
                            {item.username}
                          </p>
                        </Popover>
                        <p
                          className="text-gray-400 text-[14px] mt-[2px]"
                          style={{lineHeight: "18px"}}
                        >
                          {item.firstName && item.lastName ?
                            `${item.firstName} ${item.lastName}` :
                            item.firstName || item.lastName ?
                              item.firstName || item.lastName :
                              item.username}
                        </p>
                        <div className="mt-2 text-[15px]">
                          {item.numberOfFollowers} followers
                        </div>
                      </div>
                    </div>

                    <div>
                      <Button
                        style={{
                          color:
                            currentFollowStatus === "FOLLOWING"
                              ? "#bdbdbd"
                              : currentFollowStatus === "REQUESTED"
                              ? "#9e9e9e"
                              : "black",
                          fontWeight: 500,
                          width: "120px",
                          borderRadius: "16px",
                          padding: "10px 0px",
                        }}
                        onClick={() => {
                          if (
                            currentFollowStatus === "FOLLOWING" ||
                            currentFollowStatus === "REQUESTED"
                          ) {
                            handleUnfollowUser(item.userId);
                          } else {
                            handleFollowUser(item.userId);
                          }
                        }}
                      >
                        {currentFollowStatus === "FOLLOWING"
                          ? "Following"
                          : currentFollowStatus === "REQUESTED"
                          ? "Requested"
                          : "Follow"}
                      </Button>
                    </div>
                  </div>

                  <Divider style={{ margin: "8px 0px" }} />
                </div>
              );
            })}
          </div>
        </>
      )}
    </>
  );
};

export default MainPeopleContent;
