import React from "react";
import { Button, Input, Divider } from "antd";
import { SearchOutlined } from "@ant-design/icons";

const MainRoomContent: React.FC = () => {
  return (
    <>
      <div>
        <Input
          placeholder="Search"
          prefix={<SearchOutlined style={{ color: "#ccc" }} />}
          className="h-[40px] border rounded-2xl bg-[#fafafa] text-[16px] pl-5"
        />
      </div>

      <div className="text-[16px] font-semibold text-[#a6a6a6] mt-2">
        Your rooms
      </div>
      <div className="flex flex-col mt-2 w-full">
        <div>
          <div className="flex items-end justify-between w-full">
            <div>
              <p className="text-[17px] font-semibold">new tech</p>
              <p className="text-[#ccc]" style={{ lineHeight: "18px" }}>
                new description
              </p>
            </div>

            <div>
              <Button
                style={{ color: "#bdbdbd", fontWeight: 500, width: "80px" }}
              >
                Joined
              </Button>
            </div>
          </div>
          <div className="mt-2 text-[15px]">10 participants</div>
          <Divider style={{ margin: "6px 0px" }} />
        </div>

        <div>
          <div className="flex items-end justify-between w-full">
            <div>
              <p className="text-[17px] font-semibold">new tech</p>
              <p className="text-[#ccc] " style={{ lineHeight: "18px" }}>
                new description
              </p>
            </div>

            <div>
              <Button
                style={{
                  fontWeight: 500,
                  width: "80px",
                  color: "#bdbdbd",
                }}
              >
                Joined
              </Button>
            </div>
          </div>
          <div className="mt-2 text-[15px]">10 participants</div>
          <Divider style={{ margin: "12px 0px" }} />
        </div>
      </div>
      {/* <div className="text-[16px] font-semibold text-[#a6a6a6] mt-2">
        Room suggestions
      </div>
      <div className="flex flex-col mt-2 w-full">
        <div>
          <div className="flex items-end justify-between w-full">
            <div>
              <p className="text-[17px] font-semibold">new tech</p>
              <p className="text-[#ccc]">new description</p>
            </div>

            <div>
              <Button
                style={{ color: "black", fontWeight: 500, width: "80px" }}
              >
                Join
              </Button>
            </div>
          </div>
          <div className="mt-2 text-[15px]">10 participants</div>
          <Divider />
        </div>

        <div>
          <div className="flex items-end justify-between w-full">
            <div>
              <p className="text-[17px] font-semibold">new tech</p>
              <p className="text-[#ccc]">new description</p>
            </div>

            <div>
              <Button
                style={{ color: "black", fontWeight: 500, width: "80px" }}
              >
                Join
              </Button>
            </div>
          </div>
          <div className="mt-2 text-[15px]">10 participants</div>
          <Divider style={{ padding: "12px 0px" }} />
        </div>
      </div> */}
    </>
  );
};

export default MainRoomContent;
