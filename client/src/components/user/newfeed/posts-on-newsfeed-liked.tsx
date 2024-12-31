import React, {useEffect, useRef, useState} from "react";
import {Spin} from "antd";
import {LoadingOutlined} from "@ant-design/icons";
import {useQueryClient} from "@tanstack/react-query";
import {QUERY_KEY} from "../../../utils/queriesKey";
import {useFetchAllPostsLikedOnNewsfeed} from "../../../hooks/post";
import Post from "../profile/posts/Post";
import RemoveDuplicatePost from "../../../utils/removeDuplicatePost";
import CreatePost from "../profile/posts/create-post";
import "../../../assets/css/newfeed.css";
import {debounce} from "../../../utils/debounce";

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

const PostsFollowingOnNewsFeed: React.FC = () => {
  const queryClient = useQueryClient();
  const divRef = useRef(null);
  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allPosts, setAllPosts] = useState<PostType[]>([]);
  const { dataPost, isLoading, hasNextPost } =
    useFetchAllPostsLikedOnNewsfeed({
      limit: 10,
      after: afterTime,
    });

  // const handleScroll = () => {
  //   const isBottom =
  //     window.innerHeight + window.scrollY >=
  //     document.documentElement.scrollHeight - 1;

  //   if (isBottom && hasNextPost) {
  //     queryClient.invalidateQueries({
  //       queryKey: QUERY_KEY.fetchAllPostsFollowingOnNewsFeed(),
  //     });
  //   }
  // };

  const handleScroll = debounce(() => {
    if (divRef.current) {
      const { scrollTop, scrollHeight, clientHeight } = divRef.current;
      const isBottom = scrollTop + clientHeight >= scrollHeight - 2;
      if (isBottom && hasNextPost) {
        queryClient.invalidateQueries({
          queryKey: QUERY_KEY.fetchAllPostsFollowingOnNewsFeed(),
        });
      }
    }
  }, 300);
  useEffect(() => {
    if (dataPost && dataPost.length > 0) {
      setAllPosts((prevPosts: PostType[]) =>
        RemoveDuplicatePost([
          ...prevPosts,
          ...dataPost.map((post: PostType) => ({
            ...post,
            postImageUrls: post.postImageUrls || [],
            isRemoved: false,
          })),
        ])
      );
      const lastPost = dataPost[dataPost.length - 1];
      setAfterTime(lastPost.createdAt);
    }
  }, [dataPost]);

  // useEffect(() => {
  //   window.addEventListener("scroll", handleScroll);
  //   return () => window.removeEventListener("scroll", handleScroll);
  // }, [hasNextPost]);

  const handleDeletePostSuccess = (postId: string) => {
    setAllPosts((prevPosts) => {
      return prevPosts.map((post) => {
        if (post.id === postId) {
          return {
            ...post,
            isRemoved: true,
          };
        }
        return post;
      });
    });
  };

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: QUERY_KEY.fetchAllPostsFollowingOnNewsFeed(),
      });
    };
  }, []);

  return (
    <>
      <div
        style={{
          border: "1px solid #ccc",
          marginTop: "20px",
          height: "100%",
          borderBottom: "none",
          borderTopLeftRadius: "30px",
          borderTopRightRadius: "30px",
          paddingTop: "10px",
          backgroundColor: "white",
          paddingRight: "3px",
          maxHeight: "calc(100vh - 60px)",
          overflow: "hidden",
        }}
      >
        <div
          className="custom-scrollbar"
          style={{
            // border: "1px solid #ccc",
            // marginTop: "20px",
            height: "100%",
            // borderBottom: "none",
            // borderTopLeftRadius: "30px",
            // borderTopRightRadius: "30px",
            // paddingTop: "10px",
            overflowY: "auto",
            maxHeight: "calc(100vh - 60px)",
            // backgroundColor: "white",
          }}
          ref={divRef}
          onScroll={handleScroll}
        >
          <CreatePost />
          {isLoading && (
            <div className="flex items-center justify-center mt-8">
              <Spin indicator={<LoadingOutlined spin />} size="large" />
            </div>
          )}
          {allPosts &&
            allPosts.length > 0 &&
            allPosts.map((post) => {
              const {
                id,
                usernameOfCreator,
                avatarUrlOfCreator,
                createdAt,
                content,
                postImageUrls,
                numberOfLikes,
                numberOfComments,
                numberOfReposts,
                liked,
                idOfCreator,
                nameOfRoom,
                isRemoved,
                reposted,
              } = post;
              return (
                <Post
                  key={id}
                  id={id}
                  usernameOfCreator={usernameOfCreator}
                  avatarUrlOfCreator={avatarUrlOfCreator}
                  createdAt={createdAt}
                  content={content}
                  postImageUrls={postImageUrls}
                  numberOfLikes={numberOfLikes}
                  numberOfComments={numberOfComments}
                  numberOfReposts={numberOfReposts}
                  liked={liked}
                  idOfCreator={idOfCreator}
                  nameOfRoom={nameOfRoom}
                  isRemoved={isRemoved}
                  reposted={reposted}
                  handleDeletePostSuccess={handleDeletePostSuccess}
                />
              );
            })}
        </div>
      </div>
    </>
  );
};
export default PostsFollowingOnNewsFeed;
