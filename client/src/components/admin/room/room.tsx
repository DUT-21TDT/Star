import React from "react";
import { Button, Space, Spin, Table } from "antd";
import type { TableProps } from "antd";
import { DeleteOutlined, EditOutlined } from "@ant-design/icons";
import HeaderTableRoom from "./header-table-room";
import { useFetchAllRoom } from "../../../hooks/room";
import { LoadingOutlined } from "@ant-design/icons";
interface DataType {
  key: number;
  name: string;
  description: string;
  createdAt: string;
  participantsCount: number;
}

const columns: TableProps<DataType>["columns"] = [
  {
    title: "Name",
    dataIndex: "name",
    key: "name",
    render: (text) => <a>{text}</a>,
    sorter: (a, b) => a.name.localeCompare(b.name),
  },
  {
    title: "Paticipants",
    dataIndex: "participantsCount",
    key: "participantsCount",
    sorter: (a, b) => a.participantsCount - b.participantsCount,
  },
  {
    title: "Created",
    dataIndex: "createdAt",
    key: "createdAt",
  },

  {
    title: "Action",
    key: "action",
    render: () => (
      <Space size="small">
        <Button icon={<EditOutlined />} className="border-none" />
        <Button icon={<DeleteOutlined />} className="border-none" />
      </Space>
    ),
  },
];

const Room: React.FC = () => {
  const { data, isLoading, isError } = useFetchAllRoom();
  return (
    <>
      {isLoading ? (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      ) : isError ? (
        <div>Something went wrongs</div>
      ) : (
        <div>
          <Table
            columns={columns}
            dataSource={data}
            title={() => <HeaderTableRoom countRoom={data?.length || 0} />}
            // footer={() => "Footer"}
            expandable={{
              expandedRowRender: (record) => (
                <p style={{ margin: 0 }}>{record.description}</p>
              ),
              rowExpandable: (record) => record.name !== "Not Expandable",
            }}
            pagination={{ position: ["bottomCenter"], pageSize: 4 }}
          />
        </div>
      )}
    </>
  );
};
export default Room;
