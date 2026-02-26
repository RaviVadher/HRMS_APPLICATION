package com.roima.hrms.achievement.enums;

public enum MediaType {
    IMAGE("image"),
    VIDEO("video");

    private final String displayName;

    MediaType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

