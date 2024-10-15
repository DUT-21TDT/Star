import React, { useState } from "react";
import { Button, Input, Divider, Spin } from "antd";
import { SearchOutlined, LoadingOutlined } from "@ant-design/icons";
import { useGetAllRoomForUser } from "../../../hooks/room";
import ModalConfirmJoinRoom from "./modal-confirm-join-room";

interface RoomType {
  id: number;
  key: number;
  name: string;
  description: string;
  createdAt: Date;
  participantsCount: number;
  isParticipant?: boolean;
}

const MainRoomContent: React.FC = () => {
  const { listRoomJoined, listRoomNotJoined, isLoading, isError } =
    useGetAllRoomForUser();

  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [dataRoom, setDataRoom] = useState<RoomType | undefined>(undefined);
  const [searchValue, setSearchValue] = useState<string>("");

  const filteredRoomJoined = listRoomJoined.filter((room: RoomType) =>
    room.name.toLowerCase().includes(searchValue.toLowerCase())
  );
  const filteredRoomNotJoined = listRoomNotJoined.filter((room: RoomType) =>
    room.name.toLowerCase().includes(searchValue.toLowerCase())
  );

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
              onChange={(e) => setSearchValue(e.target.value)}
            />
          </div>

          {filteredRoomJoined.length > 0 && (
            <div className="text-[18px] font-bold text-[#a6a6a6] mt-2">
              Your rooms
            </div>
          )}

          <div className="flex flex-col mt-2 w-full">
            {filteredRoomJoined.map((item: RoomType) => (
              <div key={item.id}>
                <div className="flex items-center justify-between w-full">
                  <div>
                    <p className="text-[17px] font-semibold">{item.name}</p>
                    <p
                      className="text-[#ccc] text-[14px]"
                      style={{ lineHeight: "18px" }}
                    >
                      {item.description}
                    </p>
                  </div>

                  <div>
                    <Button
                      style={{
                        color: "#bdbdbd",
                        fontWeight: 500,
                        width: "80px",
                      }}
                      onClick={() => {
                        setIsModalOpen(true);
                        setDataRoom(item);
                      }}
                    >
                      Joined
                    </Button>
                  </div>
                </div>
                <div className="mt-2 text-[15px]">
                  {item.participantsCount} participants
                </div>
                <Divider style={{ margin: "8px 0px" }} />
              </div>
            ))}
          </div>

          {filteredRoomNotJoined.length > 0 && (
            <div className="text-[18px] font-bold text-[#a6a6a6] mt-2 mb-2">
              Room suggestions
            </div>
          )}

          <div className="flex flex-col w-full">
            {filteredRoomNotJoined.map((item: RoomType, index: number) => (
              <div className="mt-1" key={item.id}>
                <div className="flex items-center justify-between w-full ">
                  <div>
                    <p className="text-[17px] font-semibold">{item.name}</p>
                    <p
                      className="text-[#ccc] text-[14px]"
                      style={{ lineHeight: "18px" }}
                    >
                      {item.description}
                    </p>
                  </div>

                  <div>
                    <Button
                      style={{
                        color: "black",
                        fontWeight: 500,
                        width: "80px",
                      }}
                      onClick={() => {
                        setIsModalOpen(true);
                        setDataRoom(item);
                      }}
                    >
                      Join
                    </Button>
                  </div>
                </div>
                <div className="mt-2 text-[15px] font-normal">
                  {item.participantsCount} participants
                </div>
                {index !== filteredRoomNotJoined.length - 1 && (
                  <Divider style={{ margin: "8px 0px" }} />
                )}
              </div>
            ))}
          </div>

          <ModalConfirmJoinRoom
            isModalOpen={isModalOpen}
            setIsModalOpen={setIsModalOpen}
            dataRoom={dataRoom}
          />
        </>
      )}
    </>
  );
};

export default MainRoomContent;
