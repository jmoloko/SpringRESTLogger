package com.milk.restfilelogger.entity;

/**
 * @author Jack Milk
 */
public enum Permission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    FILES_READ("files:read"),
    FILES_WRITE("files:write"),
    FILES_UPLOAD("files:upload"),
    FILES_DOWNLOAD("files:download"),
    FILES_DELETE("files:delete"),
    EVENTS_READ("events:read"),
    EVENTS_WRITE("events:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
