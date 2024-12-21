package com.pbl.star.validators;

import com.pbl.star.dtos.request.post.CreatePostParams;
import com.pbl.star.dtos.request.post.CreateReplyParams;
import com.pbl.star.exceptions.RequiredFieldMissingException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CreatePostValidator {
    public static void validateCreatePostRequiredFields(CreatePostParams createPostParams) {
        if (isContentEmpty(createPostParams.getContent(), createPostParams.getImageFileNames())) {
            throw new RequiredFieldMissingException("Content or image is required");
        }
    }

    public static void validateCreateReplyRequiredFields(CreateReplyParams createReplyParams) {
        if (isContentEmpty(createReplyParams.getContent(), createReplyParams.getImageFileNames())) {
            throw new RequiredFieldMissingException("Content or image is required");
        }
    }

    private static boolean isContentEmpty(String content, List<String> imageFilenames) {
        boolean isContentEmpty = StringUtils.isBlank(content);
        boolean isImageFileNamesEmpty = imageFilenames == null || imageFilenames.isEmpty();

        return isContentEmpty && isImageFileNamesEmpty;
    }
}
