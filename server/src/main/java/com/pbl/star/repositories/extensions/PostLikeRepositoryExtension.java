package com.pbl.star.repositories.extensions;

import com.pbl.star.models.projections.user.OnInteractProfile;

import java.time.Instant;
import java.util.List;

public interface PostLikeRepositoryExtension {
    List<OnInteractProfile> getPostLikes(String currentUser, String postId, int limit, Instant after);
    List<OnInteractProfile> getPostReposts(String currentUser, String postId, int limit, Instant after);
    List<OnInteractProfile> getPostInteractions(String currentUser, String postId, int limit, Instant after);
}
