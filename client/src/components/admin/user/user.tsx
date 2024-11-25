import React, { useEffect, useState } from "react";
import {
  Button,
  Space,
  Table,
  Popconfirm,
  message,
  Tag,
  Input,
  Select,
} from "antd";
import type { TableProps } from "antd";
import HeaderTableUser from "./header-table-user";
import { fetchAllUsers } from "../../../service/userAPI.ts";
import { LockFilled, SearchOutlined, UnlockFilled } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import "../../../assets/css/table-select-paginate.css";

interface PeopleType {
  userId: string;
  avatarUrl: string;
  firstName: string;
  lastName: string;
  numberOfFollowers: number;
  username: string;
  followStatus: string;
}

const User: React.FC = () => {
  const navigate = useNavigate();

  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 4,
  });

  const [totalUsers, setTotalUsers] = useState<number>(1);
  const [userStatus, setUserStatus] = useState<string>("ACTIVE");
  const [sortBy, setSortBy] = useState<string>("");
  const [direction, setDirection] = useState<string>("asc");
  const [searchValue, setSearchValue] = useState<string>("");
  const [resultList, setResultList] = useState<PeopleType[]>([]);
  const [blockedUsers, setBlockedUsers] = useState<Record<string, boolean>>({});
  const [isLoading, setIsLoading] = useState<boolean>(false);

  // Unified API Call Function
  const fetchUsers = async () => {
    setIsLoading(true);
    try {
      const res = await fetchAllUsers(
        pagination.pageSize,
        pagination.current - 1,
        sortBy,
        direction,
        userStatus,
        searchValue
      );
      setResultList(res.content);
      setTotalUsers(res.totalElements);
    } catch (error) {
      console.error("Error fetching users:", error);
      setResultList([]);
    } finally {
      setIsLoading(false);
    }
  };

  // Fetch users whenever filters, pagination, or search value changes
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      fetchUsers();
    }, 100); // Debounce API call by 100ms

    return () => clearTimeout(timeoutId);
  }, [searchValue, pagination, userStatus, sortBy, direction]);

  const handleTableChange = (newPagination: any, sorter: any) => {
    setPagination((prev) => ({
      ...prev,
      current: newPagination.current,
    }));
    setSortBy(sorter.field);
  };

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

  const handleChangeStatus = (value: string) => {
    setUserStatus(value);
    setPagination((prev) => ({
      ...prev,
      current: 1,
    }));
  };

  const handleChangeSort = (title: string) => {
    setSortBy(title.toLowerCase());
    if (direction === "asc") setDirection("desc");
    else setDirection("asc");
  };

  const columns: TableProps<PeopleType>["columns"] = [
    {
      title: "Avatar",
      dataIndex: "avatarUrl",
      key: "avatarUrl",
      render: (avatarUrl) => (
        <div>
          {avatarUrl ? (
            <img
              src={avatarUrl}
              alt="Avatar"
              style={{ width: 40, height: 40, borderRadius: "50%" }}
            />
          ) : (
            <div
              style={{
                width: 40,
                height: 40,
                borderRadius: "50%",
                backgroundColor: "#ccc",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontSize: "16px",
                color: "#fff",
              }}
            >
              NA
            </div>
          )}
        </div>
      ),
    },
    {
      title: "Fullname",
      dataIndex: "fullname",
      key: "fullname",
      render: (_, record) => (
        <a
          onClick={() => {
            navigate(`/admin/users/${record.userId}`);
            window.scrollTo(0, 0);
          }}
        >
          {record.firstName} {record.lastName}
        </a>
      ),
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
      onHeaderCell: (column) => ({
        onClick: () => {
          handleChangeSort(String(column.title));
        },
      }),
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      render: (email) => email || "Not Provided",
      onHeaderCell: (column) => ({
        onClick: () => {
          handleChangeSort(String(column.title));
        },
      }),
    },
    {
      title: "Gender",
      dataIndex: "gender",
      key: "gender",
      render: (gender) =>
        gender ? gender.charAt(0) + gender.slice(1).toLowerCase() : "Unknown",
    },
    {
      title: "Registered At",
      dataIndex: "registerAt",
      key: "registerAt",
      render: (date) =>
        date ? new Date(date).toLocaleDateString() : "Unknown",
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
          <Tag color="red" bordered style={{ borderRadius: "10px" }}>
            99 reports
          </Tag>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <div className="flex justify-between items-center gap-4">
        <Input
          placeholder="Search"
          prefix={<SearchOutlined style={{ color: "#ccc" }} />}
          className="h-[40px] border rounded-2xl bg-[#fafafa] text-[16px] pl-5"
          value={searchValue}
          autoFocus
          onChange={(e) => setSearchValue(e.target.value)}
        />
        <Select
          defaultValue="ACTIVE"
          style={{ width: 120 }}
          onChange={handleChangeStatus}
          options={[
            { value: "ACTIVE", label: "ACTIVE" },
            { value: "INACTIVE", label: "INACTIVE" },
          ]}
        />
      </div>
      <div className="custom-table">
        <Table
          columns={columns}
          dataSource={resultList}
          loading={isLoading} // Show spinner in table instead
          title={() => <HeaderTableUser countUser={totalUsers || 0} />}
          rowKey={(record) => record.userId}
          pagination={{
            current: pagination.current,
            pageSize: pagination.pageSize,
            position: ["bottomCenter"],
            total: totalUsers,
          }}
          onChange={handleTableChange}
        />
      </div>
    </div>
  );
};

export default User;
