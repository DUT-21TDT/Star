import { Button } from "antd";
import { EllipsisOutlined } from "@ant-design/icons";
const HeaderProfile: React.FC = () => {
  return (
    <>
      {/* Header Profile */}
      <div className="flex">
        <div style={{ flexGrow: 1 }}></div>
        <div style={{ flexGrow: 4, textAlign: "center", fontWeight: 500 }}>
          Profile
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
export default HeaderProfile;
