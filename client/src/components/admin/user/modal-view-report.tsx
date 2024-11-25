// import React, { useState } from "react";
// import { format } from "date-fns";
// import { FaTimes, FaArrowLeft, FaUser, FaCalendar, FaExclamationTriangle } from "react-icons/fa";

// const ModalViewReport = () => {
//   // Initialize with isOpen as true for preview
//   const [isOpen, setIsOpen] = useState(true);
//   const [selectedReport, setSelectedReport] = useState(null);
//   // Sample username for preview
//   const username = "JohnDoe";

//   const mockReports = [
//     {
//       id: 1,
//       reporterName: "John Smith",
//       description: "This user has been posting inappropriate content and violating community guidelines repeatedly. Multiple instances of hate speech detected.",
//       createdAt: new Date("2024-01-15T14:30:00"),
//     },
//     {
//       id: 2,
//       reporterName: "Emma Wilson",
//       description: "Suspicious activity noticed on this account. Possible spam or automated behavior detected in their recent posts.",
//       createdAt: new Date("2024-01-14T09:15:00"),
//     },
//     {
//       id: 3,
//       reporterName: "Michael Brown",
//       description: "User has been harassing other community members through private messages and public comments.",
//       createdAt: new Date("2024-01-13T16:45:00"),
//     }
//   ];

//   const handleRowClick = (report) => {
//     setSelectedReport(report);
//   };

//   const handleBack = () => {
//     setSelectedReport(null);
//   };

//   const onClose = () => {
//     setIsOpen(false);
//   };

//   const truncateText = (text, maxLength) => {
//     return text.length > maxLength ? `${text.substring(0, maxLength)}...` : text;
//   };

//   if (!isOpen) return null;

//   return (
//     <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 transition-opacity">
//       <div className="relative w-full max-w-4xl max-h-[90vh] bg-white rounded-lg shadow-xl m-4 overflow-hidden">
//         <div className="flex items-center justify-between p-4 border-b border-gray-200">
//           <h2 className="text-xl font-semibold text-gray-800 flex items-center">
//             {selectedReport ? (
//               <button
//                 onClick={handleBack}
//                 className="mr-2 text-gray-600 hover:text-gray-800 transition-colors"
//               >
//                 <FaArrowLeft />
//               </button>
//             ) : null}
//             Reports for {username}
//           </h2>
//           <button
//             onClick={onClose}
//             className="text-gray-500 hover:text-gray-700 transition-colors"
//           >
//             <FaTimes size={20} />
//           </button>
//         </div>

//         <div className="p-4 overflow-y-auto max-h-[calc(90vh-120px)]">
//           {selectedReport ? (
//             <div className="space-y-4">
//               <div className="bg-gray-50 p-4 rounded-lg">
//                 <div className="flex items-center mb-3">
//                   <FaUser className="text-gray-600 mr-2" />
//                   <span className="font-medium text-gray-800">
//                     {selectedReport.reporterName}
//                   </span>
//                 </div>
//                 <div className="flex items-center mb-4">
//                   <FaCalendar className="text-gray-600 mr-2" />
//                   <span className="text-gray-600">
//                     {format(selectedReport.createdAt, "MM/dd/yyyy hh:mm a")}
//                   </span>
//                 </div>
//                 <div className="flex items-start">
//                   <FaExclamationTriangle className="text-red-500 mr-2 mt-1" />
//                   <p className="text-gray-700 leading-relaxed">
//                     {selectedReport.description}
//                   </p>
//                 </div>
//               </div>
//             </div>
//           ) : (
//             <div className="overflow-x-auto">
//               <table className="min-w-full divide-y divide-gray-200">
//                 <thead className="bg-gray-50">
//                   <tr>
//                     <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
//                       Reporter
//                     </th>
//                     <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
//                       Description
//                     </th>
//                     <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
//                       Date
//                     </th>
//                   </tr>
//                 </thead>
//                 <tbody className="bg-white divide-y divide-gray-200">
//                   {mockReports.map((report) => (
//                     <tr
//                       key={report.id}
//                       onClick={() => handleRowClick(report)}
//                       className="hover:bg-gray-50 cursor-pointer transition-colors"
//                     >
//                       <td className="px-6 py-4 whitespace-nowrap">
//                         <div className="text-sm font-medium text-gray-900">
//                           {report.reporterName}
//                         </div>
//                       </td>
//                       <td className="px-6 py-4">
//                         <div className="text-sm text-gray-500">
//                           {truncateText(report.description, 100)}
//                         </div>
//                       </td>
//                       <td className="px-6 py-4 whitespace-nowrap">
//                         <div className="text-sm text-gray-500">
//                           {format(report.createdAt, "MM/dd/yyyy hh:mm a")}
//                         </div>
//                       </td>
//                     </tr>
//                   ))}
//                 </tbody>
//               </table>
//             </div>
//           )}
//         </div>
//       </div>
//     </div>
//   );
// };

// export default ModalViewReport;