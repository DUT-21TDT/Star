package com.pbl.star.services.domain.impl;

import com.pbl.star.entities.PostLike;
import com.pbl.star.entities.PostRepost;
import com.pbl.star.exceptions.EntityConflictException;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.repositories.PostLikeRepository;
import com.pbl.star.repositories.PostRepository;
import com.pbl.star.repositories.PostRepostRepository;
import com.pbl.star.services.domain.PostInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostInteractionServiceImpl implements PostInteractionService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepostRepository postRepostRepository;

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
    public void deleteRepostPost(String userId, String postId) {
        PostRepost postRepost = postRepostRepository.findPostRepostByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post is not exist, or user did not repost the post"));

        postRepostRepository.delete(postRepost);
    }
}
