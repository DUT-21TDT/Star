import React from "react";
import { Button, Result } from "antd";
import { useConfirmAccount } from "../../hooks/user";
import { useNavigate } from "react-router-dom";

const Confirm: React.FC = () => {
  const params = new URLSearchParams(window.location.search);
  const token = params.get("token");
  const { data } = useConfirmAccount(token);
  const navigate = useNavigate();
  return (
    <>
      {data ? (
        <Result
          status="success"
          title={`Welcome, ${data?.username}!`}
          subTitle="Your account has been successfully confirmed. You can now log in."
          extra={[
            <Button type="primary" onClick={() => navigate("/login")}>
              Go to Login
            </Button>,
          ]}
        />
      ) : (
        <Result
          status="error"
          title="Failed to confirm account"
          subTitle="Please try again."
        />
      )}
    </>
  );
};

export default Confirm;
