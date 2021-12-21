package com.milk.restfilelogger.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */
public enum Role {
    USER(Set.of(
            Permission.FILES_READ,
            Permission.FILES_UPLOAD,
            Permission.FILES_DOWNLOAD,
            Permission.EVENTS_READ
    )),
    MODERATOR(Set.of(
            Permission.FILES_READ,
            Permission.FILES_WRITE,
            Permission.FILES_UPLOAD,
            Permission.FILES_DOWNLOAD,
            Permission.FILES_DELETE
    )),
    ADMIN(Set.of(
            Permission.USERS_READ,
            Permission.USERS_WRITE,
            Permission.FILES_READ,
            Permission.FILES_WRITE,
            Permission.FILES_UPLOAD,
            Permission.FILES_DOWNLOAD,
            Permission.FILES_DELETE,
            Permission.EVENTS_READ,
            Permission.EVENTS_WRITE
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
