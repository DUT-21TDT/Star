import { Button } from "antd";
import { EllipsisOutlined } from "@ant-design/icons";
const HeaderRoom: React.FC = () => {
  return (
    <>
      {/* Header room */}
      <div className="flex">
        <div style={{ flexGrow: 1 }}></div>
        <div style={{ flexGrow: 4, textAlign: "center", fontWeight: 500 }}>
          Room
        </div>
        <div style={{ flexGrow: 1 }}>
          <Button
            icon={<EllipsisOutlined />}
            style={{ borderRadius: "50%", width: "25px", height: "25px" }}
          />
        </div>
      </div>
    </>
  );
};
export default HeaderRoom;
