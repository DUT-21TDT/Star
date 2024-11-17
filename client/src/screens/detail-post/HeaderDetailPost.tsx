import { Button } from "antd";
import { ArrowLeftOutlined, EllipsisOutlined } from "@ant-design/icons";

const HeaderDetailPost: React.FC = () => {
  const handleBack = () => {
    window.history.back();
  };

  return (
    <>
      <div className="flex">
        <div style={{ flexGrow: 1 }}>
          <Button
            icon={<ArrowLeftOutlined />}
            style={{
              borderRadius: "50%",
              width: "25px",
              height: "25px",
            }}
            onClick={handleBack}
          />
        </div>
        <div style={{ flexGrow: 4, textAlign: "center", fontWeight: 500 }}>
          Star
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

export default HeaderDetailPost;
