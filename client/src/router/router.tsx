import { createBrowserRouter } from "react-router-dom";
import NotFound from "../screens/error/not-found";
import { LayoutAdmin, LayoutUser } from "./layout";
import SignUp from "../screens/auth/SignUp";

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
      element: <div>login page</div>,
      errorElement: <NotFound />,
    },
  ],
  {
    basename: import.meta.env.VITE_BASE_URL,
  }
);
export default router;
