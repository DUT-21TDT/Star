package com.pbl.star.services.impl;

import com.pbl.star.dtos.query.post.PostOverviewDTO;
import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostImage;
import com.pbl.star.enums.PostStatus;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.mapper.PostCreationMapper;
import com.pbl.star.repositories.*;
import com.pbl.star.services.PostService;
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
    private final UserRoomRepository userRoomRepository;

    @Override
    @Transactional
    public String createPost(String userId, CreatePostParams createPostParams) {

        CreatePostValidator.validateCreatePostRequiredFields(createPostParams);
        PostCreationMapper postCreationMapper = Mappers.getMapper(PostCreationMapper.class);
        Post post = postCreationMapper.toEntity(createPostParams);

        if (!userRoomRepository.existsByUserIdAndRoomId(userId, post.getRoomId())) {
            throw new EntityNotFoundException("User does not exist, or " +
                    "Room does not exist, or " +
                    "User is not a member of the room");
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
    public Slice<PostOverviewDTO> getPostsByUser(String userId, int limit, Instant after) {
        Pageable pageable = PageRequest.of(0, limit);
        return postRepository.findPostOverviewsByUserAndStatus(userId, pageable, after, PostStatus.APPROVED);
    }
}
