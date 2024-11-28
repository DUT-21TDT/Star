import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useGetRepostsOnWall} from "../../../../hooks/post";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../../utils/queriesKey";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import Post from "./Post";
import { debounce } from "../../../../utils/debounce";

interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[];
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  reposted: boolean;
  idOfCreator: string;
  nameOfRoom: string;
  isRemoved?: boolean;
}

interface RepostType {
  repostedPost: PostType | null;
  repostedByUsername: string;
  caption: string;
  repostedAt: string;
}

const RepostsOnWall = () => {
  const { id: userId } = useParams<{ id: string }>();
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<RepostType[]>([]);
  const { dataPost, isLoading, hasNextPost, afterTimeFinalPost } =
    useGetRepostsOnWall(userId || "", {
      limit: 10,
      after: afterTime,
    });

  useEffect(() => {
    setAfterTime(null);
    setAllPosts([]);
  }, [userId]);

  useEffect(() => {
    if (dataPost && dataPost.length > 0) {

      if (!afterTime) {
        setAllPosts([]);
      }

      setAllPosts((prevPosts: RepostType[]) => [
        ...prevPosts,
        ...dataPost.map((post: RepostType) => {
          const { repostedPost, repostedAt, repostedByUsername, caption } = post;
          return {
            repostedPost: repostedPost
              ? {
                ...post.repostedPost,
                postImageUrls: repostedPost.postImageUrls || [],
                isRemoved: false,
              }
              : null,
            repostedByUsername: repostedByUsername ? repostedByUsername : "",
            caption: caption ? caption : "",
            repostedAt: repostedAt ? repostedAt : "",
          };
        }),
      ]);
    }
  }, [dataPost]);

  const handleScroll = debounce(() => {
    const isBottom =
      window.innerHeight + window.scrollY >=
      document.documentElement.scrollHeight - 1;
    if (isBottom && hasNextPost) {
      setAfterTime(afterTimeFinalPost);
    }
  }, 300);

  const queryClient = useQueryClient();

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [hasNextPost, afterTimeFinalPost]);

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: [QUERY_KEY.fetchRepliesByUserId(userId || "")],
      });
    };
  }, []);

  if (isLoading && allPosts.length === 0) {
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin/>} size="large"/>
      </div>
    );
  } else if (allPosts && allPosts.length === 0) {
    return (
      <div
        className="flex items-center justify-center text-xl"
        style={{fontWeight: 450, marginTop: "25vh", color: "#999999"}}
      >
        No reposts yet
      </div>
    );
  }

  return (
    <div>
      {allPosts &&
        allPosts.map((post) => {
          const {repostedPost, repostedByUsername, caption} = post;
          if (repostedPost) {
            return (
              <div>
                <div className="flex items-center text-gray-400 pl-10 gap-1 mt-2">
                  <svg
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      d="M5 13.5V9C5 7.34315 6.34315 6 8 6H15.5M15.5 6L12.5 3M15.5 6L12.5 9M19 10.5V15C19 16.6569 17.6569 18 16 18L8.5 18M8.5 18L11.5 21M8.5 18L11.5 15"
                      stroke="#9ca3af"
                      strokeWidth="1"
                      strokeLinecap="round"
                      strokeLinejoin="round"
                    />
                  </svg>
                  <span className="text-sm font-semibold">{repostedByUsername} </span>
                  {caption ? (
                    <>
                      <span className="text-sm">reposted:</span>
                      <span className="text-sm italic">"{caption}"</span>
                    </>
                  ) : (
                    <span className="text-sm">reposted</span>
                  )}
                </div>
                <Post
                  key={repostedPost.id}
                  {...repostedPost}
                />
              </div>
            )
              ;
          }
        })}
    </div>
  );
};
export default RepostsOnWall;
