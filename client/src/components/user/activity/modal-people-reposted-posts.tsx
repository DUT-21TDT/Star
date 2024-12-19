import { Avatar, Button, Modal, Popover, Spin } from "antd";
import default_image from "../../../assets/images/default_image.jpg";
import { useNavigate } from "react-router-dom";
import { useEffect, useRef, useState } from "react";
import ContainerInformationUser from "../profile/posts/container-information-user";
import { timeAgo } from "../../../utils/convertTime";
import DOMPurify from "dompurify";
import ActivityItemOnPostDetail from "./activity-item-on-detail-post";
import { useGetAllActivitiesRepostOnDetailPost } from "../../../hooks/notification";
import { debounce } from "lodash";
import { useQueryClient } from "@tanstack/react-query";
import { QUERY_KEY } from "../../../utils/queriesKey";
import ArrowLeftOutlined from "@ant-design/icons/ArrowLeftOutlined";
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

import React from "react";

const ModalViewPeopleRepostPost = ({
  isOpenModal,
  setIsOpenModal,
  dataDetailPost,
  setIsOpenMainModal,
}: {
  isOpenModal: boolean;
  setIsOpenModal: (isOpen: boolean) => void;
  dataDetailPost: DataDetailPost;
  setIsOpenMainModal: (isOpen: boolean) => void;
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
    afterTimeFinalNotification,
    repostsCount,
    isLoading,
  } = useGetAllActivitiesRepostOnDetailPost(
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

  useEffect(() => {
    return () => {
      queryClient.resetQueries({
        queryKey: [QUERY_KEY.fetchAllActivitiesRepostOnPostDetail(id)],
      });
    };
  }, []);

  const handleBackButton = () => {
    setIsOpenMainModal(true);
    setIsOpenModal(false);
  };

  return (
    <Modal
      title={
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "50px 1fr",
            alignItems: "center",
            paddingTop: "12px",
          }}
        >
          <Button
            icon={<ArrowLeftOutlined />}
            style={{
              borderRadius: "50%",
              width: "25px",
              height: "25px",
              flexGrow: 1,
            }}
            onClick={handleBackButton}
          />

          <p className="text-center text-[18px] font-semibold">
            {repostsCount} {repostsCount > 1 ? "reposts" : "repost"}
          </p>
        </div>
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
              No one has reposted this post yet
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
                    setAllActivitiesOnDetailPost={setAllActivitiesOnDetailPost}
                  />
                );
              }
            )}
        </div>
      </div>
    </Modal>
  );
};
export default ModalViewPeopleRepostPost;
