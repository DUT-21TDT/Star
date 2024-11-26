import { Tag } from "antd";

interface IProps {
  countPost: number;
}
const HeaderTablePost = (props: IProps) => {
  const { countPost } = props;

  return (
    <div className="flex justify-between items-center gap-2">
      <div className="flex  justify-start items-center gap-1">
        <div className="text-[22px] font-semibold">Total Posts</div>
        <Tag color="purple" bordered style={{ borderRadius: "10px" }}>
          {countPost} posts
        </Tag>
      </div>
    </div>
  );
};
export default HeaderTablePost;
