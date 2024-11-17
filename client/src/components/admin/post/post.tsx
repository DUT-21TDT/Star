import React, { useState } from "react";
import { Button, Space, Spin, Table, Popconfirm, message } from "antd";
import type { TableProps } from "antd";
import HeaderTablePost from "./header-table-post";
import { useFetchAllPost } from "../../../hooks/post";
import {
  LoadingOutlined,
  EyeOutlined,
  EyeInvisibleOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

interface DataType {
  id: string;
  key: string;
  name: string;
  description: string;
  createdAt: string;
  participantsCount: number;
}

const Post: React.FC = () => {
  const navigate = useNavigate();
  const { data, isLoading, isError } = useFetchAllPost();

  // State to track hidden posts
  const [hiddenPosts, setHiddenPosts] = useState<Record<string, boolean>>({});

  const handleToggleVisibility = (record: DataType) => {
    const isCurrentlyHidden = hiddenPosts[record.id];
    setHiddenPosts((prev) => ({ ...prev, [record.id]: !isCurrentlyHidden }));

    if (isCurrentlyHidden) {
      message.success(`Post "${record.name}" is now visible!`);
    } else {
      message.success(`Post "${record.name}" is now hidden!`);
    }
  };

  const tableData: DataType[] =
    data?.content.map((item) => ({
      ...item,
      key: item.id,
    })) || [];

  const confirmDeletePost = (record: DataType) => {
    console.log(`Post ${record.id} remove.`);
    message.success("Post successfully remove!");
  };

  const columns: TableProps<DataType>["columns"] = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: (text, record) => (
        <a
          onClick={() => {
            navigate(`/admin/posts/${record.id}`);
            window.scrollTo(0, 0);
          }}
        >
          {text}
        </a>
      ),
      sorter: (a, b) => a.name.localeCompare(b.name),
    },
    {
      title: "Participants",
      dataIndex: "participantsCount",
      key: "participantsCount",
      sorter: (a, b) => a.participantsCount - b.participantsCount,
    },
    {
      title: "Created At",
      dataIndex: "createdAt",
      key: "createdAt",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="small">
          <Popconfirm
            title={hiddenPosts[record.id] ? "Unhide the post" : "Hide the post"}
            description={
              hiddenPosts[record.id]
                ? "Are you sure to unhide this post?"
                : "Are you sure to hide this post?"
            }
            onConfirm={() => handleToggleVisibility(record)}
            cancelText="No"
            placement="topRight"
          >
            <Button
              icon={
                hiddenPosts[record.id] ? (
                  <EyeInvisibleOutlined className="text-[red]" />
                ) : (
                  <EyeOutlined />
                )
              }
              className="border-none"
            />
          </Popconfirm>
          <Popconfirm
            title="Remove the post"
            description="Are you sure to remove this post?"
            onConfirm={() => confirmDeletePost(record)}
            cancelText="No"
            placement="topRight"
          >
            <Button
              icon={<DeleteOutlined className="text-[red]" />}
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
            title={() => <HeaderTablePost countPost={tableData.length || 0} />}
            expandable={{
              expandedRowRender: (record) => (
                <p style={{ margin: 0 }}>{record.description}</p>
              ),
              rowExpandable: (record) => !!record.description,
            }}
            pagination={{ position: ["bottomCenter"], pageSize: 4 }}
          />
        </div>
      )}
    </>
  );
};

export default Post;
