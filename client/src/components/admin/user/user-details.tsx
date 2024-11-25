import React from "react";
import { useGetUserDetails } from "../../../hooks/user";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useParams } from "react-router-dom";

const UserDetails: React.FC = () => {
  const { UserId } = useParams<{ UserId: string }>();
  const { data, isLoading, isError } = useGetUserDetails(UserId!);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center mt-8">
        <Spin indicator={<LoadingOutlined spin />} size="large" />
      </div>
    );
  }

  if (isError) {
    return (
      <div className="text-center mt-8 text-red-500">
        Something went wrong. Please try again later.
      </div>
    );
  }

  if (!data) {
    return (
      <div className="text-center mt-8 text-gray-500">
        No user details available.
      </div>
    );
  }

  const { name, description, participantsCount = 0, userCount = 0 } = data;

  return (
    <div className="mx-auto p-6 bg-white rounded-xl shadow-lg max-w-2xl mt-8">
      <h1 className="text-2xl font-semibold mb-4">{name}</h1>
      <p className="text-gray-600 mb-4">{description}</p>
      <div className="flex justify-between text-sm text-gray-500">
        <span>Participants: {participantsCount}</span>
        <span>Users: {userCount}</span>
      </div>
    </div>
  );
};

export default UserDetails;
