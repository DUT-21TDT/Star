import { createBrowserRouter } from "react-router-dom";
import NotFound from "../screens/error/not-found";
import SignUp from "../screens/auth/SignUp";
import Confirm from "../screens/auth/Confirm";
import Login from "../screens/auth/Login";
import Callback from "../screens/auth/Callback";
import LayoutUser from "../screens/layout/layout-user";
import LayoutAdmin from "../screens/layout/layout-admin";
import Room from "../components/admin/room/room";
import { ProtectedRouteAuth } from "../components/auth/protected-route-auth";
import Profile from "../screens/profile/profile";
import { ProtectedRoute } from "../components/auth/protected-route";

const router = createBrowserRouter(
  [
    {
      path: "/",
      element: (
        <ProtectedRoute>
          <LayoutUser />
        </ProtectedRoute>
      ),
      errorElement: <NotFound />,
      children: [
        {
          index: true,
          element: <div>homepage</div>,
        },
        {
          path: "profile/:id",
          element: <Profile />,
        },
        {
          path: "search",
          element: <div>search</div>,
        },
        {
          path: "activity",
          element: <div>activity</div>,
        },
      ],
    },
    {
      path: "/admin",
      element: (
        <ProtectedRoute>
          <LayoutAdmin />
        </ProtectedRoute>
      ),
      errorElement: <NotFound />,
      children: [
        {
          index: true,
          element: <div>dashboard</div>,
        },
        {
          path: "users",
          element: <div>users</div>,
        },
        {
          path: "posts",
          element: <div>posts</div>,
        },
        {
          path: "rooms",
          element: <Room />,
        },
      ],
    },
    {
      path: "/signup",
      element: (
        <ProtectedRouteAuth>
          <SignUp />
        </ProtectedRouteAuth>
      ),
      errorElement: <NotFound />,
    },
    {
      path: "/login",
      element: (
        <ProtectedRouteAuth>
          <Login />
        </ProtectedRouteAuth>
      ),
      errorElement: <NotFound />,
    },
    {
      path: "/confirm-signup",
      element: <Confirm />,
      errorElement: <NotFound />,
    },
    {
      path: "/callback",
      element: <Callback />,
      errorElement: <NotFound />,
    },
  ],
  {
    basename: import.meta.env.VITE_BASE_URL,
  }
);
export default router;
