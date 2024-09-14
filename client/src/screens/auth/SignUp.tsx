import React from "react";
import imageSignup from "../../assets/images/login.webp";
import type { FormInstance } from "antd";
import { Button, Divider, Form, Input, QRCode, message } from "antd";
import { usePostNewUser } from "../../hooks/user";
import { ConfigProvider } from "antd";
import { SignUpTheme } from "../../utils/theme";
import { Link } from "react-router-dom";
import GoogleLoginButton from "../../components/user/GoogleLoginButton";
import { AxiosError } from "axios";
interface SubmitButtonProps {
  form: FormInstance;
}

interface IForm {
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
}

const SubmitButton: React.FC<React.PropsWithChildren<SubmitButtonProps>> = ({
  form,
  children,
}) => {
  const [submittable, setSubmittable] = React.useState<boolean>(false);

  // Watch all values
  const values = Form.useWatch([], form);

  React.useEffect(() => {
    form
      .validateFields({ validateOnly: true })
      .then(() => setSubmittable(true))
      .catch(() => setSubmittable(false));
  }, [form, values]);

  return (
    <Button
      type="primary"
      disabled={!submittable}
      style={{
        backgroundColor: "black",
        width: "100%",
        height: "50px",
        borderRadius: "10px",
        fontSize: "16px",
        fontWeight: "500",
        color: `${submittable ? "white" : "gray"}`,
      }}
      onClick={() => form.submit()}
    >
      {children}
    </Button>
  );
};
const SignUp: React.FC = () => {
  const [form] = Form.useForm();
  const { mutate: postNewUser } = usePostNewUser();
  const validateForm = (
    form: IForm
  ): {
    isValid: boolean;
    errorContent: string;
  } => {
    // Email validation
    const emailRegex = /^[\w.-]+@([\w-]+\.)+[\w-]{2,4}$/;
    if (!emailRegex.test(form.email)) {
      return {
        isValid: false,
        errorContent: `Invalid email address. Please check and enter a 
          valid email format (e.g., example@domain.com).`,
      };
    }

    // Username validation
    const usernameRegex = /^[a-zA-Z_](?!.*?\.{2})[\w.]{4,28}[\w]$/;
    if (!usernameRegex.test(form.username)) {
      return {
        isValid: false,
        errorContent:
          "Username must be 6-30 characters long. Start and end with a letter or number.",
      };
    }

    // Password validation
    const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/;
    if (!passwordRegex.test(form.password)) {
      return {
        isValid: false,
        errorContent:
          "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, and one digit.",
      };
    }

    // Confirm password validation
    if (form.password !== form.confirmPassword) {
      return {
        isValid: false,
        errorContent: "Passwords do not match",
      };
    }
    return {
      isValid: true,
      errorContent: "",
    };
  };

  const handleSubmitFormSignUp = (values: IForm) => {
    const { isValid, errorContent } = validateForm(values);
    if (!isValid) {
      message.error({
        content: errorContent,
        duration: 4,
      });
    } else {
      postNewUser(values, {
        onSuccess: () => {
          message.success({
            content:
              "Your account has been created. Please check your email to confirm your registration.",
            duration: 4,
          });
        },
        onError: (error: Error) => {
          const axiosError = error as AxiosError;
          message.error({
            content: `${axiosError?.response?.data}`,
          });
        },
      });
    }
  };

  return (
    <ConfigProvider theme={SignUpTheme}>
      <div className="h-screen w-full relative bg-white">
        <div
          className="h-[45%] bg-cover bg-center"
          style={{ backgroundImage: `url(${imageSignup})` }}
        ></div>
        <div className="w-[450px] h-auto border border-black/15 absolute left-1/2 top-[60%] -translate-x-1/2 -translate-y-1/2 p-5 rounded-[20px] bg-white">
          <div className="text-center">
            <h1 className="text-[19px] font-bold mb-3">Welcome to Star</h1>
          </div>
          <div>
            <Form
              form={form}
              name="validateOnly"
              layout="vertical"
              autoComplete="off"
              onFinish={handleSubmitFormSignUp}
            >
              <Form.Item
                name="email"
                rules={[
                  { required: true, message: "Please input your email!" },
                ]}
              >
                <Input
                  style={{
                    backgroundColor: "#f5f5f5",
                    height: "50px",
                    borderRadius: "10px",
                    border: "none",
                    fontSize: "14px",
                    fontWeight: "500",
                  }}
                  placeholder="Email"
                />
              </Form.Item>

              <Form.Item
                name="username"
                rules={[
                  { required: true, message: "Please input your username!" },
                ]}
              >
                <Input
                  style={{
                    backgroundColor: "#f5f5f5",
                    height: "50px",
                    borderRadius: "10px",
                    border: "none",
                    fontSize: "14px",
                    fontWeight: "500",
                  }}
                  placeholder="Username"
                />
              </Form.Item>
              <Form.Item
                name="password"
                rules={[
                  { required: true, message: "Please input your password!" },
                ]}
              >
                <Input.Password
                  style={{
                    backgroundColor: "#f5f5f5",
                    height: "50px",
                    borderRadius: "10px",
                    border: "none",
                    fontSize: "14px",
                    fontWeight: "500",
                  }}
                  placeholder="Password"
                />
              </Form.Item>
              <Form.Item
                name="confirmPassword"
                rules={[
                  {
                    required: true,
                    message: "Please input your confirm password!",
                  },
                ]}
              >
                <Input.Password
                  style={{
                    backgroundColor: "#f5f5f5",
                    height: "50px",
                    borderRadius: "10px",
                    border: "none",
                    fontSize: "14px",
                    fontWeight: "500",
                  }}
                  placeholder="Confirm Password"
                />
              </Form.Item>
              <Form.Item>
                <SubmitButton form={form}>Sign up</SubmitButton>
              </Form.Item>
            </Form>
          </div>
          <div>
            <p className="text-center">
              Already have an account?{" "}
              <Link to="/login" className="text-[#1890ff]">
                Login
              </Link>
            </p>
          </div>
          <Divider style={{ color: "#bdbdbd" }}>or</Divider>
          <GoogleLoginButton />
        </div>
        <div className="w-[250px] h-[250px] absolute right-0 bottom-0 p-5 flex flex-col items-center justify-center gap-2">
          <div className="text-[13px] text-gray-400 font-normal">
            Scan to get the app
          </div>

          <QRCode type="canvas" value="https://ant.design/" />
        </div>
      </div>
    </ConfigProvider>
  );
};

export default SignUp;
