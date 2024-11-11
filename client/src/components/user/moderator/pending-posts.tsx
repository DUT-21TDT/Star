import { useEffect, useState } from "react";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import { useGetAllPendingPostForModerator } from "../../../hooks/post";
import { useParams } from "react-router-dom";
import PostModerator from "./post-moderator";
import ModalConfirmApprovePost from "./modal-confirm-approve-post";

interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  idOfCreator?: string;
  nameOfRoom?: string | null;
  idOfModerator?: string | null;
  usernameOfModerator?: string | null;
  moderatedAt?: string | null;
  violenceScore: number;
  status: string;
}

const PendingPosts = () => {
  const queryClient = useQueryClient();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<PostType[]>([]);
  const [dataPostModal, setDataPostModal] = useState<PostType | null>(null);
  const [isOpenModal, setIsOpenModal] = useState<boolean>(false);
  const [statusWantToChange, setStatusWantToChange] = useState<string>("");
  const { roomId = "" } = useParams<{ roomId: string }>();
  const { dataPost, isLoading, hasNextPost } = useGetAllPendingPostForModerator(
    {
      limit: 10,
      after: afterTime,
    },
    roomId,
    "PENDING"
  );

  const handleScroll = () => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;

    if (isBottom && hasNextPost) {
      queryClient.invalidateQueries({
        queryKey: QUERY_KEY.fetchAllPendingPostForModerator(roomId, "PENDING"),
      });
    }
  };

  useEffect(() => {
    setAllPosts([]);
    setAfterTime(null);
  }, [roomId]);

  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts: PostType[]) => [
        ...prevPosts,
        ...dataPost.map((post: PostType) => ({
          ...post,
          postImageUrls: post.postImageUrls || [],
        })),
      ]);
      const lastPost = dataPost[dataPost.length - 1];
      setAfterTime(lastPost.createdAt);
    }
  }, [dataPost]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost]);
  return (
    <>
      {isLoading ? (
        <div className="flex items-center justify-center mt-8">
          <Spin indicator={<LoadingOutlined spin />} size="large" />
        </div>
      ) : (
        allPosts.map((post) => (
          <PostModerator
            key={`pending-${post.id}`}
            postData={post}
            setDataPostModal={setDataPostModal}
            setOpenModal={setIsOpenModal}
            setStatusWantToChange={setStatusWantToChange}
          />
        ))
      )}
      <ModalConfirmApprovePost
        isOpenModal={isOpenModal}
        setIsOpenModal={setIsOpenModal}
        dataPost={dataPostModal}
        statusWantToChange={statusWantToChange}
      />
    </>
  );
};

export default PendingPosts;
