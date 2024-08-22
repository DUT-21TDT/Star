import { Button, Result } from "antd";
const NotPermitted = () => {
  return (
    <>
      <Result
        status="403"
        title="403"
        subTitle="Sorry, you are not authorized to access this page."
        extra={<Button type="primary">Back Home</Button>}
        className="custom-result"
      />
    </>
  );
};
export default NotPermitted;
