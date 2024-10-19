import React, { useState } from "react";

import { Button, Input, message, Result } from "antd";
import {
  endSession,
  resendVerifyEmail,
  revokeToken,
} from "../../service/userAPI";
import { useAppDispatch } from "../../redux/store/hook";
import { removeInformationUser } from "../../redux/slice/user-slice";
import Cookies from "js-cookie";

const VerifyEmail: React.FC = () => {
  const [inputEmail, setInputEmail] = useState<string>("");
  const [isEmailSent, setIsEmailSent] = useState<boolean>(false);
  const emailRegex = /^[\w.-]+@([\w-]+\.)+[\w-]{2,4}$/;

  const dispatch = useAppDispatch();
  const handleLogout = async () => {
    await revokeToken();
    await endSession();
    Cookies.remove("JSESSIONID");
    Cookies.remove("id_token");
    Cookies.remove("refresh_token");
    Cookies.remove("access_token");
    dispatch(removeInformationUser());
    window.location.href = "/login";
  };

  const handleResendVerifyEmail = async () => {
    if (inputEmail === "" || emailRegex.test(inputEmail)) {
      const response = await resendVerifyEmail(inputEmail);
      if (response.status === 200) {
        setIsEmailSent(true);
        setInputEmail("");
      }
    } else if (!emailRegex.test(inputEmail)) {
      message.error("Invalid email");
      return;
    }
  };
  return (
    <>
      <Result
        status={isEmailSent ? "success" : "info"}
        title={
          isEmailSent
            ? "Verification email sent!"
            : "Email verification required"
        }
        subTitle={
          isEmailSent
            ? "Please check your email to verify your account."
            : "To continue, please verify your email address."
        }
        extra={[
          !isEmailSent && (
            <>
              <Input
                style={{
                  maxWidth: "350px",
                  marginBottom: "10px",
                  height: "40px",
                }}
                placeholder="Enter your email address"
                type="email"
                value={inputEmail}
                onChange={(e) => setInputEmail(e.target.value)}
              />
              <div>
                <Button type="primary" onClick={handleResendVerifyEmail}>
                  Send
                </Button>
                <Button
                  type="default"
                  onClick={handleLogout}
                  style={{
                    marginLeft: "10px",
                  }}
                >
                  Back to login
                </Button>
              </div>
            </>
          ),
        ]}
      />
    </>
  );
};

export default VerifyEmail;
