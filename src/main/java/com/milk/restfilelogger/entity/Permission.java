package com.milk.restfilelogger.entity;

/**
 * @author Jack Milk
 */
public enum Permission {

    LEVEL_LOW("level:low"),
    LEVEL_MIDDLE("level:middle"),
    LEVEL_HIGH("level:high");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
