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
  const [notReceivedEmail, setNotReceivedEmail] = useState<boolean>(false);
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
          <>
            We have sent a verification email to your email address. <br />
            Please check your email and click the link to verify your account.
          </>
        }
        extra={[
          !notReceivedEmail && (
            <>
              <a><p
                className="my-2 text-pretty font-semibold underline w-auto inline-block"
                onClick={() => setNotReceivedEmail(true)}>
                I didn't receive the email
              </p></a>
            </>
          ),
          !isEmailSent && notReceivedEmail && (
            <>
              <div style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column" }}>
                <div className="p-4 max-w-[400px]">
                  <label className="block text-sm font-thin text-gray-700 my-1">
                    Leave the email blank to use the email you registered with
                  </label>
                  <Input
                    style={{
                      maxWidth: "360px",
                      marginBottom: "10px",
                      height: "40px",
                    }}
                    placeholder="Enter your email address"
                    type="email"
                    value={inputEmail}
                    onChange={(e) => setInputEmail(e.target.value)}
                  />
                  <div>
                    <Button type="primary" onClick={handleResendVerifyEmail} className="font-medium">
                      Resend
                    </Button>
                    <Button
                      type="default"
                      onClick={handleLogout}
                      className="font-medium mt-2"
                      style={{
                        marginLeft: "10px",
                      }}
                    >
                      Back to login
                    </Button>
                  </div>
                </div>
              </div>
            </>
          ),
        ]}
      />
    </>
  );
};

export default VerifyEmail;
