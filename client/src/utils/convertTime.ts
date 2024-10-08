function timeAgo(inputTime: string): string {
  const now = new Date();
  const timeDiff = now.getTime() - new Date(inputTime).getTime();

  const seconds = Math.floor(timeDiff / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);
  const weeks = Math.floor(days / 7);
  const months = Math.max(Math.floor(days / 30.44), 1);
  const years = Math.max(Math.floor(days / 365.25), 1);

  if (seconds < 60) {
    return "Just now";
  } else if (minutes < 60) {
    return minutes === 1 ? `${minutes} min ago` : `${minutes} mins ago`;
  } else if (hours < 24) {
    return hours === 1 ? `${hours} hour ago` : `${hours} hours ago`;
  } else if (days < 7) {
    return days === 1 ? `${days} day ago` : `${days} days ago`;
  } else if (weeks < 4) {
    return weeks === 1 ? `${weeks} week ago` : `${weeks} weeks ago`;
  } else if (months < 12) {
    return months === 1 ? `${months} month ago` : `${months} months ago`;
  } else {
    return years === 1 ? `${years} year ago` : `${years} years ago`;
  }
}
export { timeAgo };
