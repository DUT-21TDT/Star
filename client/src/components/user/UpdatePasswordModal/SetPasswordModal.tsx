import React from "react";
import { Modal, Form, Input, Button } from "antd";

interface SetPasswordModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (newPassword: string, confirmPassword: string) => void;
}

const SetPasswordModal: React.FC<SetPasswordModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
}) => {
  const [form] = Form.useForm();

  const handleFinish = (values: {
    newPassword: string;
    confirmPassword: string;
  }) => {
    const { newPassword, confirmPassword } = values;
    onSubmit(newPassword, confirmPassword);
    form.resetFields();
  };

  return (
    <Modal
      title={
        <div
          style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            fontWeight: 600,
            fontSize: "24px",
            paddingTop: "4px",
          }}
        >
          Set Password
        </div>
      }
      open={isOpen}
      onCancel={onClose}
      footer={null}
      centered
      bodyStyle={{
        padding: "24px",
        borderRadius: "12px",
      }}
      style={{
        backgroundColor: "#000000",
      }}
    >
      <Form
        form={form}
        layout="vertical"
        onFinish={handleFinish}
        initialValues={{
          newPassword: "",
          confirmPassword: "",
        }}
      >
        <Form.Item
          label="New Password"
          name="newPassword"
          rules={[
            { required: true, message: "Please enter your new password!" },
          ]}
        >
          <Input.Password placeholder="Enter new password" />
        </Form.Item>

        <Form.Item
          label="Confirm Password"
          name="confirmPassword"
          dependencies={["newPassword"]}
          rules={[
            { required: true, message: "Please confirm your new password!" },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("newPassword") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error("The two passwords do not match!")
                );
              },
            }),
          ]}
        >
          <Input.Password placeholder="Confirm new password" />
        </Form.Item>

        <Form.Item>
          <div
            style={{
              display: "flex",
              justifyContent: "flex-end",
              gap: "10px",
            }}
          >
            <Button onClick={onClose}>Cancel</Button>
            <Button
              type="primary"
              htmlType="submit"
              style={{
                backgroundColor: "#000000", // Black background
                color: "#ffffff", // White text
                border: "1px solid #333333", // Subtle border for contrast
              }}
            >
              Confirm
            </Button>
          </div>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default SetPasswordModal;
