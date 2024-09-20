package com.pbl.star.services.impl;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.entities.Post;
import com.pbl.star.entities.PostImage;
import com.pbl.star.exceptions.EntityNotFoundException;
import com.pbl.star.mapper.PostCreationMapper;
import com.pbl.star.repositories.*;
import com.pbl.star.services.PostService;
import com.pbl.star.utils.CreatePostValidator;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${aws.s3.prefix-url}")
    private String imagePrefixUrl;

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

        Post savedPost = postRepository.save(post);

        if (createPostParams.getImageFileNames() != null) {
            saveImagesInPost(savedPost.getId(), createPostParams.getImageFileNames());
        }

        return savedPost.getId();
    }

    private void saveImagesInPost(String postId, List<String> imageFileNames) {
        for (String fileName : imageFileNames) {
            PostImage postImage = PostImage.builder()
                    .postId(postId)
                    .imageUrl(imagePrefixUrl + fileName)
                    .build();
            postImageRepository.save(postImage);
        }
    }
}
