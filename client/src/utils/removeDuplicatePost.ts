interface PostType {
  id: string;
  usernameOfCreator: string;
  avatarUrlOfCreator: string | null;
  createdAt: string;
  content: string;
  postImageUrls: string[];
  numberOfLikes: number;
  numberOfComments: number;
  numberOfReposts: number;
  liked: boolean;
  reposted: boolean;
  idOfCreator: string;
  nameOfRoom: string;
}
const RemoveDuplicatePost = (posts: PostType[]): PostType[] => {
  const uniquePostsMap = new Map<string, PostType>();
  for (let i = posts.length - 1; i >= 0; i--) {
    const post = posts[i];
    if (!uniquePostsMap.has(post.id)) {
      uniquePostsMap.set(post.id, post);
    }
  }
  // return Array.from(uniquePostsMap.values()).reverse();
  return Array.from(uniquePostsMap.values()).sort((a, b) => {
    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
  });
};
export default RemoveDuplicatePost;
