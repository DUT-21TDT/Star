package com.pbl.star.validators;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.exceptions.RequiredFieldMissingException;
import org.apache.commons.lang3.StringUtils;

public class CreatePostValidator {
    public static void validateCreatePostRequiredFields(CreatePostParams createPostParams) {
        if (StringUtils.isBlank(createPostParams.getRoomId())) {
            throw new RequiredFieldMissingException("Room ID is required");
        }

        if (isContentEmpty(createPostParams)) {
            throw new RequiredFieldMissingException("Content or image is required");
        }
    }

    public static void validateCreateReplyRequiredFields(CreatePostParams createReplyParams) {
        if (StringUtils.isBlank(createReplyParams.getParentPostId())) {
            throw new RequiredFieldMissingException("Post ID is required");
        }

        if (isContentEmpty(createReplyParams)) {
            throw new RequiredFieldMissingException("Content or image is required");
        }
    }

    private static boolean isContentEmpty(CreatePostParams createPostParams) {
        boolean isContentEmpty = StringUtils.isBlank(createPostParams.getContent());
        boolean isImageFileNamesEmpty = createPostParams.getImageFileNames() == null || createPostParams.getImageFileNames().isEmpty();

        return isContentEmpty && isImageFileNamesEmpty;
    }
}
