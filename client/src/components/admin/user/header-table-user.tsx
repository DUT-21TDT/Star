import { Tag } from "antd";
import { useState } from "react";

interface IProps {
  countUser: number;
}
const HeaderTableUser = (props: IProps) => {
  const { countUser } = props;

  return (
    <div className="flex justify-between items-center gap-2">
      <div className="flex  justify-start items-center gap-1">
        <div className="text-[22px] font-semibold">Total Users</div>
        <Tag color="purple" bordered style={{ borderRadius: "10px" }}>
          {countUser} users
        </Tag>
      </div>
    </div>
  );
};
export default HeaderTableUser;
