package com.pbl.star.services.helper;

public interface ResourceAccessControl {
    boolean isPrivateProfileBlock(String currentUserId, String targetUserId);
}
