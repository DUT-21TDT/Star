import { Tag } from "antd";
interface IProps {
  countRoom: number;
}
const HeaderTableRoom = (props: IProps) => {
  const { countRoom } = props;
  return (
    <div className="flex justify-start items-center gap-2">
      <div className="text-[22px] font-semibold">Total Rooms</div>
      <Tag color="purple" bordered style={{ borderRadius: "10px" }}>
        {countRoom} rooms
      </Tag>
    </div>
  );
};
export default HeaderTableRoom;
