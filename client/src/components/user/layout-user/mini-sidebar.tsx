import { useNavigate } from "react-router-dom";
import {
  HeartIcon,
  HomeIcon,
  Logo,
  MenuIcon,
  PinIcon,
  PlusIcon,
  RoomIcon,
  SearchIcon,
  UserIcon,
} from "../../../assets/icon/sidebar-homepage-icon";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../../redux/store/hook";
import ModalCreatePost from "../profile/posts/modal-create-post";
import { Avatar, Dropdown, MenuProps } from "antd";
import default_avatar from "../../../assets/images/default_image.jpg";
import { addPinPageToRedux } from "../../../redux/slice/user-slice";
import optionPin from "../../../utils/optionPin";

const MiniSidebar: React.FC = () => {
  const id = useAppSelector((state) => state.user.id);
  const avatarUrl = useAppSelector((state) => state.user.avatarUrl);
  const icons = [
    {
      name: "home",
      component: HomeIcon,
      width: "33",
      height: "33",
      navigate: "/",
      key: "home",
      displayText: "Home",
    },
    {
      name: "search",
      component: SearchIcon,
      width: "33",
      height: "33",
      navigate: "/search",
      key: "people",
      displayText: "Search",
    },
    {
      name: "plus",
      component: PlusIcon,
      width: "22",
      height: "22",
      displayText: "Create",
    },
    {
      name: "heart",
      component: HeartIcon,
      width: "33",
      height: "33",
      navigate: "/activity",
      key: "activity",
      displayText: "Activity",
    },
    {
      name: "room",
      component: RoomIcon,
      width: "33",
      height: "33",
      navigate: "/room",
      key: "room",
      displayText: "Room",
    },
    {
      name: "user",
      component: UserIcon,
      width: "33",
      height: "33",
      navigate: `/profile/${id}`,
      key: "profile",
      displayText: "Profile",
    },
  ];
  const [activeIcon, setActiveIcon] = useState<string>("home");
  const [openModalCreatePost, setOpenModalCreatePost] = useState(false);
  const navigate = useNavigate();
  const path = window.location.pathname;
  const pinned = useAppSelector((state) => state.user.pin);

  useEffect(() => {
    switch (path) {
      case "/":
        setActiveIcon("home");
        break;
      case "/search":
        setActiveIcon("search");
        break;
      case "/activity":
        setActiveIcon("heart");
        break;
      case "/room":
        setActiveIcon("room");
        break;
      case "/following":
        setActiveIcon("home");
        break;
      case "/liked":
        setActiveIcon("home");
        break;
      default:
        setActiveIcon("user");
        break;
    }
  }, [path]);

  const dispatch = useAppDispatch();
  const items: MenuProps["items"] = [
    {
      key: "title",
      label: (
        <div className="w-full text-[15px] text-center font-bold mb-2">
          Pin to home
        </div>
      ),
      type: "group",
    },
    {
      key: "1",
      label: (
        <div className="w-[150px] h-[35px] text-[16px] font-semibold flex items-center flex-start">
          Room
        </div>
      ),
      onClick: () => {
        dispatch(addPinPageToRedux(optionPin.ROOM));
        navigate("/");
      },
    },
    {
      key: "2",
      label: (
        <div className="w-[150px] h-[35px] text-[16px] font-semibold flex items-center flex-start">
          Profile
        </div>
      ),
      onClick: () => {
        dispatch(addPinPageToRedux(optionPin.PROFILE));
        navigate("/");
      },
    },
    {
      key: "3",
      label: (
        <div className="w-[150px] h-[35px] text-[16px] font-semibold flex items-center flex-start">
          People
        </div>
      ),
      onClick: () => {
        dispatch(addPinPageToRedux(optionPin.PEOPLE));
        navigate("/");
      },
    },
    {
      key: "4",
      label: (
        <div className="w-[150px] h-[35px] text-[16px] font-semibold flex items-center flex-start">
          Activity
        </div>
      ),
      onClick: () => {
        dispatch(addPinPageToRedux(optionPin.ACTIVITY));
        navigate("/");
      },
    },
  ];

  return (
    <div
      className=" w-[100px] h-[calc(100vh)] flex flex-col justify-start items-center pt-5 pb-5 pl-4 pr-4"
      style={{
        position: "fixed",
        zIndex: "10",
        backgroundColor: "#fafafa",
        borderRight: "1px solid #dbdbdb",
      }}
    >
      {/* Logo */}
      <div className="flex-grow flex justify-center items-start w-full">
        <Logo width="40" height="40" />
      </div>

      {/* Icon List */}
      <div className="flex-grow flex flex-col items-start gap-4 w-full">
        {icons.map(
          ({
            name,
            component: IconComponent,
            width,
            height,
            navigate: iconNavigate,
            key,
          }) => (
            <div
              key={name}
              className="div-hover w-full items-center justify-center"
              onClick={() => {
                if (name === "plus") {
                  setOpenModalCreatePost(true);
                } else if (
                  name === "home" &&
                  (path === "/" || path === "/following" || path === "/liked")
                ) {
                  window.location.reload();
                } else if (iconNavigate) {
                  if (!pinned?.includes(key) || path !== "/") {
                    setActiveIcon(name);
                    navigate(iconNavigate);
                  }
                }
              }}
            >
              <div className="w-[60px] flex items-center justify-center">
                {name === "user" ? (
                  <Avatar src={avatarUrl || default_avatar} size={33} />
                ) : (
                  <IconComponent
                    width={width}
                    height={height}
                    isActive={activeIcon === name}
                  />
                )}{" "}
              </div>
            </div>
          )
        )}
      </div>

      <ModalCreatePost
        isModalOpen={openModalCreatePost}
        setIsModalOpen={setOpenModalCreatePost}
      />

      {/* Bottom Icons */}
      <div className="flex-grow flex flex-col justify-end items-start w-full pl-2 cursor-pointer">
        <Dropdown
          menu={{ items }}
          placement="topRight"
          arrow={false}
          trigger={["click"]}
        >
          <div className="div-hover w-full flex items-center justify-start pl-3 gap-4">
            <PinIcon width="28" height="28" />
          </div>
        </Dropdown>
        <div className="div-hover w-full flex items-center justify-start pl-3 gap-4">
          <MenuIcon width="30" height="30" />
        </div>
      </div>
    </div>
  );
};

export default MiniSidebar;
