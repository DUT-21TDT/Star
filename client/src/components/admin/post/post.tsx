import { Table, Popover, Modal, message } from "antd";
import type { TableProps } from "antd";
import HeaderTablePost from "./header-table-post";
import { useNavigate } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { Input, Select, Tag, Popconfirm, Space, Button } from "antd";
import {
  SearchOutlined,
  EyeOutlined,
  EyeInvisibleOutlined,
  UnorderedListOutlined,
} from "@ant-design/icons";
import "../../../assets/css/table-select-paginate.css";
import {
  getAllPostAdmin,
  hidePost,
  unhidePost,
} from "../../../service/postAPI.ts";
import { useFetchAllRoomSelectBox } from "../../../hooks/room";
import dayjs from "dayjs";

interface PostType {
  id: string;
  idOfCreator: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  nameOfRoom: string;
  createdAt: string;
  content: string;
  numberOfReports: number | null;
  postImageUrls: string[] | null;
  violenceScore: number;
  status: string;
  hideAt: string | null;
  hidden: boolean;
}

const Post: React.FC = () => {
  const navigate = useNavigate();

  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 5,
  });

  const [isModalVisible, setIsModalVisible] = useState(false);
  const [currentImage, setCurrentImage] = useState<string | null>(null);

  const handleImageClick = (url: string) => {
    setCurrentImage(url);
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const { dataRoom } = useFetchAllRoomSelectBox();

  const [totalPosts, setTotalPosts] = useState<number>(1);
  const [postType, setPostType] = useState<string>("");
  const [roomId, setRoomId] = useState<number>(0);
  const [resultList, setResultList] = useState<PostType[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [searchValue, setSearchValue] = useState<string>("");
  const [isHidden, setIsHidden] = useState<boolean | undefined>(undefined);
  const [postStatus, setPostStatus] = useState<string>("");

  // Unified API Call Function
  const fetchPosts = async () => {
    setIsLoading(true);
    try {
      const res = await getAllPostAdmin(
        pagination.current - 1,
        pagination.pageSize,
        postType,
        roomId,
        searchValue,
        postStatus,
        isHidden
      );
      setResultList(res.content);
      setTotalPosts(res.totalElements);
    } catch (error) {
      console.error("Error fetching posts:", error);
      setResultList([]);
    } finally {
      setIsLoading(false);
    }
  };

  // Fetch posts whenever filters, pagination, or search value changes
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      fetchPosts();
    }, 100); // Debounce API call by 100ms

    return () => clearTimeout(timeoutId);
  }, [pagination, postType, roomId, searchValue, postStatus, isHidden]);

  const handleTableChange = (newPagination: any) => {
    setPagination((prev) => ({
      ...prev,
      current: newPagination.current,
    }));
  };

  const handleToggleHide = async (record: PostType) => {
    const newStatus = record.hidden === true ? false : true;

    // Optimistic UI Update
    setResultList((prev) =>
      prev.map((user) =>
        user.id === record.id ? { ...user, hidden: newStatus } : user
      )
    );

    try {
      let response;
      if (newStatus === true) {
        response = await hidePost(record.id);
      } else {
        response = await unhidePost(record.id);
      }

      // Check HTTP status
      if (response) {
        throw new Error("Failed to update block state");
      }

      // Refetch user list to ensure consistency
      await fetchPosts();

      // Notify user of success
      if (newStatus === false) {
        message.success(
          `Post ${record.id} of user ${record.usernameOfCreator} is now unhide!`
        );
      } else {
        message.warning(
          `Post ${record.id} of user ${record.usernameOfCreator} is now hidden!`
        );
      }
    } catch (error) {
      console.error("Error updating user block state:", error);

      // Rollback optimistic update in case of error
      setResultList((prev) =>
        prev.map((user) =>
          user.id === record.id ? { ...user, hidden: record.hidden } : user
        )
      );

      message.error(
        `Failed to update display state for "${record.id}": ${
          (error as Error).message
        }`
      );
    }
  };

  const handleChangeType = (value: string) => {
    setPostType(value);
    setPagination((prev) => ({
      ...prev,
      current: 1,
    }));
  };

  const handleStatusType = (value: string) => {
    setPostStatus(value);
    setPagination((prev) => ({
      ...prev,
      current: 1,
    }));
  };

  const handleDisplayStatusType = (value: string) => {
    if (value === "HIDDEN") setIsHidden(true);
    else if (value === "DISPLAYED") setIsHidden(false);
    else setIsHidden(undefined);
    setPagination((prev) => ({
      ...prev,
      current: 1,
    }));
  };

  const handleChangeRoom = (value: number) => {
    setRoomId(value);
    setPagination((prev) => ({
      ...prev,
      current: 1,
    }));
  };

  const columns: TableProps<PostType>["columns"] = [
    {
      title: "Creator",
      dataIndex: "usernameOfCreator",
      key: "usernameOfCreator",
      render: (username, record) => (
        <div className="flex items-center gap-2">
          <div>
            {record.avatarUrlOfCreator ? (
              <img
                src={record.avatarUrlOfCreator}
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
          <a
            style={{ fontSize: "14px" }}
            onClick={() => {
              navigate(`/admin/users/${record.id}`);
              window.scrollTo(0, 0);
            }}
          >
            {username}
          </a>
        </div>
      ),
    },
    {
      title: "Room",
      dataIndex: "nameOfRoom",
      key: "nameOfRoom",
    },
    {
      title: "Content",
      dataIndex: "content",
      key: "content",
      render: (content, record) => {
        return (
          <Popover
            content={
              <div style={{ maxWidth: 300, whiteSpace: "pre-wrap" }}>
                {record.postImageUrls && record.postImageUrls.length > 0 && (
                  <div
                    style={{
                      display: "flex",
                      overflowX: "scroll",
                      marginBottom: 8,
                    }}
                  >
                    {record.postImageUrls.map((url, index) => (
                      <img
                        key={index}
                        src={url}
                        alt={`Post Image ${index + 1}`}
                        style={{
                          maxWidth: "100%",
                          maxHeight: 100,
                          marginRight: 4,
                          flexShrink: 0,
                          cursor: "pointer",
                        }}
                        onClick={() => handleImageClick(url)}
                      />
                    ))}
                  </div>
                )}
                {content}
              </div>
            }
            trigger="hover"
          >
            <div>
              <div className="max-w-[200px] overflow-hidden text-ellipsis whitespace-nowrap">
                {content}
              </div>
            </div>
          </Popover>
        );
      },
    },
    // {
    //   title: "Violence Score",
    //   dataIndex: "violenceScore",
    //   key: "violenceScore",
    //   render: (violenceScore) => violenceScore || "Haven't checked",
    // },
    {
      title: "Created At",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (date) =>
        date ? dayjs(date).format("HH:mm DD-MM-YYYY") : "Unknown",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status) => (
        <Tag
          color={
            status === "APPROVED"
              ? "green"
              : status === "PENDING"
              ? "blue"
              : "red"
          }
        >
          {status}
        </Tag>
      ),
    },
    {
      title: "Hidden At",
      dataIndex: "hideAt",
      key: "hideAt",
      render: (hideAt) =>
        hideAt ? dayjs(hideAt).format("HH:mm DD-MM-YYYY") : "None",
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <div>
          <Space size="small">
            <Popover
              content={record.hidden === true ? "Unhide post" : "Hide post"}
              trigger="hover"
            >
              <Popconfirm
                title={
                  record.hidden === true ? "Unhide this post" : "Hide this post"
                }
                description={
                  record.hidden === true
                    ? "Are you sure to unhide this post?"
                    : "Are you sure to hide this post?"
                }
                onConfirm={() => handleToggleHide(record)}
                cancelText="No"
                placement="topRight"
              >
                <Button
                  icon={
                    record.hidden !== true ? (
                      <EyeOutlined
                        className="text-green-500"
                        style={{ fontSize: "20px" }}
                      />
                    ) : (
                      <EyeInvisibleOutlined
                        className="text-red-500"
                        style={{ fontSize: "20px" }}
                      />
                    )
                  }
                  className="border-none"
                />
              </Popconfirm>
            </Popover>
            <Popover content="View details" trigger="hover">
              <a
                onClick={() => {
                  navigate(`/admin/posts/${record.id}`);
                  window.scrollTo(0, 0);
                }}
              >
                <UnorderedListOutlined style={{ fontSize: "20px" }} />
              </a>
            </Popover>
            <Popover content="Report list" trigger="hover">
              <Tag
              color="red"
              bordered
              style={{
                borderRadius: "10px",
                marginBottom: "4px",
                cursor: "pointer",
              }}
              onClick={() => {
                if (record.numberOfReports === null || record.numberOfReports === 0) {
                message.info("No reports for this post");
                } else {
                // Display modal with report details
                Modal.info({
                  title: "Report Details",
                  content: (
                  <div>
                    {/* Replace with actual report details */}
                    <p>Report details for post {record.id}</p>
                  </div>
                  ),
                  onOk() {},
                });
                }
              }}
              >
              {record.numberOfReports || 0} reports
              </Tag>
            </Popover>
          </Space>
        </div>
      ),
    },
  ];

  return (
    <div>
      <div className="flex justify-between items-center gap-4">
        <Input
          placeholder="Search by creator"
          prefix={<SearchOutlined style={{ color: "#ccc" }} />}
          className="h-[40px] border rounded-2xl bg-[#fafafa] text-[16px] pl-5"
          value={searchValue}
          autoFocus
          onChange={(e) => {
            setSearchValue(e.target.value);
            setPagination((prev) => ({
              ...prev,
              current: 1,
            }));
          }}
        />
        <Select
          defaultValue=""
          style={{ width: 140 }}
          onChange={handleChangeType}
          options={[
            { value: "", label: "All type" },
            { value: "POST", label: "Post" },
            { value: "REPLY", label: "Reply" },
          ]}
        />
        <Select
          defaultValue=""
          style={{ width: 140 }}
          onChange={handleStatusType}
          options={[
            { value: "", label: "All status" },
            { value: "APPROVED", label: "Approved" },
            { value: "PENDING", label: "Pending" },
            { value: "REJECTED", label: "Rejected" },
          ]}
        />
        <Select
          defaultValue=""
          style={{ width: 180 }}
          onChange={handleDisplayStatusType}
          options={[
            { value: "", label: "All display status" },
            { value: "HIDDEN", label: "Hidden" },
            { value: "DISPLAYED", label: "Displayed" },
          ]}
        />
        <Select
          placeholder="Select Room"
          style={{ width: 180 }}
          onChange={handleChangeRoom}
          defaultValue={0}
          options={dataRoom}
        />
      </div>
      <div className="custom-table">
        <Table
          columns={columns}
          dataSource={resultList}
          loading={isLoading} // Show spinner in table instead
          title={() => <HeaderTablePost countPost={totalPosts || 0} />}
          rowKey={(record) => record.id}
          pagination={{
            current: pagination.current,
            pageSize: pagination.pageSize,
            position: ["bottomCenter"],
            total: totalPosts,
          }}
          onChange={handleTableChange}
        />
      </div>
      <Modal
        visible={isModalVisible}
        title="Image Preview"
        footer={null}
        onCancel={handleCancel}
        centered
        zIndex={10000}
      >
        {currentImage && (
          <img
            src={currentImage}
            alt="Preview"
            style={{ width: "100%", paddingTop: "10px" }}
          />
        )}
      </Modal>
    </div>
  );
};

export default Post;
