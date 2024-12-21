import { createBrowserRouter } from "react-router-dom";
import NotFound from "../screens/error/not-found";
import SignUp from "../screens/auth/SignUp";
import Confirm from "../screens/auth/Confirm";
import Login from "../screens/auth/Login";
import Callback from "../screens/auth/Callback";
import LayoutUser from "../screens/layout/layout-user";
import LayoutAdmin from "../screens/layout/layout-admin";
import Room from "../components/admin/room/room";
import Post from "../components/admin/post/post.tsx";
import User from "../components/admin/user/user.tsx";
import { ProtectedRouteAuth } from "../components/auth/protected-route-auth";
import Profile from "../screens/profile/profile";
import { ProtectedRoute } from "../components/auth/protected-route";
import RoomUser from "../screens/room/room-user";
import PeopleUser from "../screens/people/people-user";
import NewFeed from "../screens/newfeeds/newfeed";
import PostInRoomContainer from "../screens/post-in-room/PostInRoomContainer";
import ModeratorContainer from "../screens/moderator/moderator-container";
import PendingPosts from "../components/user/moderator/pending-posts";
import ApprovedPosts from "../components/user/moderator/approved-post";
import RejectedPosts from "../components/user/moderator/rejected-post";
import RoomDetails from "../components/admin/room/room-details.tsx";
import DetailPost from "../screens/detail-post/DetailPost.tsx";
import PostDetails from "../components/admin/post/post-details.tsx";
import UserDetails from "../components/admin/user/user-details.tsx";
import Activity from "../screens/activity/activity.tsx";

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
          element: <NewFeed />,
        },
        {
          path: "profile/:id",
          element: <Profile />,
        },
        {
          path: "search",
          element: <PeopleUser />,
        },
        {
          path: "activity",
          element: <Activity />,
        },
        {
          path: "room",
          element: <RoomUser />,
        },
        {
          path: "room/:roomId/posts",
          element: <PostInRoomContainer />,
        },
        {
          path: "moderator/:roomId",
          element: <ModeratorContainer />,
          children: [
            { path: "pending", element: <PendingPosts /> },
            { path: "approved", element: <ApprovedPosts /> },
            { path: "rejected", element: <RejectedPosts /> },
          ],
        },
        {
          path: "post/:postId",
          element: <DetailPost />,
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
          element: <User />,
        },
        {
          path: "users/:userId",
          element: <UserDetails />,
        },
        {
          path: "posts",
          element: <Post />,
        },
        {
          path: "posts/:postId",
          element: <PostDetails />,
        },
        {
          path: "rooms",
          element: <Room />,
        },
        {
          path: "rooms/:roomId",
          element: <RoomDetails />,
        },
      ],
    },
    {
      path: "/sign-up",
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
      path: "/confirm-sign-up",
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
