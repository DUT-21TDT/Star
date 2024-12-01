import {ConfigProvider} from "antd";
import "../../assets/css/profile.css";
import {roomUserTheme} from "../../utils/theme";
import HeaderPeople from "../../components/user/people/header-people";
import MainPeopleContent from "../../components/user/people/main-people";
import {Helmet} from "react-helmet-async";

const PeopleUser = () => {
  return (
    <>
      <Helmet>
        <title>People • Star</title>
      </Helmet>
      <ConfigProvider theme={roomUserTheme}>
        <div className="w-full flex justify-center">
          <div
            className=" h-full pt-2 "
            style={{width: "100%", maxWidth: "650px"}}
          >
            <HeaderPeople/>
            <div
              style={{
                border: "1px solid #bdbdbd",
                marginTop: "20px",
                  borderBottom: "none",
                  borderTopLeftRadius: "30px",
                  borderTopRightRadius: "30px",
                padding: "20px",
                backgroundColor: "white",
              }}
            >
              <MainPeopleContent/>
            </div>
          </div>
        </div>
      </ConfigProvider>
    </>
  );
};
export default PeopleUser;
