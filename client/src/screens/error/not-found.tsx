import React from "react";
import { Button, Result } from "antd";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../redux/store/hook";
const NotFound: React.FC = () => {
  const navigate = useNavigate();
  const role = useAppSelector((state) => state.user.role);
  return (
    <Result
      status="404"
      title="404"
      subTitle="Sorry, the page you visited does not exist."
      extra={
        <Button
          type="primary"
          onClick={() => {
            if (role === "ADMIN") {
              navigate("/admin");
            } else if (role === "USER") {
              navigate("/");
            }
          }}
        >
          Back Home
        </Button>
      }
      className="custom-result"
    />
  );
};
export default NotFound;
