package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.request.post.CreateReplyParams;
import com.pbl.star.dtos.request.post.FilterPostParams;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.enums.FollowRequestStatus;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import com.pbl.star.mapper.post.PostEntityMapper;
import com.pbl.star.models.entities.Post;
import com.pbl.star.models.entities.PostImage;
import com.pbl.star.models.projections.post.*;
import com.pbl.star.repositories.*;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.helper.ResourceAccessControl;
import com.pbl.star.utils.ImageUtil;
import com.pbl.star.utils.SliceTransfer;
import com.pbl.star.validators.CreatePostValidator;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final FollowingRepository followingRepository;
    private final PostImageRepository postImageRepository;
    private final PostRepostRepository postRepostRepository;
    private final UserRoomRepository userRoomRepository;
    private final RoomRepository roomRepository;

    private final ResourceAccessControl resourceAccessControl;

    @Override
    @Transactional
    public Post createPost(String userId, CreatePostParams createPostParams) {

        CreatePostValidator.validateCreatePostRequiredFields(createPostParams);

        List<String> imageFileNames = createPostParams.getImageFileNames();

        PostEntityMapper postCreationMapper = Mappers.getMapper(PostEntityMapper.class);
        Post post = postCreationMapper.toEntity(createPostParams);

        if (!userRoomRepository.existsByUserIdAndRoomId(userId, post.getRoomId())) {
            throw new EntityNotFoundException("Room does not exist, or " +
                    "user is not a member of the room");
        }

        post.setUserId(userId);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setStatus(PostStatus.PENDING);

        Post savedPost = postRepository.save(post);

        if (imageFileNames!= null) {
            saveImagesInPost(savedPost.getId(), imageFileNames);
        }

        return savedPost;
    }

    private void saveImagesInPost(String postId, @NonNull List<String> imageFileNames) {
        String imagePrefixUrl = ImageUtil.getImagePrefixUrl();

        List<PostImage> postImages = new ArrayList<>(imageFileNames.size());
        for (int i = 0; i < imageFileNames.size(); i++) {
            postImages.add(PostImage.builder()
                    .postId(postId)
                    .position(i)
                    .imageUrl(imagePrefixUrl + imageFileNames.get(i))
                    .build());
        }

        postImageRepository.saveAll(postImages);
    }

    @Override
    public PostForUser getPostById(String currentUserId, String postId) {
        return postRepository.findExistPostByIdAsUser(currentUserId, postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
    }

    @Override
    public Page<PostForAdmin> getPostsAsAdmin(int page, int size, FilterPostParams filter) {

        if (filter == null) {
            filter = new FilterPostParams();
        }

        Pageable pageable = PageRequest.of(page, size);

        List<PostForAdmin> postsList = postRepository.findExistPostsAsAdmin(pageable, filter);
        long totalElements = postRepository.countExistPostsAsAdmin(filter);

        return new PageImpl<>(postsList, pageable, totalElements);
    }

    @Override
    public Slice<PostForUser> getPostsOnUserWall(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User have private profile");
        }

        List<PostForUser> postsList = postRepository.findExistPostsOfUsersByStatusAsUser(limit + 1, after, PostStatus.APPROVED, List.of(targetUserId));

        return SliceTransfer.trimToSlice(postsList, limit);
    }

    @Override
    public Slice<PendingPostForUser> getPendingPostsByUser(String currentUserId, int limit, Instant after) {
        List<PendingPostForUser> postsList = postRepository.findExistPendingPostsOfUser(limit + 1, after, currentUserId);
        return SliceTransfer.trimToSlice(postsList, limit);
    }

    @Override
    public Slice<PostForUser> getPostsOnNewsfeed(String userId, int limit, Instant after) {
        List<String> joinedRoomIds = userRoomRepository.findRoomIdsByUserId(userId);

        if (joinedRoomIds.isEmpty()) {
            return new SliceImpl<>(new ArrayList<>());  // Empty slice
        }

        List<PostForUser> postsList = postRepository.findExistPostsInRoomsByStatusAsUser(limit + 1, after, PostStatus.APPROVED, joinedRoomIds);
        return SliceTransfer.trimToSlice(postsList, limit);
    }

    @Override
    public Slice<PostForUser> getPostsOfFollowingUsers(String currentUserId, int limit, Instant after) {
        List<String> followingUserIds = followingRepository.findFolloweeIdsByFollowerIdAndStatus(currentUserId, FollowRequestStatus.ACCEPTED);

        if (followingUserIds.isEmpty()) {
            return new SliceImpl<>(new ArrayList<>());  // Empty slice
        }

        List<PostForUser> posts = postRepository.findExistPostsOfUsersByStatusAsUser(limit + 1, after, PostStatus.APPROVED, followingUserIds);
        return SliceTransfer.trimToSlice(posts, limit);
    }

    @Override
    public Slice<PostForUser> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room does not exist");
        }

        List<PostForUser> postsList = postRepository.findExistPostsInRoomsByStatusAsUser(limit + 1, after, status, List.of(roomId));
        return SliceTransfer.trimToSlice(postsList, limit);
    }

    @Override
    public Slice<PostForMod> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room does not exist");
        }

        List<PostForMod> postsList = postRepository.findExistPostsInRoomByStatusAsMod(limit + 1, after, status, roomId);
        return SliceTransfer.trimToSlice(postsList, limit);
    }

    @Override
    @Transactional
    public Post deletePostOfUser(String postId, String userId) {
        Post post = postRepository.findExistPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        if (!post.getUserId().equals(userId)) {
            throw new ResourceOwnershipException("Cannot delete post of another user");
        }

        post.setDeleted(true);
        post.setDeletedAt(Instant.now());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post createReply(String userId, String parentPostId, CreateReplyParams createReplyParams) {

        CreatePostValidator.validateCreateReplyRequiredFields(createReplyParams);

        List<String> imageFileNames = createReplyParams.getImageFileNames();

        Post parentPost = postRepository.findExistPostById(parentPostId)
                .orElseThrow(() -> new EntityNotFoundException("Parent post does not exist"));

        if (!userRoomRepository.existsByUserIdAndRoomId(userId, parentPost.getRoomId())) {
            throw new EntityNotFoundException("Room does not exist, or " +
                    "user is not a member of the room");
        }

        Post reply = Post.builder()
                .userId(userId)
                .roomId(parentPost.getRoomId())
                .parentPostId(parentPost.getId())
                .content(createReplyParams.getContent())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .status(PostStatus.APPROVED)
                .build();

        Post savedReply = postRepository.save(reply);

        if (imageFileNames != null) {
            saveImagesInPost(savedReply.getId(), imageFileNames);
        }

        return savedReply;
    }

    @Override
    public CustomSlice<PostForUser> getReplies(String userId, String postId, int limit, Instant after) {
        List<PostForUser> repliesList = postRepository.findExistRepliesOfPostAsUser(limit + 1, after, userId, postId);
        Slice<PostForUser> replies = SliceTransfer.trimToSlice(repliesList, limit);

        CustomSlice<PostForUser> repliesPage = new CustomSlice<>(replies);

        if (after == null) {
            repliesPage.setTotalElements(postRepository.countExistRepliesOfPost(postId));
        }

        return repliesPage;
    }

    @Override
    public Slice<ReplyOnWall> getRepliesOnWall(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User have private profile");
        }

        List<ReplyOnWall> postsList = postRepository.findExistRepliesOnWallAsUser(limit + 1, after, currentUserId, targetUserId);
        return SliceTransfer.trimToSlice(postsList, limit);
    }

    @Override
    public Slice<RepostOnWall> getRepostsOnWall(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User have private profile");
        }

        List<RepostOnWall> repostsList = postRepostRepository.findRepostsOnWallAsUser(limit + 1, after, currentUserId, targetUserId);
        return SliceTransfer.trimToSlice(repostsList, limit);
    }
}
