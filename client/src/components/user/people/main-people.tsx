import React, {useEffect, useState} from "react";
import { Input, Spin } from "antd";
import { SearchOutlined, LoadingOutlined } from "@ant-design/icons";
import {
  useFetchAllUsers,
} from "../../../hooks/user";
import {getAllUsers} from "../../../service/userAPI.ts";
import PeopleItem from "./on-search-people-item.tsx";

interface IPeopleType {
  userId: string;
  username: string;
  avatarUrl: string | null;
  firstName: string | null;
  lastName: string | null;
  followStatus: string;
  numberOfFollowers: number;
}

const MainPeopleContent: React.FC = () => {
  const [searchValue, setSearchValue] = useState<string>("");
  const { data, isLoading, isError } = useFetchAllUsers("");

  const [resultList, setResultList] = useState<IPeopleType[]>(data);

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
            {resultList.map((item: IPeopleType) => (
              <PeopleItem
                key={item.userId}
                userId={item.userId}
                username={item.username}
                avatarUrl={item.avatarUrl}
                firstName={item.firstName}
                lastName={item.lastName}
                followStatus={item.followStatus}
                numberOfFollowers={item.numberOfFollowers}
              />
            ))}
          </div>
        </>
      )}
    </>
  );
};

export default MainPeopleContent;
