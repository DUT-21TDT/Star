import { Result, Spin } from "antd";
import { useGetTokenFromCode } from "../../hooks/user";
import { LoadingOutlined } from "@ant-design/icons";
import { useEffect, useState } from "react";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import { getCurrentUser } from "../../service/userAPI";
import { useAppDispatch } from "../../redux/store/hook";
import { storeInformationUser } from "../../redux/slice/user-slice";

type CurrentUser = {
  id: string;
  username: string;
  role: string;
  status: string;
  avatarUrl: string;
};

const Callback: React.FC = () => {
  const code = new URLSearchParams(window.location.search).get("code") || "";
  const { access_token, refresh_token, id_token, isLoading, isError } =
    useGetTokenFromCode(code);
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null);

  const navigate = useNavigate();

  const dispatch = useAppDispatch();

  useEffect(() => {
    if (access_token && refresh_token && id_token) {
      Cookies.set("access_token", access_token);
      Cookies.set("refresh_token", refresh_token);
      Cookies.set("id_token", id_token);

      getCurrentUser().then((res) => {
        setCurrentUser({
          id: res?.id,
          username: res?.username,
          role: res?.role || "USER",
          status: res?.status || "INACTIVE",
          avatarUrl: res?.avatarUrl || ""
        });

        //store user data in redux
        dispatch(
          storeInformationUser({
            id: res?.id,
            username: res?.username,
            role: res?.role || "USER",
            status: res?.status || "INACTIVE",
            avatarUrl: res?.avatarUrl || ""
          })
        );
      });
    }
  }, [access_token, refresh_token, id_token, dispatch]);

  useEffect(() => {
    if (currentUser?.role === "USER") {
      navigate("/");
    } else if (currentUser?.role === "ADMIN") {
      navigate("/admin");
    }
  }, [currentUser, navigate]);
  return (
    <>
      {isLoading && (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      )}
      {isError && (
        <Result
          status="error"
          title="Something went wrong"
          subTitle="Please try again."
        />
      )}
    </>
  );
};
export default Callback;
