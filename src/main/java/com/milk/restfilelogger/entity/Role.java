package com.milk.restfilelogger.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jack Milk
 */
public enum Role {
    USER(Set.of(
            Permission.LEVEL_LOW
    )),
    MODERATOR(Set.of(
            Permission.LEVEL_LOW,
            Permission.LEVEL_MIDDLE
    )),
    ADMIN(Set.of(
            Permission.LEVEL_LOW,
            Permission.LEVEL_MIDDLE,
            Permission.LEVEL_HIGH
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
