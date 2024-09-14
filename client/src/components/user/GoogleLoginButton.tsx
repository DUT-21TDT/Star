import React from "react";
import googleIcon from "../../assets/images/devicon_google.png";
import { useGoogleLogin } from "../../hooks/user";

const GoogleLoginButton: React.FC = () => {
  const { handleGoogleLogin } = useGoogleLogin();

  return (
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
  );
};

export default GoogleLoginButton;
