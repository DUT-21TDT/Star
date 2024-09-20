package com.pbl.star.utils;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.exceptions.RequiredFieldMissingException;
import org.apache.commons.lang3.StringUtils;

public class CreatePostValidator {
    public static void validateCreatePostRequiredFields(CreatePostParams createPostParams) {
        if (StringUtils.isBlank(createPostParams.getRoomId())) {
            throw new RequiredFieldMissingException("Room ID is required");
        }

        if (StringUtils.isBlank(createPostParams.getContent())) {
            throw new RequiredFieldMissingException("Content is required");
        }
    }
}
