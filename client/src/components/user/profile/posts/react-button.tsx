const ReactButton = () => {
  return (
    <div className="flex items-center gap-3">
      <div className="min-w-9 w-fit h-[40px] cursor-pointer rounded-[50%] flex items-center justify-center hover:bg-[#fafafa] p-2">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M18.4999 4.96676C15.7806 2.79127 12.9999 4.96675 11.9999 5.96675C10.9999 4.96675 8.2193 2.79127 5.49996 4.96676C2.78062 7.14224 2.18961 11.6564 5.99996 15.4667C9.81031 19.2771 11.9999 19.9667 11.9999 19.9667C11.9999 19.9667 14.1896 19.2771 17.9999 15.4667C21.8103 11.6564 21.2193 7.14224 18.4999 4.96676Z"
            stroke="black"
            strokeWidth="1"
          />
        </svg>
        1
      </div>

      <div className=" min-w-9 w-fit h-[40px] cursor-pointer rounded-[50%] flex items-center justify-center hover:bg-[#fafafa]">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M4 12C4 16.4183 7.58172 20 12 20C13.2552 20 14.4428 19.7109 15.5 19.1958L19.5 20L19 15.876C19.6372 14.7278 20 13.4063 20 12C20 7.58172 16.4183 4 12 4C7.58172 4 4 7.58172 4 12Z"
            stroke="black"
            strokeWidth="1"
          />
        </svg>
        10
      </div>
      <div className=" min-w-9 w-fit h-[40px] cursor-pointer rounded-[50%] flex items-center justify-center hover:bg-[#fafafa]">
        <svg
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M5 13.5V9C5 7.34315 6.34315 6 8 6H15.5M15.5 6L12.5 3M15.5 6L12.5 9M19 10.5V15C19 16.6569 17.6569 18 16 18L8.5 18M8.5 18L11.5 21M8.5 18L11.5 15"
            stroke="black"
            strokeWidth="1"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      </div>
    </div>
  );
};
export default ReactButton;
