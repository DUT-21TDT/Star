import React from "react";
import "../css/sidebar.css";
interface IProps {
  width: string;
  height: string;
  style?: React.CSSProperties;
  isActive?: boolean;
}
const Logo: React.FC<IProps> = ({ width, height, style }) => {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 34 40"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      style={style}
    >
      <path
        d="M26.4569 18.5392C26.2867 18.4566 26.1138 18.3772 25.9386 18.3011C25.6336 12.6121 22.5624 9.35521 17.4055 9.32187C17.3821 9.32173 17.3589 9.32173 17.3355 9.32173C14.251 9.32173 11.6857 10.6544 10.1067 13.0793L12.9429 15.0485C14.1224 13.2371 15.9736 12.851 17.3369 12.851C17.3526 12.851 17.3684 12.851 17.384 12.8511C19.082 12.8621 20.3633 13.3618 21.1926 14.3363C21.7961 15.0457 22.1997 16.026 22.3996 17.2633C20.8941 17.0043 19.266 16.9247 17.5255 17.0257C12.6225 17.3115 9.47051 20.2058 9.68221 24.2275C9.78963 26.2675 10.7937 28.0225 12.5094 29.169C13.96 30.1381 15.8282 30.6121 17.7699 30.5048C20.3341 30.3625 22.3456 29.3723 23.749 27.5617C24.8148 26.1867 25.4889 24.4048 25.7865 22.1596C27.0085 22.906 27.9142 23.8883 28.4144 25.0692C29.2649 27.0765 29.3145 30.375 26.6553 33.0642C24.3255 35.42 21.525 36.4392 17.2926 36.4706C12.5977 36.4354 9.04706 34.9115 6.73852 31.941C4.57676 29.1596 3.45955 25.1421 3.41787 20C3.45955 14.8579 4.57676 10.8403 6.73852 8.0589C9.04706 5.08852 12.5977 3.56458 17.2925 3.52927C22.0215 3.56485 25.634 5.09612 28.0309 8.08083C29.2062 9.5445 30.0923 11.3852 30.6764 13.5313L34 12.6338C33.2919 9.99212 32.1778 7.71577 30.6616 5.82792C27.5888 2.00139 23.0947 0.0406558 17.3041 0H17.2809C11.5022 0.0405146 7.05838 2.00871 4.07305 5.84985C1.41651 9.268 0.0461885 14.0241 0.000144081 19.9859L0 20L0.000144081 20.0141C0.0461885 25.9758 1.41651 30.7321 4.07305 34.1502C7.05838 37.9912 11.5022 39.9596 17.2809 40H17.3041C22.4418 39.964 26.0632 38.6025 29.0465 35.5856C32.9496 31.6388 32.8321 26.6915 31.5457 23.6544C30.6227 21.4765 28.8631 19.7075 26.4569 18.5392ZM17.5863 26.9806C15.4374 27.1031 13.205 26.1269 13.0949 24.0358C13.0133 22.4854 14.185 20.7554 17.7182 20.5493C18.1228 20.5257 18.5199 20.5142 18.9099 20.5142C20.1933 20.5142 21.3939 20.6404 22.4854 20.8819C22.0783 26.0281 19.6902 26.8638 17.5863 26.9806Z"
        fill="black"
      />
    </svg>
  );
};

const HomeIcon: React.FC<IProps> = ({ width, height, isActive }) => {
  return (
    <>
      {!isActive ? (
        <svg
          width={width}
          height={height}
          viewBox="0 0 32 37"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M26.7632 23.3452V15.3539C26.7632 14.1542 26.2841 13.0029 25.4127 12.1783C24.3913 11.2116 22.9742 9.90754 21.8301 8.9942C19.666 7.26652 18.5859 5.85492 16 5.85492C13.4141 5.85492 12.334 7.26652 10.1699 8.9942C9.02584 9.90754 7.60871 11.2116 6.58729 12.1783C5.71594 13.0029 5.23676 14.1542 5.23676 15.3539V23.3452C5.23676 24.8313 6.44147 25.6378 7.92757 25.6378H11.7639C12.3161 25.6378 12.7639 25.19 12.7639 24.6378V20.3006V19.3398C12.7639 17.5289 14.2318 16.0609 16.0427 16.0609C17.8536 16.0609 19.3216 17.5289 19.3216 19.3398V20.3006V24.6378C19.3216 25.19 19.7693 25.6378 20.3216 25.6378H24.0724C25.5585 25.6378 26.7632 24.8313 26.7632 23.3452Z"
            stroke="#B8B8B8"
            strokeWidth="3"
          />
        </svg>
      ) : (
        <HomeIconActive width={"23"} height={"23"} />
      )}
    </>
  );
};

const HomeIconActive: React.FC<IProps> = ({ width, height }) => {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M23.8739 20.1633V11.3474C23.8739 10.0239 23.3452 8.75383 22.384 7.8441C21.2571 6.77769 19.6938 5.33905 18.4317 4.33146C16.0442 2.42551 14.8527 0.868256 12 0.868256C9.14728 0.868256 7.95575 2.42551 5.56832 4.33146C4.30619 5.33905 2.74284 6.77769 1.61602 7.8441C0.654757 8.75383 0.126129 10.0239 0.126129 11.3474V20.1633C0.126129 21.8027 1.45516 23.1317 3.09459 23.1317H7.05255C8.14551 23.1317 9.03152 22.2457 9.03152 21.1528V16.8045C9.03152 15.812 9.52756 14.8851 10.3534 14.3346C11.3505 13.6699 12.6495 13.6699 13.6466 14.3346C14.4724 14.8851 14.9685 15.812 14.9685 16.8045V21.1528C14.9685 22.2457 15.8545 23.1317 16.9474 23.1317H20.9054C22.5448 23.1317 23.8739 21.8027 23.8739 20.1633Z"
        fill="black"
      />
    </svg>
  );
};

const SearchIcon: React.FC<IProps> = ({ width, height, isActive }) => {
  return (
    <>
      {!isActive ? (
        <svg
          width={width}
          height={height}
          viewBox="0 0 32 37"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M21.1832 21.1672C22.8477 19.4946 23.8765 17.1887 23.8765 14.6425C23.8765 9.53385 19.7351 5.39249 14.6265 5.39249C9.51786 5.39249 5.3765 9.53385 5.3765 14.6425C5.3765 19.7511 9.51786 23.8925 14.6265 23.8925C17.1889 23.8925 19.508 22.8505 21.1832 21.1672ZM21.1832 21.1672L26.6235 26.6075"
            stroke="#B8B8B8"
            strokeWidth="3"
            strokeLinecap="round"
          />
        </svg>
      ) : (
        <SearchIconActive width={"30"} height={"30"} />
      )}
    </>
  );
};

const SearchIconActive: React.FC<IProps> = ({ width, height }) => {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 32 32"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M21.1832 21.1672C22.8477 19.4946 23.8765 17.1887 23.8765 14.6425C23.8765 9.53385 19.7351 5.39249 14.6265 5.39249C9.51786 5.39249 5.3765 9.53385 5.3765 14.6425C5.3765 19.7511 9.51786 23.8925 14.6265 23.8925C17.1889 23.8925 19.508 22.8505 21.1832 21.1672ZM21.1832 21.1672L26.6235 26.6075"
        stroke="black"
        strokeWidth="3"
        strokeLinecap="round"
      />
    </svg>
  );
};

const PlusIcon: React.FC<IProps> = ({ width, height, isActive }) => {
  return (
    <svg
      fill={isActive ? "#000000" : "#B8B8B8"}
      version="1.1"
      id="Capa_1"
      xmlns="http://www.w3.org/2000/svg"
      xmlnsXlink="http://www.w3.org/1999/xlink"
      width={`${width}px`}
      height={`${height}px`}
      viewBox="0 0 45.402 45.402"
      xmlSpace="preserve"
    >
      <g>
        <path
          d="M41.267,18.557H26.832V4.134C26.832,1.851,24.99,0,22.707,0c-2.283,0-4.124,1.851-4.124,4.135v14.432H4.141
		c-2.283,0-4.139,1.851-4.138,4.135c-0.001,1.141,0.46,2.187,1.207,2.934c0.748,0.749,1.78,1.222,2.92,1.222h14.453V41.27
		c0,1.142,0.453,2.176,1.201,2.922c0.748,0.748,1.777,1.211,2.919,1.211c2.282,0,4.129-1.851,4.129-4.133V26.857h14.435
		c2.283,0,4.134-1.867,4.133-4.15C45.399,20.425,43.548,18.557,41.267,18.557z"
        />
      </g>
    </svg>
  );
};

const HeartIcon: React.FC<IProps> = ({ width, height, isActive }) => {
  return (
    <>
      {!isActive ? (
        <svg
          width={width}
          height={height}
          viewBox="0 0 32 37"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M23.8983 7.38499C20.594 4.72024 17.2151 7.38499 16 8.60988C14.7848 7.38499 11.406 4.72024 8.10169 7.38499C4.79734 10.0497 4.07919 15.5791 8.70925 20.2464C13.3393 24.9137 16 25.7585 16 25.7585C16 25.7585 18.6607 24.9137 23.2907 20.2464C27.9208 15.5791 27.2026 10.0497 23.8983 7.38499Z"
            stroke="#B8B8B8"
            strokeWidth="3"
          />
        </svg>
      ) : (
        <HeartIconActive width={width} height={height} />
      )}
    </>
  );
};

const HeartIconActive: React.FC<IProps> = ({ width, height }) => {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 32 32"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M23.8983 7.38499C20.594 4.72024 17.2151 7.38499 16 8.60988C14.7848 7.38499 11.406 4.72024 8.10169 7.38499C4.79734 10.0497 4.07919 15.5791 8.70925 20.2464C13.3393 24.9137 16 25.7585 16 25.7585C16 25.7585 18.6607 24.9137 23.2907 20.2464C27.9208 15.5791 27.2026 10.0497 23.8983 7.38499Z"
        fill="black"
        stroke="black"
        strokeWidth="2"
      />
    </svg>
  );
};

const UserIcon: React.FC<IProps> = ({ width, height, isActive }) => {
  return (
    <>
      {!isActive ? (
        <svg
          width={width}
          height={height}
          viewBox="0 0 32 37"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M8.46644 26.4102H23.4972C24.9332 26.4102 25.9244 24.9722 25.4133 23.6301C24.2021 20.4494 21.1522 18.3473 17.7487 18.3473H14.1657C10.7482 18.3473 7.7025 20.5033 6.5667 23.7265C6.10496 25.0369 7.07714 26.4102 8.46644 26.4102Z"
            stroke="#B8B8B8"
            strokeWidth="3"
          />
          <path
            d="M16.0465 13.7291C18.2941 13.7291 20.1162 11.9071 20.1162 9.65945C20.1162 7.41184 18.2941 5.58978 16.0465 5.58978C13.7989 5.58978 11.9768 7.41184 11.9768 9.65945C11.9768 11.9071 13.7989 13.7291 16.0465 13.7291Z"
            stroke="#B8B8B8"
            strokeWidth="3"
          />
        </svg>
      ) : (
        <UserIconActive width={width} height={height} />
      )}
    </>
  );
};

const UserIconActive: React.FC<IProps> = ({ width, height }) => {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 32 32"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M8.46644 26.4102H23.4972C24.9332 26.4102 25.9244 24.9722 25.4133 23.6301C24.2021 20.4494 21.1522 18.3473 17.7487 18.3473H14.1657C10.7482 18.3473 7.7025 20.5033 6.5667 23.7265C6.10496 25.0369 7.07714 26.4102 8.46644 26.4102Z"
        fill="black"
      />
      <path
        d="M16.0465 13.7291C18.2941 13.7291 20.1162 11.9071 20.1162 9.65945C20.1162 7.41184 18.2941 5.58978 16.0465 5.58978C13.7989 5.58978 11.9768 7.41184 11.9768 9.65945C11.9768 11.9071 13.7989 13.7291 16.0465 13.7291Z"
        fill="black"
      />
      <path
        d="M8.46644 26.4102H23.4972C24.9332 26.4102 25.9244 24.9722 25.4133 23.6301C24.2021 20.4494 21.1522 18.3473 17.7487 18.3473H14.1657C10.7482 18.3473 7.7025 20.5033 6.5667 23.7265C6.10496 25.0369 7.07714 26.4102 8.46644 26.4102Z"
        stroke="black"
        strokeWidth="2"
      />
      <path
        d="M16.0465 13.7291C18.2941 13.7291 20.1162 11.9071 20.1162 9.65945C20.1162 7.41184 18.2941 5.58978 16.0465 5.58978C13.7989 5.58978 11.9768 7.41184 11.9768 9.65945C11.9768 11.9071 13.7989 13.7291 16.0465 13.7291Z"
        stroke="black"
        strokeWidth="2"
      />
    </svg>
  );
};

const PinIcon: React.FC<IProps> = ({ width, height }) => {
  return (
    <svg
      className="pin-icon"
      role="img"
      viewBox="0 0 24 24"
      width={width}
      height={height}
    >
      <path d="M12 23.922c-.072 0-.166-.085-.283-.254a3.489 3.489 0 0 1-.352-.654 5.193 5.193 0 0 1-.293-.899 4.25 4.25 0 0 1-.117-.976v-5.576h2.08v5.576c0 .319-.039.644-.117.976a5.202 5.202 0 0 1-.293.899 3.489 3.489 0 0 1-.352.654c-.11.17-.201.254-.273.254ZM5.78 16.49c-.482 0-.87-.14-1.163-.42-.286-.286-.43-.66-.43-1.123 0-.748.2-1.478.596-2.187.397-.71.947-1.345 1.65-1.905a8.372 8.372 0 0 1 2.481-1.328c.95-.332 1.98-.498 3.086-.498 1.107 0 2.132.166 3.076.498a8.372 8.372 0 0 1 2.48 1.329c.71.56 1.26 1.194 1.651 1.904.397.71.596 1.439.596 2.187 0 .463-.143.837-.43 1.123-.286.28-.67.42-1.152.42H5.779Zm.488-1.787h11.455c.182 0 .257-.104.224-.312-.058-.43-.244-.86-.556-1.29-.313-.43-.73-.82-1.25-1.171a6.823 6.823 0 0 0-1.836-.85A7.792 7.792 0 0 0 12 10.758a7.89 7.89 0 0 0-2.314.322 6.85 6.85 0 0 0-1.827.85c-.52.351-.937.742-1.25 1.172-.312.43-.5.859-.566 1.289-.033.208.042.312.225.312Zm-.84-13.086c0-.338.117-.618.351-.84.241-.228.554-.341.938-.341h10.566c.384 0 .694.113.928.341.24.222.361.502.361.84 0 .352-.136.7-.41 1.045a5.307 5.307 0 0 1-.693.723c-.293.26-.632.534-1.016.82-.384.287-.784.573-1.201.86l.361 5.41h-1.875l-.361-6.24c-.013-.17.042-.284.166-.342.3-.163.583-.326.85-.489.273-.162.514-.315.722-.459.209-.143.381-.27.518-.38.137-.118.23-.202.283-.254.046-.053.055-.098.03-.137-.02-.04-.056-.059-.108-.059H8.152a.123.123 0 0 0-.107.059c-.02.039-.01.084.03.137.051.052.146.136.282.253.144.111.32.238.528.381.215.144.452.297.713.46.267.162.553.325.859.488.124.058.182.172.176.341l-.371 6.24H8.377l.371-5.41a32.5 32.5 0 0 1-1.21-.859 19.68 19.68 0 0 1-1.017-.82 5.57 5.57 0 0 1-.683-.723c-.274-.345-.41-.693-.41-1.045Z"></path>
    </svg>
  );
};

const MenuIcon: React.FC<IProps> = ({ width, height }) => {
  return (
    <svg
      className="menu-icon"
      width={`${width}px`}
      height={`${height}px`}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M4 6H20M4 12H14M4 18H9"
        // stroke="#B8B8B8"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
};

const RoomIcon: React.FC<IProps> = ({ width, height, isActive }) => {
  return (
    <>
      {!isActive ? (
        <svg
          fill="#B8B8B8"
          version="1.1"
          id="Capa_1"
          xmlns="http://www.w3.org/2000/svg"
          xmlnsXlink="http://www.w3.org/1999/xlink"
          width={width}
          height={height}
          viewBox="0 0 520.349 520.349"
          xmlSpace="preserve"
        >
          <g>
            <path
              d="M445.223,0H142.589c-6.683,0-12.105,5.423-12.105,12.105v180.979h16.65c-5.006-6.392-7.725-14.224-7.725-22.467
		c-0.006-9.764,3.8-18.943,10.708-25.845c1.421-1.421,2.973-2.687,4.583-3.845V24.211h278.417v8.697l-127.104,92.285v395.155
		l127.796-92.787c1.626,4.77,6.095,8.228,11.414,8.228c6.685,0,12.105-5.426,12.105-12.105V12.105
		C457.328,5.417,451.907,0,445.223,0z M354.031,331.973c-5.71,0-10.468-7.046-11.691-16.485h-13.63v-10.592h13.819
		c1.448-8.86,6.017-15.38,11.49-15.38c6.638,0,12.011,9.498,12.011,21.231C366.042,322.468,360.663,331.973,354.031,331.973z
		 M150.122,314.471c1.424,1.412,2.967,2.678,4.572,3.824v105.389c0,6.68-5.426,12.105-12.105,12.105
		c-6.683,0-12.105-5.426-12.105-12.105V266.139h16.65C135.948,280.466,136.935,301.271,150.122,314.471z M236.706,218.385
		c4.817,4.817,4.817,12.608,0,17.425l-58.995,59.001c-2.403,2.394-5.556,3.605-8.709,3.611c-3.153-0.006-6.307-1.206-8.71-3.611
		c-4.811-4.817-4.805-12.613,0-17.419l37.974-37.977H75.336c-6.803,0-12.315-5.512-12.315-12.315
		c0-6.803,5.506-12.318,12.321-12.318h122.917l-37.968-37.974c-4.805-4.805-4.811-12.608,0-17.413
		c4.812-4.812,12.614-4.812,17.425,0L236.706,218.385z"
            />
          </g>
        </svg>
      ) : (
        <svg
          fill="#000000"
          version="1.1"
          id="Capa_1"
          xmlns="http://www.w3.org/2000/svg"
          xmlnsXlink="http://www.w3.org/1999/xlink"
          width={width}
          height={height}
          viewBox="0 0 520.349 520.349"
          xmlSpace="preserve"
        >
          <g>
            <path
              d="M445.223,0H142.589c-6.683,0-12.105,5.423-12.105,12.105v180.979h16.65c-5.006-6.392-7.725-14.224-7.725-22.467
		c-0.006-9.764,3.8-18.943,10.708-25.845c1.421-1.421,2.973-2.687,4.583-3.845V24.211h278.417v8.697l-127.104,92.285v395.155
		l127.796-92.787c1.626,4.77,6.095,8.228,11.414,8.228c6.685,0,12.105-5.426,12.105-12.105V12.105
		C457.328,5.417,451.907,0,445.223,0z M354.031,331.973c-5.71,0-10.468-7.046-11.691-16.485h-13.63v-10.592h13.819
		c1.448-8.86,6.017-15.38,11.49-15.38c6.638,0,12.011,9.498,12.011,21.231C366.042,322.468,360.663,331.973,354.031,331.973z
		 M150.122,314.471c1.424,1.412,2.967,2.678,4.572,3.824v105.389c0,6.68-5.426,12.105-12.105,12.105
		c-6.683,0-12.105-5.426-12.105-12.105V266.139h16.65C135.948,280.466,136.935,301.271,150.122,314.471z M236.706,218.385
		c4.817,4.817,4.817,12.608,0,17.425l-58.995,59.001c-2.403,2.394-5.556,3.605-8.709,3.611c-3.153-0.006-6.307-1.206-8.71-3.611
		c-4.811-4.817-4.805-12.613,0-17.419l37.974-37.977H75.336c-6.803,0-12.315-5.512-12.315-12.315
		c0-6.803,5.506-12.318,12.321-12.318h122.917l-37.968-37.974c-4.805-4.805-4.811-12.608,0-17.413
		c4.812-4.812,12.614-4.812,17.425,0L236.706,218.385z"
            />
          </g>
        </svg>
      )}
    </>
  );
};

export {
  Logo,
  HomeIcon,
  HomeIconActive,
  SearchIcon,
  SearchIconActive,
  PlusIcon,
  HeartIcon,
  HeartIconActive,
  UserIcon,
  UserIconActive,
  PinIcon,
  MenuIcon,
  RoomIcon,
};
