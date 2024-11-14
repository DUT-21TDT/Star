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
import { useAppSelector } from "../../../redux/store/hook";
import ModalCreatePost from "../profile/posts/modal-create-post";

const SideBar: React.FC = () => {
  const id = useAppSelector((state) => state.user.id);
  const icons = [
    {
      name: "home",
      component: HomeIcon,
      width: "33",
      height: "33",
      navigate: "/",
    },
    {
      name: "search",
      component: SearchIcon,
      width: "33",
      height: "33",
      navigate: "/search",
    },
    {
      name: "plus",
      component: PlusIcon,
      width: "22",
      height: "22",
    },
    {
      name: "heart",
      component: HeartIcon,
      width: "33",
      height: "33",
      navigate: "/activity",
    },
    {
      name: "room",
      component: RoomIcon,
      width: "33",
      height: "33",
      navigate: "/room",
    },
    {
      name: "user",
      component: UserIcon,
      width: "33",
      height: "33",
      navigate: `/profile/${id}`,
    },
  ];
  const [activeIcon, setActiveIcon] = useState<string>("home");
  const [openModalCreatePost, setOpenModalCreatePost] = useState(false);
  const navigate = useNavigate();
  const path = window.location.pathname;

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
      default:
        setActiveIcon("user");
        break;
    }
  }, [path]);

  return (
    <div
      className=" w-[70px] h-[calc(100vh-50px)] flex flex-col justify-between items-center pt-5 "
      style={{ position: "fixed" }}
    >
      {/* Logo */}
      <div className="flex-grow flex justify-center items-start">
        <Logo width="40" height="40" />
      </div>

      {/* Icon List */}
      <div className="flex-grow flex flex-col items-center gap-4">
        {icons.map(
          ({
            name,
            component: IconComponent,
            width,
            height,
            navigate: iconNavigate,
          }) => (
            <div
              key={name}
              className="div-hover"
              onClick={() => {
                if (name === "plus") {
                  setOpenModalCreatePost(true);
                } else if (iconNavigate) {
                  setActiveIcon(name);
                  navigate(iconNavigate);
                  // window.scrollTo(0, 0);
                }
              }}
            >
              <IconComponent
                width={width}
                height={height}
                isActive={activeIcon === name}
              />
            </div>
          )
        )}
      </div>

      <ModalCreatePost
        isModalOpen={openModalCreatePost}
        setIsModalOpen={setOpenModalCreatePost}
      />

      {/* Bottom Icons */}
      <div className="flex-grow flex flex-col items-center gap-3 justify-end">
        <PinIcon width="30" height="30" />
        <MenuIcon width="30" height="30" />
      </div>
    </div>
  );
};

export default SideBar;
