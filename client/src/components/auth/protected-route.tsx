import React from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { AppDispatch } from "../../redux/store/store";

import Cookies from "js-cookie";

const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  useEffect(() => {
    const access_token: string | undefined = Cookies.get("access_token");
    if (access_token == null) {
      navigate("/login");
    }
  }, [dispatch, navigate]);

  return <>{children}</>;
};

export { ProtectedRoute };
