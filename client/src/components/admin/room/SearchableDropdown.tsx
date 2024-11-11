import React, { useState, useRef, useEffect } from "react";
import {FiPlus, FiSearch, FiX} from "react-icons/fi";
import { IoMdArrowDropdown } from "react-icons/io";
import {getMembersInRoom} from "../../../service/userAPI.ts";
import {QueryObserverResult, RefetchOptions } from "@tanstack/react-query";
import {addModeratorToRoom} from "../../../service/roomAPI.ts";
import {AxiosError} from "axios";
import {message} from "antd";

interface Member {
  userId: string;
  username: string;
  avatarUrl: string;
  firstName: string;
  lastName: string;
}

interface IProps {
  roomId: string;
  mods: Member[];
  refetchMods: (options?: RefetchOptions | undefined) => Promise<QueryObserverResult<any, Error>>;
}

const SearchableDropdown: React.FC<IProps> = (props: IProps) => {

  const { roomId, mods, refetchMods } = props;

  const [isOpen, setIsOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedUsers, setSelectedUsers] = useState<Member[]>([]);
  const [error, setError] = useState("");
  const dropdownRef = useRef(null);

  const [filteredUsers, setFilteredUsers] = useState<Member[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  useEffect(() => {
    setIsLoading(true);
    // Debounce API call
    const timeoutId = setTimeout(() => {
      if (searchTerm.trim() !== "") {
        // Call the API to get filtered members based on search term
        getMembersInRoom(roomId, searchTerm)
          .then((members) => {

            const notInSelectedUsers = members.filter(
              (member: Member) => !selectedUsers.some((user) => user.userId === member.userId)
            );

            setFilteredUsers(notInSelectedUsers);
            setIsLoading(false);
          })
          .catch((error) => {
            console.error("Error fetching members:", error);
            setFilteredUsers([]);
          });
      } else {
        setFilteredUsers([]);
      }
    }, 300); // Debounce duration: 300ms

    return () => clearTimeout(timeoutId); // Cleanup timeout on unmount or searchTerm change
  }, [searchTerm, roomId, selectedUsers]);

  const handleUserSelect = (user: Member) => {
    setSelectedUsers(prev => [...prev, user]);
    setSearchTerm("");
    setError("");
  };

  const handleUserRemove = (userId: string) => {
    setSelectedUsers(selectedUsers.filter((user) => user.userId !== userId));
  };

  const handleSubmit = async () => {
    if (selectedUsers.length === 0) {
      setError("Please select at least one user as moderator");
      return;
    }

    if (mods.some((mod) => selectedUsers.some((user) => user.userId === mod.userId))) {
      setError("User is already a moderator of this room");
      return;
    }

    try {
      await Promise.all(
        selectedUsers.map((user) => addModeratorToRoom(roomId, user.username))
      );

      await refetchMods();
      setSelectedUsers([]);
      setError("");
    } catch (error) {
      console.error("Error adding moderator:", error);
      const statusCode = (error as AxiosError)?.response?.status;
      if (statusCode === 404) {
        message.error("User is not a member of this room");
      } else if (statusCode === 409) {
        message.error("User is already a moderator of this room");
      } else if (statusCode === 500) {
        message.error("Something went wrong");
      }
    }
  };

  return (
    <div className="mx-auto p-4">
      <div className="mb-4">
        <div ref={dropdownRef} className="relative">
          <div className="flex items-center gap-2 text-sm text-gray-500">
          <div
            className="relative w-[80%] cursor-pointer bg-white border border-gray-300 rounded-lg shadow-sm"
            onClick={() => setIsOpen(true)}
          >
            <div className="flex flex-wrap gap-2 p-2">
              {selectedUsers.map((user) => (
                <div
                  key={user.userId}
                  className="flex items-center gap-2 bg-blue-100 px-3 py-1 rounded-full"
                >
                  <img
                    src={user.avatarUrl}
                    alt={user.username}
                    className="w-6 h-6 rounded-full object-cover"
                  />
                  <span className="text-sm text-blue-800">{user.username}</span>
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleUserRemove(user.userId);
                    }}
                    className="text-blue-800 hover:text-blue-600 focus:outline-none"
                    aria-label={`Remove ${user.username}`}
                  >
                    <FiX className="w-4 h-4"/>
                  </button>
                </div>
              ))}
              <div className="flex-1 relative">
                <input
                  id="moderator-search"
                  type="text"
                  className="w-full border-none focus:ring-0 p-1 text-sm"
                  placeholder="Search users..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  aria-expanded={isOpen}
                  aria-controls="user-listbox"
                  aria-activedescendant={isOpen ? "user-option-0" : undefined}
                />
                <div
                  className="absolute right-2 top-1/2 transform -translate-y-1/2 flex items-center gap-2 text-gray-400">
                  <FiSearch className="w-4 h-4"/>
                  <IoMdArrowDropdown
                    className={`w-5 h-5 transform transition-transform duration-200 ${
                      isOpen ? "rotate-180" : ""
                    }`}
                  />
                </div>
              </div>
            </div>
          </div>
          <button
            onClick={handleSubmit}
            className="flex bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors duration-200"
          >
            <FiPlus className="w-5 h-5 mr-1" />Add Moderator
          </button>
        </div>
          {isOpen && searchTerm.trim() && !isLoading && (
            <ul
              id="user-listbox"
              role="listbox"
              className="absolute z-10 w-[80%] mt-1 bg-white border border-gray-300 rounded-lg shadow-lg max-h-60 overflow-auto"
            >
              {filteredUsers.length > 0 ? (
                filteredUsers.map((user, index) => (
                  <li
                    key={user.userId}
                    id={`user-option-${index}`}
                    role="option"
                    aria-selected="false"
                    className="flex items-center gap-3 px-4 py-2 cursor-pointer hover:bg-gray-50 transition-colors duration-150"
                    onClick={() => handleUserSelect(user)}
                  >
                    <img
                      src={user.avatarUrl}
                      alt={user.username}
                      className="w-8 h-8 rounded-full object-cover"
                    />
                    <div>
                      <div className="font-medium text-gray-900">
                        {user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` :
                          user.firstName || user.lastName ? user.firstName || user.lastName : user.username}
                      </div>
                      <div className="text-sm text-gray-500">@{user.username}</div>
                    </div>
                  </li>
                ))
              ) : (
                <li className="px-4 py-2 text-sm text-gray-500">
                  No users found matching your search
                </li>
              )}
            </ul>
          )}
        </div>
      </div>

      {error && (
        <p className="text-red-500 text-sm mt-2" role="alert">
          {error}
        </p>
      )}

    </div>
  );
};

export default SearchableDropdown;