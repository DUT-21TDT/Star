import React, { useState, useRef, useEffect } from "react";
import { FiSearch, FiX } from "react-icons/fi";
import { IoMdArrowDropdown } from "react-icons/io";
import { getAllPostInRoom } from "../../../service/postAPI.ts";
import { QueryObserverResult, RefetchOptions } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { message } from "antd";

interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[] | null;
  idOfCreator?: string;
  nameOfRoom?: string | null;
  idOfModerator?: string | null;
  usernameOfModerator?: string | null;
  moderatedAt?: string | null;
  violenceScore: number;
  status: string;
  isChangeStatus?: string;
}

interface IProps {
  roomId: string;
  posts: PostType[];
  refetchPosts: (options?: RefetchOptions | undefined) => Promise<QueryObserverResult<any, Error>>;
}

const SearchableDropdown: React.FC<IProps> = (props: IProps) => {
  const { roomId, posts, refetchPosts } = props;

  const [isOpen, setIsOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedPosts, setSelectedPosts] = useState<PostType[]>([]);
  const [error, setError] = useState("");
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const [filteredPosts, setFilteredPosts] = useState<PostType[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const handleClickOutside = (event: any) => {
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
        // Call the API to get filtered posts based on search term
        getAllPostInRoom(roomId, { limit: 10, after: null }) // Assuming getAllPostInRoom accepts this config
          .then((postsData) => {
            const notInSelectedPosts = postsData.filter(
              (post: PostType) => !selectedPosts.some((selectedPost) => selectedPost.id === post.id)
            );

            setFilteredPosts(notInSelectedPosts);
            setIsLoading(false);
          })
          .catch((error) => {
            console.error("Error fetching posts:", error);
            setFilteredPosts([]);
          });
      } else {
        setFilteredPosts([]);
      }
    }, 300); // Debounce duration: 300ms

    return () => clearTimeout(timeoutId); // Cleanup timeout on unmount or searchTerm change
  }, [searchTerm, roomId, selectedPosts]);

  const handlePostSelect = (post: PostType) => {
    setSelectedPosts(prev => [...prev, post]);
    setSearchTerm("");
    setError("");
  };

  const handlePostRemove = (postId: string) => {
    setSelectedPosts(selectedPosts.filter((post) => post.id !== postId));
  };

  const handleSubmit = async () => {
    if (selectedPosts.length === 0) {
      setError("Please select at least one post.");
      return;
    }

    // Handle post submission or further logic here
    // For now, we simply log selected posts
    console.log("Selected posts:", selectedPosts);
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
                {selectedPosts.map((post) => (
                  <div
                    key={post.id}
                    className="flex items-center gap-2 bg-blue-100 px-3 py-1 rounded-full"
                  >
                    <span className="text-sm text-blue-800">{post.content}</span>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handlePostRemove(post.id);
                      }}
                      className="text-blue-800 hover:text-blue-600 focus:outline-none"
                      aria-label={`Remove ${post.content}`}
                    >
                      <FiX className="w-4 h-4" />
                    </button>
                  </div>
                ))}
                <div className="flex-1 relative">
                  <input
                    id="post-search"
                    type="text"
                    className="w-full border-none focus:ring-0 p-1 text-sm"
                    placeholder="Search posts..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    aria-expanded={isOpen}
                    aria-controls="post-listbox"
                    aria-activedescendant={isOpen ? "post-option-0" : undefined}
                  />
                  <div
                    className="absolute right-2 top-1/2 transform -translate-y-1/2 flex items-center gap-2 text-gray-400"
                  >
                    <FiSearch className="w-4 h-4" />
                    <IoMdArrowDropdown
                      className={`w-5 h-5 transform transition-transform duration-200 ${
                        isOpen ? "rotate-180" : ""
                      }`}
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
          {isOpen && searchTerm.trim() && !isLoading && (
            <ul
              id="post-listbox"
              role="listbox"
              className="absolute z-10 w-[80%] mt-1 bg-white border border-gray-300 rounded-lg shadow-lg max-h-60 overflow-auto"
            >
              {filteredPosts.length > 0 ? (
                filteredPosts.map((post, index) => (
                  <li
                    key={post.id}
                    id={`post-option-${index}`}
                    role="option"
                    aria-selected="false"
                    className="flex items-center gap-3 px-4 py-2 cursor-pointer hover:bg-gray-50 transition-colors duration-150"
                    onClick={() => handlePostSelect(post)}
                  >
                    <div>
                      <div className="font-medium text-gray-900">{post.content}</div>
                      <div className="text-sm text-gray-500">@{post.usernameOfCreator}</div>
                    </div>
                  </li>
                ))
              ) : (
                <li className="px-4 py-2 text-sm text-gray-500">
                  No posts found matching your search
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

      <div className="mt-4">
        <button
          onClick={handleSubmit}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg"
        >
          Submit
        </button>
      </div>
    </div>
  );
};

export default SearchableDropdown;
