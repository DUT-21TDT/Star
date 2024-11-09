package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.post.PendingPostForUserDTO;
import com.pbl.star.dtos.query.post.PostForModDTO;
import com.pbl.star.dtos.query.post.PostForUserDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostImage;
import com.pbl.star.entities.PostLike;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.enums.RoomRole;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.exceptions.ResourceOwnershipException;
import com.pbl.star.mapper.PostCreationMapper;
import com.pbl.star.repositories.*;
import com.pbl.star.services.domain.PostService;
import com.pbl.star.utils.CreatePostValidator;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final FollowingRepository followingRepository;

    @Override
    @Transactional
    public String createPost(String userId, CreatePostParams createPostParams) {

        CreatePostValidator.validateCreatePostRequiredFields(createPostParams);
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

        if (createPostParams.getImageFileNames() != null) {
            saveImagesInPost(savedPost.getId(), createPostParams.getImageFileNames());
        }

        return savedPost.getId();
    }

    private void saveImagesInPost(String postId, @NonNull List<String> imageFileNames) {
        String imagePrefixUrl = ImageUtil.getImagePrefixUrl();
        List<PostImage> postImages = imageFileNames.stream()
                .map(imageFileName -> PostImage.builder()
                        .postId(postId)
                        .imageUrl(imagePrefixUrl + imageFileName)
                        .build())
                .toList();

        postImageRepository.saveAll(postImages);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnUserWall(String currentUserId, String targetUserId, int limit, Instant after) {
        boolean privateProfile = userRepository.getPrivateProfileById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        if (privateProfile &&
                !currentUserId.equals(targetUserId) &&
                !followingRepository.isFollowing(currentUserId, targetUserId)
        ) {
            throw new ResourceOwnershipException("User has private profile");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findPostsOfUserByStatus(pageable, after, PostStatus.APPROVED, targetUserId);
    }

    @Override
    public Slice<PendingPostForUserDTO> getPendingPostsByUser(String currentUserId, int limit, Instant after) {
        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findPendingPostsOfUser(pageable, after, currentUserId);
    }

    @Override
    public Slice<PostForUserDTO> getPostsOnNewsfeed(String userId, int limit, Instant after) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User does not exist");
        }

        Pageable pageable = PageRequest.of(0, limit);
        String[] joinedRoomIds = userRoomRepository.findRoomIdsByUserId(userId).toArray(String[]::new);
        return postRepository.findPostsInRoomsByStatusAsUser(pageable, after, PostStatus.APPROVED, joinedRoomIds);
    }

    @Override
    public Slice<PostForUserDTO> getPostsInRoom(String roomId, PostStatus status, int limit, Instant after) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room does not exist");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findPostsInRoomsByStatusAsUser(pageable, after, status, roomId);
    }

    @Override
    public Slice<PostForModDTO> getPostsInRoomAsMod(String roomId, PostStatus status, int limit, Instant after) {
        if (!roomRepository.existsById(roomId)) {
            throw new EntityNotFoundException("Room does not exist");
        }

        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findPostsInRoomByStatusAsMod(pageable, after, status, roomId);
    }

    @Override
    @Transactional
    public void likePost(String userId, String postId) {
        if (!postRepository.existsById(postId)) {
            throw new EntityNotFoundException("Post does not exist");
        }

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new EntityConflictException("User already liked the post");
        }

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .likeAt(Instant.now())
                .build();

        postLikeRepository.save(postLike);
    }

    @Override
    @Transactional
    public void unlikePost(String userId, String postId) {
        PostLike postLike = postLikeRepository.findPostLikeByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exist, or user did not like the post"));

        postLikeRepository.delete(postLike);
    }

    @Override
    @Transactional
    public void moderatePostStatus(String postId, PostStatus status, String moderatorId) {
        Post post = postRepository.findById(postId)
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
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void unmoderatePostStatus(String postId, String moderatorId) {
        Post post = postRepository.findById(postId)
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
}
