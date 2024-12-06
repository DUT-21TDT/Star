import { ConfigProvider } from "antd";
import HeaderDetailPost from "./HeaderDetailPost";
import { newFeedsTheme } from "../../utils/theme";
import MainContentDetailPost from "../../components/user/profile/posts/detail-post";
import { Helmet } from "react-helmet-async";
import { useParams } from "react-router-dom";
import { useGetPostDetailById } from "../../hooks/post.ts";
import _ from "lodash";

const DetailPost = () => {
  const postId = useParams<{ postId: string }>().postId || "";
  const {
    data: dataPostDetail,
    isLoading,
    isError,
  } = useGetPostDetailById(postId);

  return (
    <>
      <Helmet>
        <title>
          {dataPostDetail.content
            ? _.truncate(dataPostDetail.content, {
                length: 100,
                omission: "...",
              }) + " • Star"
            : "Star"}
        </title>
      </Helmet>
      <ConfigProvider theme={newFeedsTheme}>
        <div className="w-full flex justify-center">
          <div
            className="h-full pt-2"
            style={{
              width: "100%",
              maxWidth: "650px",
            }}
          >
            <HeaderDetailPost />
            <div
              style={{
                border: "1px solid #bdbdbd",
                marginTop: "20px",
                borderRadius: "30px",
                padding: "20px 0px 20px 0px",
                backgroundColor: "white",
                minHeight: "800px",
              }}
            >
              <MainContentDetailPost
                dataPostDetail={dataPostDetail}
                isLoading={isLoading}
                isError={isError}
              />
            </div>
          </div>
        </div>
      </ConfigProvider>
    </>
  );
};
export default DetailPost;
