import { createBrowserRouter } from "react-router-dom";
import NotFound from "../screens/error/not-found";
import { LayoutAdmin, LayoutUser } from "./layout";

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
  ],
  {
    basename: import.meta.env.VITE_BASE_URL,
  }
);
export default router;
