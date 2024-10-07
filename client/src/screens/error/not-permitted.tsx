import { Button, Result } from "antd";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../redux/store/hook";

const NotPermitted = () => {
  const navigate = useNavigate();
  const role = useAppSelector((state) => state.user.role);

  return (
    <>
      <Result
        status="403"
        title="403"
        subTitle="Sorry, you are not authorized to access this page."
        extra={
          <Button
            type="primary"
            onClick={() => {
              if (role === "ADMIN") {
                navigate("/admin");
              } else if (role === "USER") {
                navigate("/");
              }
            }}
          >
            Back Home
          </Button>
        }
        className="custom-result"
      />
    </>
  );
};
export default NotPermitted;
