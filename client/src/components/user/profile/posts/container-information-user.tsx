import { Image } from "antd";
import { useGetProfileUser } from "../../../../hooks/user";
import default_image from "../../../../assets/images/default_image.jpg";
import { useNavigate } from "react-router-dom";
interface ContainerInformationUserProps {
  idOfCreator: string;
}

const ContainerInformationUser: React.FC<ContainerInformationUserProps> = (
  props
) => {
  const { idOfCreator } = props;
  const { data } = useGetProfileUser(idOfCreator || "");
  const navigate = useNavigate();
  return (
    <>
      <div
        className="w-[300px] h-[full]"
        style={{
          padding: "15px 10px",
        }}
        onClick={() => navigate(`/profile/${idOfCreator}`)}
      >
        <div className="flex justify-between items-center cursor-pointer">
          <div>
            {data?.publicProfile?.firstName || data?.publicProfile?.lastName ? (
              <div className="text-[18px] font-semibold">
                {`${data?.publicProfile?.firstName || ""} ${
                  data?.publicProfile?.lastName || ""
                }`}
              </div>
            ) : (
              <div className="text-[18px] font-semibold">
                {data?.publicProfile?.username}
              </div>
            )}
            <div className="text-[15px]"> {data?.publicProfile?.username}</div>
          </div>
          <div
            className="w-[60px] h-[60px]"
            style={{
              borderRadius: "50%",
              position: "relative",
            }}
          >
            <Image
              src={`${data?.publicProfile?.avatarUrl || default_image}`}
              width={60}
              height={60}
              style={{
                borderRadius: "50%",
              }}
              id="avatar-profile"
            />
          </div>
        </div>
        <div className="text-[15px] font-normal mt-3 text-left">
          {data?.publicProfile?.bio || ""}
        </div>
        <div className="text-[#a1a1a1] font-normal text-[15px]">
          {data?.publicProfile?.numberOfFollowers || 0} followers
        </div>
      </div>
      {!data?.isCurrentUser && (
        <button className="font-semibold w-full h-[40px] text-[15px] border rounded-[10px] bg-[black] text-[white]">
          {data?.isFollowing ? "Unfollow" : "Follow"}
        </button>
      )}
    </>
  );
};
export default ContainerInformationUser;
