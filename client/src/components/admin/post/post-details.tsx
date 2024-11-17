import React from "react";
import { useGetPostDetails } from "../../../hooks/post";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useParams } from "react-router-dom";

const PostDetails: React.FC = () => {
    const { postId } = useParams<{ postId: string }>();
    const { data, isLoading, isError } = useGetPostDetails(postId!);

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
                No post details available.
            </div>
        );
    }

    const { name, description, participantsCount, postsCount } = data;

    return (
        <div className="mx-auto p-6 bg-white rounded-xl shadow-lg max-w-2xl mt-8">
            <h1 className="text-2xl font-semibold mb-4">{name}</h1>
            <p className="text-gray-600 mb-4">{description}</p>
            <div className="flex justify-between text-sm text-gray-500">
                <span>Participants: {participantsCount}</span>
                <span>Posts: {postsCount}</span>
            </div>
        </div>
    );
};

export default PostDetails;
