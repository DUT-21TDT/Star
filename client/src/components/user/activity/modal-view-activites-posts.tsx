import { Avatar, Modal, Popover, Spin } from "antd";
import default_image from "../../../assets/images/default_image.jpg";
import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import ContainerInformationUser from "../profile/posts/container-information-user";
import { timeAgo } from "../../../utils/convertTime";
import DOMPurify from "dompurify";
import ActivityItemOnPostDetail from "./activity-item-on-detail-post";
import { useGetAllActivitiesOnDetailPost } from "../../../hooks/notification";
import { debounce } from "lodash";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import {
  HeartIconActivity,
  RepostIcon,
  ViewsIcon,
} from "../../../assets/icon/sidebar-homepage-icon";
import { LoadingOutlined } from "@ant-design/icons";

type DataDetailPost = {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string;
  createdAt: string;
  content: string;
  postImageUrls: string[];
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  reposted: boolean;
  nameOfRoom: string;
  idOfCreator: string;
};

interface IActivityItemPostDetail {
  userId: string;
  username: string;
  avatarUrl: string;
  firstName: string;
  lastName: string;
  interactType: string;
  interactAt: string;
  followStatus: string;
}

import React, { ReactNode } from "react";
import { useAppSelector } from "../../../redux/store/hook";
import ModalViewPeopleLikePost from "./modal-people-liked-posts";
import ModalViewPeopleRepostPost from "./modal-people-reposted-posts";

interface StatCardProps {
  icon: ReactNode;
  label: string;
  count: number;
  setIsOpenModalPeopleLikedPost?: (isOpen: boolean) => void;
  setIsOpenModalPeopleRepostedPost?: (isOpen: boolean) => void;
  setIsOpenMainModal?: (isOpen: boolean) => void;
}

const StatCard: React.FC<StatCardProps> = ({
  icon,
  label,
  count,
  setIsOpenModalPeopleLikedPost,
  setIsOpenModalPeopleRepostedPost,
  setIsOpenMainModal,
}) => {
  const handleOpenModal = () => {
    if (
      label === "Likes" &&
      setIsOpenModalPeopleLikedPost &&
      setIsOpenMainModal
    ) {
      setIsOpenModalPeopleLikedPost(true);
      setIsOpenMainModal(false);
    } else if (
      label === "Reports" &&
      setIsOpenModalPeopleRepostedPost &&
      setIsOpenMainModal
    ) {
      setIsOpenModalPeopleRepostedPost(true);
      setIsOpenMainModal(false);
    }
  };
  return (
    <div
      className="w-full flex items-start gap-3 my-3"
      style={{ paddingBottom: "5px" }}
    >
      <div
        style={{
          marginLeft: "20px",
          width: "40px",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        {icon}
      </div>
      <div
        className="flex items-center justify-between w-full pb-5 pr-3"
        style={{
          paddingBottom: "10px",
          borderBottom: "1px solid rgb(240,240,240)",
          width: "calc(100% - 50px)",
          cursor: "pointer",
        }}
        onClick={handleOpenModal}
      >
        <div
          style={{
            fontSize: "16px",
            color: "black",
            fontWeight: 500,
          }}
        >
          {label}
        </div>
        <div
          style={{
            fontSize: "16px",
            color: "black",
            fontWeight: 500,
            display: "flex",
            alignItems: "center",
            gap: "8px",
          }}
        >
          {count}{" "}
          {label !== "Views" ? (
            <span>
              <svg
                aria-label="Next"
                role="img"
                viewBox="0 0 24 24"
                height={16}
                width={16}
                fill="none"
                stroke="#000000"
              >
                <title>Next</title>
                <polyline points="7.498 3 16.502 12 7.498 21"></polyline>
              </svg>
            </span>
          ) : (
            ""
          )}
        </div>
      </div>
    </div>
  );
};

const ModalViewActiviesPost = ({
  isOpenModal,
  setIsOpenModal,
  dataDetailPost,
}: {
  isOpenModal: boolean;
  setIsOpenModal: (isOpen: boolean) => void;
  dataDetailPost: DataDetailPost;
}) => {
  const {
    id,
    idOfCreator,
    usernameOfCreator,
    avatarUrlOfCreator,
    createdAt,
    content,
  } = dataDetailPost;
  const navigate = useNavigate();
  const handleNavigateProfileUser = (id: string) => () => {
    if (id) {
      navigate(`/profile/${id}`);
      window.scrollTo(0, 0);
    }
  };
  const [isPopoverVisibleUsername, setIsPopoverVisibleUsername] =
    useState(false);
  const [popoverContent, setPopoverContent] = useState<React.ReactNode>(
    <div></div>
  );
  const handlePopoverUsernameVisibilityChange = (visible: boolean) => {
    setIsPopoverVisibleUsername(visible);
    if (visible) {
      setPopoverContent(
        <ContainerInformationUser idOfCreator={idOfCreator || ""} />
      );
    }
  };

  const sanitizedContent = DOMPurify.sanitize(
    content
      .replace(
        /(https?:\/\/\S+)/g,
        '<a href="$1" target="_blank" rel="noopener noreferrer" class="text-[#1B75D0] hover:text-[#165ca3]">$1</a>'
      )
      .replace(/\n/g, "<br />"),
    {
      ADD_ATTR: ["target"],
      FORBID_TAGS: ["style"],
    }
  );

  const [afterTime, setAfterTime] = useState<string | null>(null);
  const [allActivitiesOnDetailPost, setAllActivitiesOnDetailPost] = useState<
    IActivityItemPostDetail[]
  >([]);
  const [isDataFetched, setIsDataFetched] = useState(false);
  const {
    dataNotification,
    hasNextNotification,
    isLoading,
    afterTimeFinalNotification,
    viewsCount: initialViewsCount,
    likesCount: initialLikesCount,
    repostsCount: initialRepostsCount,
  } = useGetAllActivitiesOnDetailPost(
    id,
    {
      limit: 15,
      after: afterTime,
    },
    isDataFetched
  );

  useEffect(() => {
    if (isOpenModal && !isDataFetched) {
      setIsDataFetched(true);
    }
  }, [isOpenModal]);

  const [viewsCount, setViewsCount] = useState<number>(0);
  const [likesCount, setLikesCount] = useState<number>(0);
  const [repostsCount, setRepostsCount] = useState<number>(0);

  const cachedCounts = useRef({
    viewsCount: 0,
    likesCount: 0,
    repostsCount: 0,
  });

  useEffect(() => {
    if (afterTime === null) {
      cachedCounts.current = {
        viewsCount: initialViewsCount ?? 0,
        likesCount: initialLikesCount ?? 0,
        repostsCount: initialRepostsCount ?? 0,
      };

      setViewsCount(initialViewsCount ?? 0);
      setLikesCount(initialLikesCount ?? 0);
      setRepostsCount(initialRepostsCount ?? 0);
    }
  }, [afterTime, initialViewsCount, initialLikesCount, initialRepostsCount]);

  useEffect(() => {
    if (afterTime !== null) {
      setViewsCount(cachedCounts.current.viewsCount);
      setLikesCount(cachedCounts.current.likesCount);
      setRepostsCount(cachedCounts.current.repostsCount);
    }
  }, [afterTime]);

  const divRef = useRef(null);
  useEffect(() => {
    if (dataNotification && dataNotification.length > 0) {
      setAllActivitiesOnDetailPost((prevPosts: IActivityItemPostDetail[]) => [
        ...prevPosts,
        ...dataNotification,
      ]);
    }
  }, [dataNotification]);

  const handleScroll = debounce(() => {
    if (divRef.current) {
      const { scrollTop, scrollHeight, clientHeight } = divRef.current;
      const isBottom = scrollTop + clientHeight >= scrollHeight - 2;
      if (isBottom && hasNextNotification) {
        setAfterTime(afterTimeFinalNotification);
      }
    }
  }, 300);

  const queryClient = useQueryClient();
  const isCurrentUser =
    useAppSelector((state) => state.user.id) === idOfCreator;

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: [QUERY_KEY.fetchAllActivitiesOnPostDetail(id)],
      });
    };
  }, []);

  // state filter people liked post
  const [isOpenModalPeopleLikedPost, setIsOpenModalPeopleLikedPost] =
    useState(false);
  const [isOpenModalPeopleRepostedPost, setIsOpenModalPeopleRepostedPost] =
    useState(false);

  return (
    <>
      <Modal
        title={
          <p className="text-center text-[18px] pt-3 font-semibold">
            Post activity
          </p>
        }
        open={isOpenModal}
        onCancel={() => setIsOpenModal(false)}
        centered
        width={650}
        footer={null}
        closeIcon={null}
      >
        <div
          style={{
            backgroundColor: "white",
            overflowY: "auto",
            maxHeight: "calc(100vh - 200px)",
          }}
          className="custom-scrollbar"
          onScroll={handleScroll}
          ref={divRef}
        >
          <div
            className="w-full h-[90px] mt-5"
            style={{
              border: "1px solid #bdbdbd",
              borderRadius: "10px",
            }}
          >
            <div
              className="p-3 hover:cursor-pointer"
              style={{
                display: "flex",
                gap: "10px",
              }}
            >
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  minHeight: "80px",
                }}
              >
                <Avatar
                  style={{
                    width: "45px",
                    height: "45px",
                    cursor: "pointer",
                    flexShrink: 0,
                  }}
                  src={avatarUrlOfCreator || default_image}
                  onClick={handleNavigateProfileUser(idOfCreator || "")}
                />
              </div>

              <div style={{ width: "calc(100% - 65px)" }}>
                <div className="flex justify-between w-full">
                  <Popover
                    content={popoverContent}
                    placement="bottomLeft"
                    trigger="hover"
                    mouseEnterDelay={0.35}
                    overlayClassName="custom-popover"
                    arrow={false}
                    open={isPopoverVisibleUsername}
                    onOpenChange={handlePopoverUsernameVisibilityChange}
                  >
                    <div
                      style={{
                        fontWeight: "500",
                        fontSize: "16px",
                        cursor: "pointer",
                      }}
                      onClick={handleNavigateProfileUser(idOfCreator || "")}
                    >
                      {usernameOfCreator}{" "}
                      <span
                        style={{
                          color: "rgb(153,153,153)",
                          fontSize: "14px",
                        }}
                      >
                        {timeAgo(createdAt)}
                      </span>
                    </div>
                  </Popover>
                </div>
                <p
                  style={{
                    lineHeight: "22px",
                    fontSize: "15px",
                    textAlign: "left",
                    marginTop: "4px",
                    wordBreak: "break-word",
                    display: "-webkit-box",
                    WebkitLineClamp: 1,
                    WebkitBoxOrient: "vertical",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                  }}
                  dangerouslySetInnerHTML={{ __html: sanitizedContent }}
                ></p>
              </div>
            </div>
          </div>
          <div>
            {isCurrentUser && (
              <StatCard
                icon={<ViewsIcon width="25" height="25" />}
                label="Views"
                count={viewsCount}
              />
            )}
            <StatCard
              icon={<HeartIconActivity width="30" height="30" />}
              label="Likes"
              count={likesCount}
              setIsOpenModalPeopleLikedPost={setIsOpenModalPeopleLikedPost}
              setIsOpenModalPeopleRepostedPost={
                setIsOpenModalPeopleRepostedPost
              }
              setIsOpenMainModal={setIsOpenModal}
            />
            <StatCard
              icon={<RepostIcon width="30" height="30" />}
              label="Reports"
              count={repostsCount}
              setIsOpenModalPeopleLikedPost={setIsOpenModalPeopleLikedPost}
              setIsOpenModalPeopleRepostedPost={
                setIsOpenModalPeopleRepostedPost
              }
              setIsOpenMainModal={setIsOpenModal}
            />
          </div>
          {allActivitiesOnDetailPost &&
            allActivitiesOnDetailPost.length === 0 &&
            !isLoading && (
              <div
                style={{
                  marginTop: "20px",
                  borderRadius: "30px",
                  padding: "20px 0px 20px 0px",
                  backgroundColor: "white",
                  overflowY: "auto",
                  height: "25vh",
                  fontWeight: 450,
                  color: "#999999",
                  fontSize: "1.2rem",
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                No activities yet
              </div>
            )}
          {allActivitiesOnDetailPost &&
            allActivitiesOnDetailPost.length === 0 &&
            isLoading && (
              <div className="flex items-center justify-center mt-8">
                <Spin indicator={<LoadingOutlined spin />} size="large" />
              </div>
            )}
          <div>
            {allActivitiesOnDetailPost &&
              allActivitiesOnDetailPost.length > 0 &&
              allActivitiesOnDetailPost.map(
                (notification: IActivityItemPostDetail, index: number) => {
                  return (
                    <ActivityItemOnPostDetail
                      key={index}
                      data={notification}
                      setAllActivitiesOnDetailPost={
                        setAllActivitiesOnDetailPost
                      }
                    />
                  );
                }
              )}
          </div>
        </div>
      </Modal>

      <ModalViewPeopleLikePost
        isOpenModal={isOpenModalPeopleLikedPost}
        setIsOpenModal={setIsOpenModalPeopleLikedPost}
        dataDetailPost={dataDetailPost}
        setIsOpenMainModal={setIsOpenModal}
      />

      <ModalViewPeopleRepostPost
        isOpenModal={isOpenModalPeopleRepostedPost}
        setIsOpenModal={setIsOpenModalPeopleRepostedPost}
        dataDetailPost={dataDetailPost}
        setIsOpenMainModal={setIsOpenModal}
      />
    </>
  );
};
export default ModalViewActiviesPost;
