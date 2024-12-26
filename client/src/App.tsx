import {RouterProvider} from "react-router-dom";
import router from "./router/router";
import {ConfigProvider, message} from "antd";
import {globalTheme} from "./utils/theme";
import Cookies from "js-cookie";
import {useEffect, useRef, useState} from "react";
import {useAppDispatch} from "./redux/store/hook";
import {getCurrentUser, handleRefreshToken} from "./service/userAPI";
import {storeInformationUser} from "./redux/slice/user-slice";
import "react-photo-view/dist/react-photo-view.css";
import {Helmet, HelmetProvider} from "react-helmet-async";
import {EventSource} from "eventsource";

interface INotificationType {
  id: string;
  type: string;
  artifactId: string;
  artifactType: string;
  artifactPreview: string;
  lastActor?: {
    id: string;
    username: string;
    avatarUrl: string;
  };
  numberOfActors: number;
  changeAt: string;
  read: boolean;
}
function App() {
  const dispatch = useAppDispatch();
  const access_token = Cookies.get("access_token") || null;
  const eventSourceRef = useRef<EventSource | null>(null);

  const [retry, setRetry] = useState(false);

  const fetchCurrentUser = async () => {
    try {
      const res = await getCurrentUser();
      dispatch(
        storeInformationUser({
          id: res?.id,
          username: res?.username,
          firstName: res?.firstName || "",
          lastName: res?.lastName || "",
          role: res?.role || "USER",
          status: res?.status || "INACTIVE",
          avatarUrl: res?.avatarUrl || "",
        })
      );
    } catch (err) {
      try {
        if (retry) {
          throw new Error();
        }

        const response = await handleRefreshToken();
        if (response && response.access_token) {
          Cookies.set("access_token", response.access_token);
          Cookies.set("refresh_token", response.refresh_token);
          Cookies.set("id_token", response.id_token);

          setRetry(true);
        }
      } catch (err) {
        console.error("Token refresh failed:", err);

        Cookies.remove("access_token");
        Cookies.remove("refresh_token");
        Cookies.remove("id_token");

        window.location.href = "/login";
      }
    }
  };

  const mapperMessageFromEventSource = (data: INotificationType) => {
    const { type, lastActor } = data;
    const username = lastActor?.username || "Someone";

    switch (type) {
      case "NEW_PENDING_POST":
        return "You have a new pending post";
      case "APPROVE_POST":
        return "Your post has been approved";
      case "REJECT_POST":
        return "Your post has been rejected";
      case "LIKE_POST":
        return `${username} liked your post`;
      case "REPLY_POST":
        return `${username} replied to your post`;
      case "REPOST_POST":
        return `${username} reposted your post`;
      case "FOLLOW":
        return `${username} followed you`;
      case "REQUEST_FOLLOW":
        return `${username} sent you a follow request`;
      case "ACCEPT_FOLLOW":
        return "Your follow request has been accepted";
      default:
        return "You have a new notification";
    }
  };

  useEffect(() => {
    const eventSource = new EventSource(
      `${import.meta.env.VITE_BACKEND_URL}/notifications/sse-stream`,
      {
        fetch: (input, init) =>
          fetch(input, {
            ...init,
            headers: {
              ...init?.headers,
              Authorization: `Bearer ${Cookies.get("access_token")}`,
            },
          }),
      }
    );

    eventSource.onmessage = (event) => {
      if (event.data) {
        message.info(mapperMessageFromEventSource(JSON.parse(event.data)));
      }
    };

    eventSource.onerror = (error) => {
      console.error(error);
      eventSource.close();
    };

    eventSourceRef.current = eventSource;

    return () => {
      if (eventSourceRef.current) {
        eventSourceRef.current.close();
      }
    };
  }, []);

  useEffect(() => {
    if (access_token) {
      fetchCurrentUser();
    }
  }, [access_token, retry, dispatch]);

  return (
    <>
      {
        <HelmetProvider>
          <Helmet>
            <title>Star</title>
          </Helmet>
          <ConfigProvider theme={globalTheme}>
            <RouterProvider router={router} />
          </ConfigProvider>
        </HelmetProvider>
      }
    </>
  );
}

export default App;
