import { createBrowserRouter } from "react-router-dom";
import NotFound from "../screens/error/not-found";
import { LayoutAdmin, LayoutUser } from "./layout";
import SignUp from "../screens/auth/SignUp";
import Confirm from "../screens/auth/Confirm";
import Login from "../screens/auth/Login";
import Callback from "../screens/auth/Callback";

const router = createBrowserRouter(
  [
    {
      path: "/",
      element: <LayoutUser />,
      errorElement: <NotFound />,
    },
    {
      path: "/admin",
      element: <LayoutAdmin />,
      errorElement: <NotFound />,
    },
    {
      path: "/signup",
      element: <SignUp />,
      errorElement: <NotFound />,
    },
    {
      path: "/login",
      element: <Login />,
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
