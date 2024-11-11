import { ConfigProvider, Tabs } from "antd";
import { useParams, useNavigate, Outlet } from "react-router-dom";
import { moderatorTheme } from "../../utils/theme";

const { TabPane } = Tabs;
const ModeratorContainer = () => {
  const { roomId } = useParams<{ roomId: string; status: string }>();
  const url = window.location.href.split("/");
  const status = url[url.length - 1];
  const navigate = useNavigate();

  const onTabChange = (key: string) => {
    navigate(`/moderator/${roomId}/${key}`);
    window.scrollTo(0, 0);
  };

  return (
    <ConfigProvider theme={moderatorTheme}>
      <div className="w-full flex justify-center bg-white">
        <div
          className="h-full pt-2"
          style={{ width: "100%", maxWidth: "650px" }}
        >
          <div
            style={{
              border: "1px solid #ccc",
              marginTop: "20px",
              height: "100%",
              borderRadius: "30px",
              paddingTop: "10px",
            }}
          >
            <Tabs
              onChange={onTabChange}
              tabBarStyle={{ borderBottom: "1px solid #d9d9d9" }}
              destroyInactiveTabPane={true}
              activeKey={status}
            >
              <TabPane
                tab={<div className="text-[16px] font-semibold">Pending</div>}
                key="pending"
              />
              <TabPane
                tab={<div className="text-[16px] font-semibold">Approved</div>}
                key="approved"
              />
              <TabPane
                tab={<div className="text-[16px] font-semibold">Rejected</div>}
                key="rejected"
              />
            </Tabs>
            <Outlet />
          </div>
        </div>
      </div>
    </ConfigProvider>
  );
};
export default ModeratorContainer;
