package com.pbl.star.services.domain.impl;

import com.pbl.star.dtos.request.post.CreateReportParams;
import com.pbl.star.dtos.request.post.RejectPostParams;
import com.pbl.star.dtos.response.post.PostInteractionListResponse;
import com.pbl.star.enums.InteractType;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.enums.ReportStatus;
import com.pbl.star.enums.RoomRole;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.exceptions.ModeratorAccessException;
import com.pbl.star.models.entities.Post;
import com.pbl.star.models.entities.PostLike;
import com.pbl.star.models.entities.PostReport;
import com.pbl.star.models.entities.PostRepost;
import com.pbl.star.models.projections.report.ReportForMod;
import com.pbl.star.models.projections.user.OnInteractProfile;
import com.pbl.star.repositories.*;
import com.pbl.star.services.domain.PostInteractionService;
import com.pbl.star.utils.SliceTransfer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
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
    private final PostReportRepository postReportRepository;
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
    public PostReport reportPost(String userId, String postId, CreateReportParams params) {
        if (!postRepository.existsByIdAndDeleted(postId, false)) {
            throw new EntityNotFoundException("Post does not exist");
        }

        if (postReportRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new EntityConflictException("User already reported the post");
        }

        PostReport postReport = PostReport.builder()
                .postId(postId)
                .userId(userId)
                .reason(params.getReason())
                .reportAt(Instant.now())
                .status(ReportStatus.PENDING)
                .build();

        return postReportRepository.save(postReport);
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
        post.setRejectReason(null);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post rejectPost(String postId, String moderatorId, RejectPostParams params) {
        Post post = postRepository.findExistPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        if (!userRoomRepository.existsByUserIdAndRoomIdAndRole(moderatorId, post.getRoomId(), RoomRole.MODERATOR)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        if (post.getStatus() == PostStatus.REJECTED) {
            throw new EntityConflictException("Post already has the status REJECTED");
        }

        post.setStatus(PostStatus.REJECTED);
        post.setModeratedBy(moderatorId);
        post.setModeratedAt(Instant.now());
        post.setRejectReason(params.getReason());
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
        post.setRejectReason(null);
        postRepository.save(post);
    }

    @Override
    public PostInteractionListResponse getActorProfilesOfPost(String currentUserId, String postId, InteractType type, int limit, Instant after) {

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

        return PostInteractionListResponse.builder()
                .actors(SliceTransfer.trimToSlice(actorProfiles, limit))
                .likesCount(likesCount)
                .repostsCount(repostsCount)
                .viewsCount(viewCount)
                .build();
    }

    @Override
    public Slice<ReportForMod> getReportsForMod(String moderatorId, String postId, int limit, Instant after) {
        Post post = postRepository.findExistPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist"));

        if (!userRoomRepository.existsByUserIdAndRoomIdAndRole(moderatorId, post.getRoomId(), RoomRole.MODERATOR)) {
            throw new ModeratorAccessException("User is not a moderator of the room");
        }

        List<ReportForMod> reports = postReportRepository.getReportsOfPost(postId, limit + 1, after);
        return SliceTransfer.trimToSlice(reports, limit);
    }
}
