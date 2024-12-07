const IconFollowed = () => {
  return (
    <div
      style={{
        backgroundColor: "#6e3def",
        width: "20px",
        height: "20px",
        borderRadius: "50%",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        border: "2px solid white",
        position: "absolute",
        bottom: "-5px",
        right: "0",
      }}
    >
      <svg
        aria-label=""
        role="img"
        viewBox="0 0 18 18"
        style={{
          width: "18px",
          height: "18px",
          fill: "white",
        }}
      >
        <title></title>
        <path d="M5.81283 13C5.55932 13 5.35968 12.9466 5.2139 12.8398C5.0713 12.736 5 12.5921 5 12.4082C5 12.1235 5.0919 11.8224 5.2757 11.505C5.4595 11.1876 5.72569 10.891 6.07427 10.6151C6.42286 10.3363 6.84274 10.1109 7.33393 9.93882C7.82828 9.76381 8.38285 9.67631 8.99762 9.67631C9.61557 9.67631 10.1701 9.76381 10.6613 9.93882C11.1557 10.1109 11.5756 10.3363 11.921 10.6151C12.2696 10.891 12.5357 11.1876 12.7195 11.505C12.9065 11.8224 13 12.1235 13 12.4082C13 12.5921 12.9271 12.736 12.7813 12.8398C12.6387 12.9466 12.4407 13 12.1872 13H5.81283ZM9.00238 8.87987C8.6633 8.87987 8.34957 8.79384 8.0612 8.6218C7.77283 8.44679 7.53991 8.21246 7.36245 7.9188C7.18816 7.62217 7.10101 7.28995 7.10101 6.92214C7.10101 6.56025 7.18816 6.23396 7.36245 5.94327C7.53991 5.65258 7.77283 5.42269 8.0612 5.25362C8.34957 5.08454 8.6633 5 9.00238 5C9.34145 5 9.65518 5.08306 9.94355 5.24917C10.2319 5.41528 10.4633 5.64368 10.6376 5.93437C10.815 6.2221 10.9037 6.54839 10.9037 6.91324C10.9037 7.28402 10.815 7.61772 10.6376 7.91435C10.4633 8.21098 10.2319 8.44679 9.94355 8.6218C9.65518 8.79384 9.34145 8.87987 9.00238 8.87987Z"></path>
      </svg>
    </div>
  );
};

const IconLiked = () => {
  return (
    <div
      style={{
        backgroundColor: "#ff0034",
        width: "20px",
        height: "20px",
        borderRadius: "50%",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        border: "2px solid white",
        position: "absolute",
        bottom: "-5px",
        right: "0",
      }}
    >
      <svg
        aria-label=""
        role="img"
        viewBox="0 0 18 18"
        style={{
          width: "12px",
          height: "12px",
          fill: "white",
        }}
      >
        <title></title>
        <path d="M9 15.4L7.6 14.1C3.6 11.4 1 9.4 1 6.5 1 4.5 2.5 3 4.5 3 5.7 3 7 3.7 9 5.3 11 3.7 12.3 3 13.5 3 15.5 3 17 4.5 17 6.5 17 9.4 14.4 11.4 10.4 14.1L9 15.4Z"></path>
      </svg>
    </div>
  );
};

const IconReposted = () => {
  return (
    <div
      style={{
        backgroundColor: "#1d7bff",
        width: "20px",
        height: "20px",
        borderRadius: "50%",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        border: "2px solid white",
        position: "absolute",
        bottom: "-5px",
        right: "0",
      }}
    >
      <svg
        width="12px"
        height="12px"
        viewBox="-0.5 0 25 25"
        fill="white"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M10.52 4.13998C11.25 4.35998 11.72 5.96 11.9 7.91C17.21 7.91 22 13.4802 22 20.0802C19.8 14.0802 15 12.45 11.86 12.45C11.65 14.21 11.2 15.6202 10.52 15.8202C8.41996 16.4302 2 12.4401 2 9.98006C2 7.52006 8.40996 3.52998 10.52 4.13998Z"
          stroke="white"
          stroke-width="1.5"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </div>
  );
};

const IconInformation = () => {
  return (
    <div
      style={{
        backgroundColor: "#1d7bff",
        width: "20px",
        height: "20px",
        borderRadius: "50%",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        border: "2px solid white",
        position: "absolute",
        bottom: "-5px",
        right: "0",
      }}
    >
      <svg
        width="12px"
        height="12px"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M10 9H17M10 13H17M7 9H7.01M7 13H7.01M21 20L17.6757 18.3378C17.4237 18.2118 17.2977 18.1488 17.1656 18.1044C17.0484 18.065 16.9277 18.0365 16.8052 18.0193C16.6672 18 16.5263 18 16.2446 18H6.2C5.07989 18 4.51984 18 4.09202 17.782C3.71569 17.5903 3.40973 17.2843 3.21799 16.908C3 16.4802 3 15.9201 3 14.8V7.2C3 6.07989 3 5.51984 3.21799 5.09202C3.40973 4.71569 3.71569 4.40973 4.09202 4.21799C4.51984 4 5.0799 4 6.2 4H17.8C18.9201 4 19.4802 4 19.908 4.21799C20.2843 4.40973 20.5903 4.71569 20.782 5.09202C21 5.51984 21 6.0799 21 7.2V20Z"
          stroke="white"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </div>
  );
};
export { IconFollowed, IconLiked, IconReposted, IconInformation };
