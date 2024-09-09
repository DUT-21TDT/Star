import { useState } from "react";

import { ConfigProvider, Layout, Menu, theme } from "antd";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import HeaderAdmin from "../components/layout-admin/header-admin";
import {
  CreditCardOutlined,
  HomeOutlined,
  UserOutlined,
  DeploymentUnitOutlined,
} from "@ant-design/icons";
import { adminTheme } from "../utils/theme";

const { Sider, Content } = Layout;

const LayoutAdmin = () => {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  const navigate = useNavigate();
  const location = useLocation();

  const urls = ["", "users", "posts", "rooms"];

  //Add env variable to get the current key from the url
  const currentKey =
    urls.findIndex(
      (url) => url && location.pathname.startsWith(`/admin/${url}`)
    ) + 1;
  const label = ["Dashboard", "User", "Post", "Room"];
  const icons = [
    <HomeOutlined />,
    <UserOutlined />,
    <CreditCardOutlined />,
    <DeploymentUnitOutlined />,
  ];

  const items = Array.from({ length: 4 }, (_, i) => ({
    key: `${i + 1}`,
    icon: icons[i],
    label: label[i],
    onClick: () => navigate(`${urls[i]}`),
  }));

  return (
    <ConfigProvider theme={adminTheme}>
      <Layout>
        <Sider trigger={null} collapsible collapsed={collapsed} theme="light">
          <div className="w-full h-[100px] flex items-center justify-center p-[10px] text-[24px] text-[#013CC6] font-medium">
            Star
          </div>
          <Menu
            mode="inline"
            defaultSelectedKeys={[`${currentKey === 0 ? 1 : currentKey}`]}
            items={items}
          />
        </Sider>
        <Layout>
          <HeaderAdmin collapsed={collapsed} setCollapsed={setCollapsed} />
          <Content
            style={{
              margin: "24px 16px",
              padding: 24,
              minHeight: 500,
              maxHeight: "100%",
              background: colorBgContainer,
              borderRadius: borderRadiusLG,
            }}
          >
            <Outlet />
          </Content>
        </Layout>
      </Layout>
    </ConfigProvider>
  );
};
export default LayoutAdmin;
