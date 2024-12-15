package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.query.user.OnInteractProfile;
import com.pbl.star.dtos.response.post.PostInteractionList;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostLike;
import com.pbl.star.entities.PostRepost;
import com.pbl.star.enums.InteractType;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.enums.RoomRole;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.repositories.PostLikeRepository;
import com.pbl.star.repositories.PostRepository;
import com.pbl.star.repositories.PostRepostRepository;
import com.pbl.star.repositories.UserRoomRepository;
import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.utils.SliceTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostInteractionServiceImpl implements PostInteractionService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepostRepository postRepostRepository;
    private final UserRoomRepository userRoomRepository;

    @Override
    @Transactional
    public PostLike likePost(String userId, String postId) {
        if (!postRepository.existsByIdAndDeleted(postId, false)) {
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

        return postLikeRepository.save(postLike);
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
    public PostRepost repostPost(String userId, String postId) {

        if (!postRepository.existsByIdAndDeleted(postId, false)) {
            throw new EntityNotFoundException("Post does not exist");
        }

        if (postRepostRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new EntityConflictException("User already reposted the post");
        }

        PostRepost postRepost = PostRepost.builder()
                .postId(postId)
                .userId(userId)
                .repostAt(Instant.now())
                .build();

        return postRepostRepository.save(postRepost);
    }

    @Override
    @Transactional
    public void deleteRepostPost(String userId, String postId) {
        PostRepost postRepost = postRepostRepository.findPostRepostByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exist, or user did not repost the post"));

        postRepostRepository.delete(postRepost);
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
    public PostInteractionList getActorProfilesOfPost(String currentUserId, String postId, InteractType type, int limit, Instant after) {

        if (!postRepository.existsByIdAndDeleted(postId, false)) {
            throw new EntityNotFoundException("Post does not exist");
        }

        List<OnInteractProfile> actorProfiles = new ArrayList<>();
        Long likesCount = null;
        Long repostsCount = null;
        Long viewCount = 999L;

        if (type == InteractType.LIKE) {
            actorProfiles = postLikeRepository.getPostLikes(currentUserId, postId, limit + 1, after);

            if (after == null) {
                likesCount = postLikeRepository.countPostLikesByPostId(postId);
            }

        } else if (type == InteractType.REPOST) {
            actorProfiles = postLikeRepository.getPostReposts(currentUserId, postId, limit + 1, after);

            if (after == null) {
                repostsCount = postRepostRepository.countPostRepostsByPostId(postId);
            }

        } else if (type == InteractType.ALL) {
            actorProfiles = postLikeRepository.getPostInteractions(currentUserId, postId, limit + 1, after);

            if (after == null) {
                likesCount = postLikeRepository.countPostLikesByPostId(postId);
                repostsCount = postRepostRepository.countPostRepostsByPostId(postId);
            }
        }

        return PostInteractionList.builder()
                .actors(SliceTransfer.trimToSlice(actorProfiles, limit))
                .likesCount(likesCount)
                .repostsCount(repostsCount)
                .viewsCount(viewCount)
                .build();
    }
}
