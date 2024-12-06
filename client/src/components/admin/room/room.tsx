import React, { useState } from "react";
import { Button, Space, Spin, Table, Popconfirm, message } from "antd";
import type { TableProps } from "antd";
import { DeleteOutlined, EditOutlined } from "@ant-design/icons";
import HeaderTableRoom from "./header-table-room";
import { useDeleteRoom, useFetchAllRoom } from "../../../hooks/room";
import { LoadingOutlined } from "@ant-design/icons";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import ModalEditRoom from "./modal-edit-room";
import { useNavigate } from "react-router-dom";

interface DataType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: string;
  participantsCount: number;
}

const Room: React.FC = () => {
  const [openModalEdit, setOpenModalEdit] = useState<boolean>(false);
  const [dataEditRoom, setDataEditRoom] = useState<DataType | null>(null);
  const [textSearchRoom, setTextSearchRoom] = useState<string>("");
  const { mutate: deleteRoom } = useDeleteRoom();
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const confirm = (
    _: React.MouseEvent<HTMLElement, MouseEvent> | undefined,
    record: DataType
  ): void => {
    deleteRoom(record.id.toString(), {
      onSuccess: () => {
        message.success("Delete room successfully");
        queryClient.invalidateQueries({ queryKey: QUERY_KEY.fetchAllRoom() });
      },
      onError: () => {
        message.error("Delete room failed. Please try again later");
      },
    });
  };

  const columns: TableProps<DataType>["columns"] = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: (text, record) => (
        <a
          onClick={() => {
            navigate(`/admin/rooms/${record.id}`);
            window.scrollTo(0, 0);
          }}
        >
          {text}
        </a>
      ),
      sorter: (a, b) => a.name.localeCompare(b.name),
    },
    {
      title: "Paticipants",
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
        <>
          <Space size="small">
            <Button
              icon={<EditOutlined />}
              className="border-none"
              onClick={() => {
                setOpenModalEdit(true);
                setDataEditRoom(record);
              }}
            />
            <Popconfirm
              title="Delete the room"
              description="Are you sure to delete this room?"
              onConfirm={(e) => confirm(e, record)}
              cancelText="No"
              placement="topRight"
            >
              <Button
                icon={<DeleteOutlined className="text-[red]" />}
                className="border-none"
              />
            </Popconfirm>
          </Space>
        </>
      ),
    },
  ];
  const { data, isLoading, isError } = useFetchAllRoom();

  const filteredData = data?.filter((room: DataType) =>
    room.name.toLowerCase().includes(textSearchRoom.toLowerCase())
  );

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
            dataSource={filteredData}
            title={() => (
              <HeaderTableRoom
                countRoom={data?.length || 0}
                setTextSearchRoom={setTextSearchRoom}
              />
            )}
            // footer={() => "Footer"}
            expandable={{
              expandedRowRender: (record) => (
                <p
                  style={{
                    margin: 0,
                  }}
                >
                  {record.description}
                </p>
              ),
              rowExpandable: (record) => record.name !== "Not Expandable",
            }}
            pagination={{ position: ["bottomCenter"], pageSize: 4 }}
          />
          <ModalEditRoom
            openModalEdit={openModalEdit}
            setOpenModalEdit={setOpenModalEdit}
            data={dataEditRoom}
          />
        </div>
      )}
    </>
  );
};
export default Room;
