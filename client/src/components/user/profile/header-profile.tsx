import { Button, Dropdown, MenuProps } from "antd";
import { EllipsisOutlined } from "@ant-design/icons";
import { useAppDispatch, useAppSelector } from "../../../redux/store/hook";
import optionPin from "../../../utils/optionPin";
import { useNavigate } from "react-router-dom";
import {
  addPinPageToRedux,
  removePinPageToRedux,
} from "../../../redux/slice/user-slice";
const HeaderProfile: React.FC = () => {
  const pinnedPage = useAppSelector((state) => state.user.pin);
  const isPinnedProfile = pinnedPage?.includes(optionPin.PROFILE);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const items: MenuProps["items"] = [
    {
      key: "1",
      label: (
        <div className="w-[120px] h-[35px] text-[16px] font-semibold flex items-center flex-start">
          {isPinnedProfile ? "Unpin" : "Pin to home"}
        </div>
      ),
      onClick: () => {
        if (isPinnedProfile) {
          dispatch(removePinPageToRedux(optionPin.PROFILE));
        } else {
          dispatch(addPinPageToRedux(optionPin.PROFILE));
          navigate("/");
        }
      },
    },
  ];
  return (
    <>
      {/* Header Profile */}
      <div className="flex">
        <div style={{ flexGrow: 1 }}></div>
        <div style={{ flexGrow: 4, textAlign: "center", fontWeight: 500 }}>
          Profile
        </div>
        <div style={{ flexGrow: 1 }}>
          <Dropdown menu={{ items }} placement="topRight" arrow={false}>
            <Button
              icon={<EllipsisOutlined />}
              style={{ borderRadius: "50%", width: "25px", height: "25px" }}
            />
          </Dropdown>
        </div>
      </div>
    </>
  );
};
export default HeaderProfile;
