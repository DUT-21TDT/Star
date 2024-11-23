package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.post.*;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.response.CustomSlice;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostImage;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.enums.RoomRole;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import com.pbl.star.mapper.PostCreationMapper;
import com.pbl.star.repositories.*;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.services.helper.ResourceAccessControl;
import com.pbl.star.validators.CreatePostValidator;
import com.pbl.star.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostRepostRepository postRepostRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final ResourceAccessControl resourceAccessControl;

    @Override
    @Transactional
    public Post createPost(String userId, CreatePostParams createPostParams) {

        CreatePostValidator.validateCreatePostRequiredFields(createPostParams);

        List<String> imageFileNames = createPostParams.getImageFileNames();
        if (imageFileNames != null && imageFileNames.size() > 20) {
            throw new EntityConflictException("Number of images exceeds the limit");
        }

        PostCreationMapper postCreationMapper = Mappers.getMapper(PostCreationMapper.class);
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
    public PostForUserDTO getPostById(String currentUserId, String postId) {
        return postRepository.findExistPostByIdAsUser(currentUserId, postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnUserWall(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User have private profile");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findExistPostsOfUserByStatusAsUser(pageable, after, PostStatus.APPROVED, targetUserId);
    }

    @Override
    public Slice<PendingPostForUserDTO> getPendingPostsByUser(String currentUserId, int limit, Instant after) {
        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findExistPendingPostsOfUser(pageable, after, currentUserId);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnNewsfeed(String userId, int limit, Instant after) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User does not exist");
        }

        Pageable pageable = PageRequest.of(0, limit);
        String[] joinedRoomIds = userRoomRepository.findRoomIdsByUserId(userId).toArray(String[]::new);
        return postRepository.findExistPostsInRoomsByStatusAsUser(pageable, after, PostStatus.APPROVED, joinedRoomIds);
    }

    @Override
    public Slice<PostForUserDTO> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room does not exist");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findExistPostsInRoomsByStatusAsUser(pageable, after, status, roomId);
    }

    @Override
    public Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room does not exist");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findExistPostsInRoomByStatusAsMod(pageable, after, status, roomId);
    }

    @Override
    @Transactional
    public Post moderatePostStatus(String postId, PostStatus status, String moderatorId) {
        Post post = postRepository.findExistPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        if (!userRoomRepository.existsByUserIdAndRoomIdAndRole(moderatorId, post.getRoomId(), RoomRole.MODERATOR)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        if (post.getStatus() == status) {
            throw new EntityConflictException("Post already has the status " + status.name());
        }

        post.setStatus(status);
        post.setModeratedBy(moderatorId);
        post.setModeratedAt(Instant.now());
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public void unmoderatePostStatus(String postId, String moderatorId) {
        Post post = postRepository.findExistPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        if (!userRoomRepository.existsByUserIdAndRoomIdAndRole(moderatorId, post.getRoomId(), RoomRole.MODERATOR)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        if (post.getStatus() == PostStatus.PENDING) {
            throw new EntityConflictException("Post is not moderated yet");
        }

        post.setStatus(PostStatus.PENDING);
        post.setModeratedBy(null);
        post.setModeratedAt(null);
        postRepository.save(post);
    }

    @Override
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
    public Post createReply(String userId, CreatePostParams createReplyParams) {

        CreatePostValidator.validateCreateReplyRequiredFields(createReplyParams);

        List<String> imageFileNames = createReplyParams.getImageFileNames();
        if (imageFileNames != null && imageFileNames.size() > 20) {
            throw new EntityConflictException("Number of images exceeds the limit");
        }

        Post parentPost = postRepository.findExistPostById(createReplyParams.getParentPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

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
    public CustomSlice<PostForUserDTO> getReplies(String userId, String postId, int limit, Instant after) {
        Pageable pageable = PageRequest.of(0, limit);
        Slice<PostForUserDTO> replies = postRepository.findExistRepliesOfPostAsUser(pageable, after, userId, postId);

        CustomSlice<PostForUserDTO> repliesPage = new CustomSlice<>(replies);

        if (after == null) {
            repliesPage.setTotalElements(postRepository.countExistRepliesOfPost(postId));
        }

        return repliesPage;
    }

    @Override
    public Slice<ReplyOnWallDTO> getRepliesOnWall(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User have private profile");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findExistRepliesOnWallAsUser(pageable, after, currentUserId, targetUserId);
    }

    @Override
    public Slice<RepostOnWallDTO> getRepostsOnWall(String currentUserId, String targetUserId, int limit, Instant after) {

        if (resourceAccessControl.isPrivateProfileBlock(currentUserId, targetUserId)) {
            throw new ResourceOwnershipException("User have private profile");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepostRepository.findRepostsOnWallAsUser(pageable, after, currentUserId, targetUserId);
    }


    // Repository methods
    @Override
    public Optional<Post> findExistPostById(String postId) {
        return postRepository.findExistPostById(postId);
    }
}
