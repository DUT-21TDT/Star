import React, { useState } from "react";
import { Button, Input, Divider, Spin, Popover } from "antd";
import { SearchOutlined, LoadingOutlined } from "@ant-design/icons";
import {
  useFetchAllUsers,
  useFollowUser,
  useUnfollowUser,
} from "../../../hooks/user";
import { useNavigate } from "react-router-dom";
import ContainerInformationUser from "../profile/posts/container-information-user";

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
  const { data, isLoading, isError } = useFetchAllUsers(searchValue);
  const { mutate: followUser } = useFollowUser();
  const { mutate: unfollowUser } = useUnfollowUser();
  const navigate = useNavigate();

  const [followStatusMap, setFollowStatusMap] = useState<
    Record<string, string>
  >({});

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
          <div className="flex flex-col mt-2 w-full">
            {data.map((item: PeopleType) => {
              const currentFollowStatus =
                followStatusMap[item.userId] || item.followStatus;
              return (
                <div key={item.userId} style={{ cursor: "pointer" }}>
                  <div className="flex items-center justify-between w-full">
                    <div className="flex items-center">
                      <img
                        src={item.avatarUrl || "/avatar.jpg"}
                        alt="avatar"
                        className="w-[50px] h-[50px] rounded-full mr-3"
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
                            className="text-[17px] font-semibold"
                            onClick={() => navigate(`/profile/${item.userId}`)}
                          >
                            {item.username}
                          </p>
                        </Popover>
                        <p
                          className="text-[#ccc] text-[14px]"
                          style={{ lineHeight: "18px" }}
                        >
                          {item.firstName} {item.lastName}
                        </p>
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
                          width: "80px",
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
                  <div className="mt-2 ml-16 text-[15px]">
                    {item.numberOfFollowers} followers
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
