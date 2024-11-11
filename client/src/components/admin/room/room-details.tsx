import React, {useEffect, useState} from "react";
import { FiEdit2, FiTrash2 } from "react-icons/fi";
import {
    useEditRoom,
    useGetModerators,
    useGetRoomDetails,
    useRemoveModerator
} from "../../../hooks/room.ts";
import {message, Popconfirm, Spin} from "antd";
import {LoadingOutlined} from "@ant-design/icons";
import {useParams} from "react-router-dom";
import {AxiosError} from "axios";
import SearchableDropdown from "./SearchableDropdown.tsx";

interface RoomDetails {
    id: string
    name: string;
    description: string;
    participantsCount: number;
    postsCount: number;
}

interface Member {
    userId: string;
    username: string;
    avatarUrl: string;
    firstName: string;
    lastName: string;
}

interface EditRoomForm {
    name: string;
    description: string;
}

interface ErrorMessage {
    name?: string;
    description?: string;
}

const RoomDetails: React.FC = () => {
    const { roomId } = useParams<{ roomId: string }>();
    const { data, isLoading, isError } = useGetRoomDetails(roomId!);
    const { data: modsData, isLoading: modsLoading, isError: modsError, refetch: refetchMods } = useGetModerators(roomId!);

    const [isEditing, setIsEditing] = useState(false);

    const [roomDetails, setRoomDetails] = useState<RoomDetails>(data);
    const [mods, setMods] = useState<Member[]>(modsData);

    const [editForm, setEditForm] = useState<EditRoomForm>({
        name: "",
        description: ""
    });

    const [errors, setErrors] = useState<ErrorMessage>({});

    const { mutate: editRoom } = useEditRoom();
    const { mutate: removeModerator } = useRemoveModerator();

    useEffect(() => {
        if (roomId && !isLoading && data) {
            setRoomDetails(data);
            setEditForm({
                name: data.name,
                description: data.description
            });
        }
    }, [data, isLoading, roomId]);

    useEffect(() => {
        if (roomId && !modsLoading && modsData) {
            setMods(modsData);
        }
    }, [modsData, modsLoading, roomId]);

    const validateForm = () => {
        const newErrors: ErrorMessage = {};
        if (editForm.name.length < 3) {
            newErrors.name = "Group name must be at least 3 characters long";
        }
        if (editForm.description.length < 10) {
            newErrors.description = "Description must be at least 10 characters long";
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSave = () => {
        if (validateForm()) {

            const editRoomData = {
                id: roomDetails.id,
                name: editForm.name,
                description: editForm.description
            }

            editRoom(editRoomData, {
                onSuccess: () => {
                    setRoomDetails(prev => ({
                        ...prev,
                        name: editForm.name,
                        description: editForm.description
                    }) as RoomDetails);
                    setIsEditing(false);
                },
                onError: (error: Error) => {
                    const axiosError = error as AxiosError;
                    const errorMessage = axiosError?.response?.status === 409 ?
                      `Room "${editRoomData.name}" already exists` : "Something went wrong";
                    setErrors({
                        name: errorMessage
                    })
                }
              }
            )
        }
    };

    const handleRemoveModerator = (userId: string) => {
        removeModerator({ roomId: roomDetails.id, userId }, {
            onSuccess: () => {
                refetchMods();
                message.success("Moderator removed successfully");
            },
            onError: (error: Error) => {
                const axiosError = error as AxiosError;
                const statusCode = axiosError?.response?.status;

                if (statusCode === 404) {
                    message.error("User cannot be found in this room");
                } else if (statusCode === 409) {
                    message.error("User is not a moderator of this room");
                } else {
                    message.error("Something went wrong");
                }
            }
        });
    };

    return (
    <>
        {isLoading ? (
            <div className="flex items-center justify-center mt-8">
                <Spin indicator={<LoadingOutlined spin />} size="large" />
            </div>
        ) : isError ? (
            <div>Something went wrongs</div>
        ) : roomDetails ? (
            <div className="mx-auto p-6 bg-white rounded-xl shadow-lg">
            <div className="mb-8">
                {!isEditing ? (
                    <div className="space-y-4">
                        <div className="flex justify-between items-center">
                            <h1 className="text-3xl font-bold text-gray-800">{roomDetails.name}</h1>
                            <button
                                onClick={() => setIsEditing(true)}
                                className="p-2 text-blue-600 hover:bg-blue-50 rounded-full transition-colors"
                                aria-label="Edit group information"
                            >
                                <FiEdit2 className="w-5 h-5" />
                            </button>
                        </div>
                        <p className="text-gray-600">{roomDetails.description}</p>
                    </div>
                ) : (
                    <div className="space-y-4">
                        <div className="space-y-2">
                            <label className="block text-sm font-medium text-gray-700">Group Name</label>
                            <input
                                type="text"
                                value={editForm.name}
                                onChange={(e) => setEditForm(prev => ({ ...prev, name: e.target.value }))}
                                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                maxLength={50}
                            />
                            {errors.name && <p className="text-red-500 text-sm">{errors.name}</p>}
                        </div>
                        <div className="space-y-2">
                            <label className="block text-sm font-medium text-gray-700">Description</label>
                            <textarea
                                value={editForm.description}
                                onChange={(e) => setEditForm(prev => ({ ...prev, description: e.target.value }))}
                                className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                                rows={3}
                                maxLength={200}
                            />
                            {errors.description && <p className="text-red-500 text-sm">{errors.description}</p>}
                        </div>
                        <div className="flex space-x-2">
                            <button
                                onClick={handleSave}
                                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                            >
                                Save Changes
                            </button>
                            <button
                                onClick={() => setIsEditing(false)}
                                className="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors"
                            >
                                Cancel
                            </button>
                        </div>
                    </div>
                )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                <div className="p-4 bg-gray-50 rounded-lg">
                    <h2 className="text-xl font-semibold text-gray-800 mb-2">Members</h2>
                    <p className="text-3xl font-bold text-blue-600">{roomDetails.participantsCount}</p>
                </div>
                <div className="p-4 bg-gray-50 rounded-lg">
                    <h2 className="text-xl font-semibold text-gray-800 mb-2">Posts</h2>
                    <p className="text-3xl font-bold text-blue-600">{roomDetails.postsCount}</p>
                </div>
            </div>

            <div className="space-y-6">
                <h2 className="text-2xl font-bold text-gray-800">Moderators</h2>
                <>
                    { modsLoading ? (
                        <div className="flex items-center justify-center mt-8">
                            <Spin indicator={<LoadingOutlined spin />} size="large" />
                        </div>
                    ) : modsError ? (
                        <div>Something went wrongs</div>
                    ) : mods ?
                      <>
                        { mods.length === 0 ? (
                            <div className="space-y-4 px-2 text-gray-600">No moderators</div>
                        ) : (
                        <div className="space-y-4">
                            {mods.map((mod) => (
                                <div key={mod.userId} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                                    <div className="flex items-center space-x-4">
                                        <img
                                            src={`${mod.avatarUrl}`}
                                            alt={mod.username}
                                            className="w-10 h-10 rounded-full object-cover"
                                        />
                                        <div>
                                            <p className="font-medium text-gray-800">
                                                {mod.firstName && mod.lastName ? `${mod.firstName} ${mod.lastName}` :
                                                mod.firstName || mod.lastName ? mod.firstName || mod.lastName : mod.username}
                                            </p>
                                            <p className="text-sm text-gray-500">@{mod.username}</p>
                                        </div>
                                    </div>
                                    <Popconfirm
                                      title="Remove moderator"
                                      description={`Are you sure to remove this moderator?`}
                                      okText={`Yes`}
                                      okType={`primary`}
                                      icon={null}
                                      onConfirm={() => handleRemoveModerator(mod.userId)}
                                      cancelText="No"
                                      placement="topRight"
                                    >
                                        <button
                                            className="p-2 text-red-600 hover:bg-red-50 rounded-full transition-colors"
                                            aria-label={`Remove ${mod.username} as moderator`}
                                        >
                                            <FiTrash2 className="w-5 h-5" />
                                        </button>
                                    </Popconfirm>
                                </div>
                            ))}
                        </div>
                        )}
                      </> : (
                        <div className="space-y-4 px-2 text-gray-600">Moderators not found</div>
                      )
                    }
                </>
                <div className="mt-4">
                    <h3 className="text-lg font-semibold text-gray-800 mb-2">Add New Moderator</h3>
                    <SearchableDropdown roomId={roomDetails.id}
                                        mods={mods}
                                        refetchMods={refetchMods}
                    />
                    <div className="flex space-x-2">
                    {/*    <input*/}
                    {/*        type="text"*/}
                    {/*        value={newModUsername}*/}
                    {/*        onChange={(e) => setNewModUsername(e.target.value)}*/}
                    {/*        placeholder="Enter username"*/}
                    {/*        className="flex-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"*/}
                    {/*    />*/}
                    {/*    <button*/}
                    {/*        onClick={handleAddModerator}*/}
                    {/*        className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"*/}
                    {/*        disabled={newModUsername.length < 3}*/}
                    {/*    >*/}
                    {/*        <FiPlus className="w-5 h-5" />*/}
                    {/*    </button>*/}
                    </div>
                </div>
            </div>
        </div>
        ) : (
            <div>Room not found</div>
        )}
    </>
    );
};

export default RoomDetails;