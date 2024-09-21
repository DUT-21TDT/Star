import { useNavigate } from "react-router-dom";
import {
  HeartIcon,
  HomeIcon,
  Logo,
  MenuIcon,
  PinIcon,
  PlusIcon,
  SearchIcon,
  UserIcon,
} from "../../../assets/icon/sidebar-homepage-icon";
import { useState } from "react";
import { useAppSelector } from "../../../redux/store/hook";

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
      name: "user",
      component: UserIcon,
      width: "33",
      height: "33",
      navigate: `/profile/${id}`,
    },
  ];
  const [activeIcon, setActiveIcon] = useState<string>("home");
  const navigate = useNavigate();
  return (
    <div className=" w-[70px] h-[calc(100vh-50px)] flex flex-col justify-between items-center ">
      {/* Logo */}
      <div className="flex-grow flex justify-center items-center">
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
                setActiveIcon(name);
                if (iconNavigate) {
                  navigate(iconNavigate);
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

      {/* Bottom Icons */}
      <div className="flex-grow flex flex-col items-center gap-3 justify-end">
        <PinIcon width="30" height="30" />
        <MenuIcon width="30" height="30" />
      </div>
    </div>
  );
};

export default SideBar;
