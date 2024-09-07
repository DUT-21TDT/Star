import React from "react";
import imageSignup from "../../assets/images/login.webp";
import { ConfigProvider, Divider, message, QRCode } from "antd";
import googleIcon from "../../assets/images/devicon_google.png";
import { Link } from "react-router-dom";
import { LoginTheme } from "../../utils/theme";
import "../../assets/css/login.css";
import axios from "axios";

const Login: React.FC = () => {

  const urlAuthLogin = `${import.meta.env.VITE_BACKEND_AUTH_URL
    }/oauth2/authorize?response_type=code&client_id=${import.meta.env.VITE_CLIENT_ID
    }&redirect_uri=${import.meta.env.VITE_REDIRECT_URI}&scope=openid`;

  const urlGoogleLogin = `${import.meta.env.VITE_BACKEND_AUTH_URL}/oauth2/authorization/google`;

  const handleGoogleLogin = (e: React.MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();
    if (sessionStorage.getItem("isGoogleLogin") === null) {
      sessionStorage.setItem("isGoogleLogin", "true");
      window.location.href = urlAuthLogin
    }
  };

  if (sessionStorage.getItem("isGoogleLogin")) {
    sessionStorage.removeItem("isGoogleLogin")
    window.location.href = urlGoogleLogin
  }

  return (
    <ConfigProvider theme={LoginTheme}>
      <div className="h-screen w-full relative bg-white">
        <div
          className="h-[45%] bg-cover bg-center"
          style={{ backgroundImage: `url(${imageSignup})` }}
        ></div>
        <div className="w-[450px] h-auto border border-black/15 absolute left-1/2 top-[60%] -translate-x-1/2 -translate-y-1/2 p-5 rounded-[20px] bg-white">
          <div className="text-center">
            <h1 className="text-[17px] font-bold mb-5">
              Log in with your Google account
            </h1>
          </div>
          <div>
            <form
              onSubmit={async (e) => {
                e.preventDefault(); // Ngăn chặn hành vi gửi form mặc định
                const username = (e.target as HTMLFormElement).username.value;
                const password = (e.target as HTMLFormElement).password.value;
                if (username === "" || password === "") {
                  message.error("Tên đăng nhập và mật khẩu là bắt buộc.");
                  return;
                }
                try {
                  const response = await axios.post(`${import.meta.env.VITE_BACKEND_AUTH_URL}/login`, {
                    username,
                    password
                  }, {
                    headers: {
                      'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    withCredentials: true
                  });

                  if (response.status === 200) {
                    window.location.href = urlAuthLogin;
                  }
                } catch (error) {
                  console.error("Lỗi đăng nhập:", error);
                  message.error("Đăng nhập thất bại, vui lòng kiểm tra lại thông tin đăng nhập");
                }
              }}
            >
              <div className="w-full flex flex-col gap-4 pt-2 pb-4">
                <div>
                  <input
                    type="text"
                    name="username"
                    className="w-full h-[50px] border border-black/15 rounded-[10px] p-2 text-[14px] pl-3 "
                    placeholder="Username"
                  />
                </div>
                <div>
                  <input
                    type="password"
                    name="password"
                    className="w-full h-[50px] border border-black/15 rounded-[10px] p-2 text-[14px] pl-3"
                    placeholder="Password"
                  />
                </div>
                <div>
                  <button
                    type="submit"
                    className="w-full h-[50px] bg-black text-[white] border border-black/15 rounded-[10px] p-2 text-[16px]"
                  >
                    Log in
                  </button>
                </div>
              </div>
            </form>
          </div>
          <div>
            <p className="text-center">
              Don't have an account?{" "}
              <Link to="/signup" className="text-[#1890ff]">
                {" "}
                Sign-up
              </Link>
            </p>
          </div>
          <Divider style={{ color: "#bdbdbd" }}>or</Divider>
          <a onClick={handleGoogleLogin}>
            <div className="border border-black/15 p-[20px_25px] rounded-[15px] flex items-center justify-between gap-2 cursor-pointer">
              <div className="w-[35px] h-[35px]">
                <img
                  src={googleIcon}
                  alt=""
                  className="w-full h-full object-cover object-center"
                />
              </div>
              <div className="text-[16px] font-bold">Continue with Google</div>
              <div>
                <svg
                  width="28"
                  height="28"
                  viewBox="0 0 24 24"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M10 17L15 12L10 7"
                    stroke="grey"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  />
                </svg>
              </div>
            </div>
          </a>
        </div>
        <div className="w-[250px] h-[250px] absolute right-0 bottom-0 p-5 flex flex-col items-center justify-center gap-2">
          <div className="text-[13px] text-gray-400 font-normal">
            Scan to get the app
          </div>

          <QRCode type="canvas" value="https://ant.design/" />
        </div>
      </div>
    </ConfigProvider>
  );
};

export default Login;
