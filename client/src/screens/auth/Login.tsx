import React, { useLayoutEffect, useState } from "react";
import imageSignup from "../../assets/images/login.webp";
import { ConfigProvider, Divider, message, QRCode } from "antd";
import googleIcon from "../../assets/images/devicon_google.png";
import { Link } from "react-router-dom";
import { LoginTheme } from "../../utils/theme";
import "../../assets/css/login.css";

const Login: React.FC = () => {
  const [isLoading, setIsLoading] = useState(true);
  const urlAuthLogin = `${
    import.meta.env.VITE_BACKEND_AUTH_URL
  }/oauth2/authorize?response_type=code&client_id=${
    import.meta.env.VITE_CLIENT_ID
  }&redirect_uri=${import.meta.env.VITE_REDIRECT_URI}&scope=openid`;
  useLayoutEffect(() => {
    const hasRedirected = sessionStorage.getItem("hasRedirected");
    if (!hasRedirected) {
      sessionStorage.setItem("hasRedirected", "true");
      window.location.href = urlAuthLogin;
    } else {
      setIsLoading(false);
    }
  }, [urlAuthLogin]);

  if (isLoading) {
    return null;
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
              action={`${import.meta.env.VITE_BACKEND_AUTH_URL}/login`}
              method="post"
              encType="application/x-www-form-urlencoded"
              onSubmit={(e) => {
                const username = (e.target as HTMLFormElement).username.value;
                const password = (e.target as HTMLFormElement).password.value;
                if (username === "" || password === "") {
                  e.preventDefault();
                  message.error("Username and password are required.");
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
          <a
            href={`${
              import.meta.env.VITE_BACKEND_AUTH_URL
            }/oauth2/authorization/google`}
          >
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
    // <div className="container">
    //   <div className="screen">
    //     <div className="screen__content">
    //       <form
    //         className="login"
    //         action="http://localhost:8081/login"
    //         method="post"
    //         encType="application/x-www-form-urlencoded"
    //       >
    //         <div className="login__field">
    //           <i className="login__icon fas fa-user"></i>
    //           <input
    //             type="text"
    //             name="username"
    //             className="login__input"
    //             placeholder="Username"
    //           />
    //         </div>
    //         <div className="login__field">
    //           <i className="login__icon fas fa-lock"></i>
    //           <input
    //             type="password"
    //             name="password"
    //             className="login__input"
    //             placeholder="Password"
    //           />
    //         </div>
    //         <button className="button login__submit">
    //           <span className="button__text">Log In Now</span>
    //           <i className="button__icon fas fa-chevron-right"></i>
    //         </button>
    //       </form>
    //       <div className="social-login">
    //         <h3>log in via</h3>
    //         <div className="social-icons">
    //           <a
    //             href="http://localhost:8081/oauth2/authorization/google"
    //             className="social-login__icon fab fa-google"
    //           >
    //             Google
    //           </a>
    //         </div>
    //       </div>
    //     </div>
    //     <div className="screen__background">
    //       <span className="screen__background__shape screen__background__shape4"></span>
    //       <span className="screen__background__shape screen__background__shape3"></span>
    //       <span className="screen__background__shape screen__background__shape2"></span>
    //       <span className="screen__background__shape screen__background__shape1"></span>
    //     </div>
    //   </div>
    // </div>
  );
};

export default Login;
