import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../redux/store/hook";

import Cookies from "js-cookie";

const ProtectedRouteAuth = ({ children }: { children: React.ReactNode }) => {
  const [isChecking, setIsChecking] = useState(true);
  const navigate = useNavigate();
  const userRole = useAppSelector((state) => state.user.role);
  useEffect(() => {
    const access_token: string | undefined = Cookies.get("access_token");
    if (access_token) {
      switch (userRole) {
        case "ADMIN":
          navigate("/admin");
          break;
        case "USER":
          navigate("/");
          break;
      }
    } else {
      setIsChecking(false);
    }
  }, [navigate, userRole]);

  if (isChecking) {
    return null;
  }

  return <>{children}</>;
};

export { ProtectedRouteAuth };
