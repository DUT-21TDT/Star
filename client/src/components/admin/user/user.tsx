import React, { useState } from "react";
import { Button, Space, Spin, Table, Popconfirm, message } from "antd";
import type { TableProps } from "antd";
import HeaderTableUser from "./header-table-user";
import { useFetchAllUsers } from "../../../hooks/user";
import { LoadingOutlined, LockFilled, UnlockFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

interface PeopleType {
  userId: string;
  avatarUrl: string | null;
  firstName: string | null;
  lastName: string | null;
  numberOfFollowers: number;
  username: string;
  followStatus: string;
}

const User: React.FC = () => {
  const navigate = useNavigate();

  // Fetch all users using the custom hook
  const { data, isLoading, isError } = useFetchAllUsers(""); // assuming empty string for no search

  // State to track blocked users
  const [blockedUsers, setBlockedUsers] = useState<Record<string, boolean>>({});

  const handleToggleBlock = (record: PeopleType) => {
    const isCurrentlyBlocked = blockedUsers[record.userId];
    setBlockedUsers((prev) => ({
      ...prev,
      [record.userId]: !isCurrentlyBlocked,
    }));

    if (isCurrentlyBlocked) {
      message.success(`User "${record.username}" is now unblocked!`);
    } else {
      message.warning(`User "${record.username}" is now blocked!`);
    }
  };

  // Transform the API response into the expected Table data format
  const tableData: PeopleType[] = data || [];

  const columns: TableProps<PeopleType>["columns"] = [
    {
      title: "Name",
      dataIndex: "username",
      key: "username",
      render: (_, record) => (
        <a
          onClick={() => {
            navigate(`/admin/users/${record.userId}`);
            window.scrollTo(0, 0);
          }}
        >
          {`${record.firstName} ${record.lastName}`}
        </a>
      ),
      sorter: (a, b) =>
        `${a.firstName} ${a.lastName}`.localeCompare(
          `${b.firstName} ${b.lastName}`
        ),
    },
    {
      title: "Followers",
      dataIndex: "numberOfFollowers",
      key: "numberOfFollowers",
      sorter: (a, b) => a.numberOfFollowers - b.numberOfFollowers,
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="small">
          <Popconfirm
            title={
              blockedUsers[record.userId]
                ? "Unblock the user"
                : "Block the user"
            }
            description={
              blockedUsers[record.userId]
                ? "Are you sure to unblock this user?"
                : "Are you sure to block this user?"
            }
            onConfirm={() => handleToggleBlock(record)}
            cancelText="No"
            placement="topRight"
          >
            <Button
              icon={
                !blockedUsers[record.userId] ? (
                  <UnlockFilled className="text-green-500" />
                ) : (
                  <LockFilled className="text-red-500" />
                )
              }
              className="border-none"
            />
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <>
      {isLoading ? (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      ) : isError ? (
        <div className="text-center mt-8 text-red-500">
          Something went wrong. Please try again later.
        </div>
      ) : (
        <div>
          <Table
            columns={columns}
            dataSource={tableData}
            title={() => <HeaderTableUser countUser={tableData.length || 0} />}
            expandable={{
              expandedRowRender: (record) => (
                <p style={{ margin: 0 }}>{record.followStatus}</p> // Display followStatus in expandable row
              ),
              rowExpandable: (record) => !!record.followStatus, // You can conditionally check if followStatus exists
            }}
            pagination={{ position: ["bottomCenter"], pageSize: 4 }}
          />
        </div>
      )}
    </>
  );
};

export default User;
