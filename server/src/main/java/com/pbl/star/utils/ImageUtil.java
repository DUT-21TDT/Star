package com.pbl.star.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageUtil {
    private static String IMAGE_PREFIX_URL;

    @Value("${aws.s3.prefix-url}")
    private void setImagePrefixUrl(String privateName) {
        IMAGE_PREFIX_URL = privateName;
    }

    public static String getImagePrefixUrl() {
        return IMAGE_PREFIX_URL;
    }
}
