// import React, { useState, useRef, useEffect } from "react";
// import { FiSearch, FiX } from "react-icons/fi";
// import { IoMdArrowDropdown } from "react-icons/io";
// import { useFetchAllUsers } from "../../../hooks/user"; // Import the hook
// import { QueryObserverResult, RefetchOptions } from "@tanstack/react-query";
// import { AxiosError } from "axios";
// import { message } from "antd";

// interface PeopleType {
//   userId: string;
//   avatarUrl: string | null;
//   firstName: string | null;
//   lastName: string | null;
//   numberOfFollowers: number;
//   username: string;
//   followStatus: string;
// }

// interface IProps {
//   roomId: string;
//   refetchUsers: (options?: RefetchOptions | undefined) => Promise<QueryObserverResult<any, Error>>;
// }

// const SearchableDropdown: React.FC<IProps> = (props: IProps) => {
//   // const { roomId, refetchUsers } = props;

//   const [isOpen, setIsOpen] = useState(false);
//   const [searchTerm, setSearchTerm] = useState("");
//   const [selectedUsers, setSelectedUsers] = useState<PeopleType[]>([]);
//   const [error, setError] = useState("");
//   const dropdownRef = useRef<HTMLDivElement | null>(null);

//   const { data, isLoading, isError } = useFetchAllUsers(searchTerm); // Use the hook to fetch users based on the search term

//   const [filteredUsers, setFilteredUsers] = useState<PeopleType[]>([]);

//   useEffect(() => {
//     const handleClickOutside = (event: any) => {
//       if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
//         setIsOpen(false);
//       }
//     };

//     document.addEventListener("mousedown", handleClickOutside);
//     return () => document.removeEventListener("mousedown", handleClickOutside);
//   }, []);

//   useEffect(() => {
//     if (data) {
//       const notInSelectedUsers = data.filter(
//         (user: PeopleType) => !selectedUsers.some((selectedUser) => selectedUser.userId === user.userId)
//       );

//       setFilteredUsers(notInSelectedUsers);
//     }
//   }, [data, selectedUsers]);

//   const handleUserSelect = (user: PeopleType) => {
//     setSelectedUsers((prev) => [...prev, user]);
//     setSearchTerm("");
//     setError("");
//   };

//   const handleUserRemove = (userId: string) => {
//     setSelectedUsers(selectedUsers.filter((user) => user.userId !== userId));
//   };

//   const handleSubmit = async () => {
//     if (selectedUsers.length === 0) {
//       setError("Please select at least one user.");
//       return;
//     }

//     // Handle user submission or further logic here
//     // For now, we simply log selected users
//     console.log("Selected users:", selectedUsers);
//   };

//   return (
//     <div className="mx-auto p-4">
//       <div className="mb-4">
//         <div ref={dropdownRef} className="relative">
//           <div className="flex items-center gap-2 text-sm text-gray-500">
//             <div
//               className="relative w-[80%] cursor-pointer bg-white border border-gray-300 rounded-lg shadow-sm"
//               onClick={() => setIsOpen(true)}
//             >
//               <div className="flex flex-wrap gap-2 p-2">
//                 {selectedUsers.map((user) => (
//                   <div
//                     key={user.userId}
//                     className="flex items-center gap-2 bg-blue-100 px-3 py-1 rounded-full"
//                   >
//                     <span className="text-sm text-blue-800">{user.firstName} {user.lastName}</span>
//                     <button
//                       onClick={(e) => {
//                         e.stopPropagation();
//                         handleUserRemove(user.userId);
//                       }}
//                       className="text-blue-800 hover:text-blue-600 focus:outline-none"
//                       aria-label={`Remove ${user.firstName} ${user.lastName}`}
//                     >
//                       <FiX className="w-4 h-4" />
//                     </button>
//                   </div>
//                 ))}
//                 <div className="flex-1 relative">
//                   <input
//                     id="user-search"
//                     type="text"
//                     className="w-full border-none focus:ring-0 p-1 text-sm"
//                     placeholder="Search users..."
//                     value={searchTerm}
//                     onChange={(e) => setSearchTerm(e.target.value)}
//                     aria-expanded={isOpen}
//                     aria-controls="user-listbox"
//                     aria-activedescendant={isOpen ? "user-option-0" : undefined}
//                   />
//                   <div
//                     className="absolute right-2 top-1/2 transform -translate-y-1/2 flex items-center gap-2 text-gray-400"
//                   >
//                     <FiSearch className="w-4 h-4" />
//                     <IoMdArrowDropdown
//                       className={`w-5 h-5 transform transition-transform duration-200 ${isOpen ? "rotate-180" : ""}`}
//                     />
//                   </div>
//                 </div>
//               </div>
//             </div>
//           </div>
//           {isOpen && searchTerm.trim() && !isLoading && !isError && (
//             <ul
//               id="user-listbox"
//               role="listbox"
//               className="absolute z-10 w-[80%] mt-1 bg-white border border-gray-300 rounded-lg shadow-lg max-h-60 overflow-auto"
//             >
//               {filteredUsers.length > 0 ? (
//                 filteredUsers.map((user, index) => (
//                   <li
//                     key={user.userId}
//                     id={`user-option-${index}`}
//                     role="option"
//                     aria-selected="false"
//                     className="flex items-center gap-3 px-4 py-2 cursor-pointer hover:bg-gray-50 transition-colors duration-150"
//                     onClick={() => handleUserSelect(user)}
//                   >
//                     <div>
//                       <div className="font-medium text-gray-900">{user.firstName} {user.lastName}</div>
//                       <div className="text-sm text-gray-500">@{user.username}</div>
//                     </div>
//                   </li>
//                 ))
//               ) : (
//                 <li className="px-4 py-2 text-sm text-gray-500">
//                   No users found matching your search
//                 </li>
//               )}
//             </ul>
//           )}
//         </div>
//       </div>

//       {error && (
//         <p className="text-red-500 text-sm mt-2" role="alert">
//           {error}
//         </p>
//       )}

//       <div className="mt-4">
//         <button
//           onClick={handleSubmit}
//           className="bg-blue-600 text-white px-4 py-2 rounded-lg"
//         >
//           Submit
//         </button>
//       </div>
//     </div>
//   );
// };

// export default SearchableDropdown;
