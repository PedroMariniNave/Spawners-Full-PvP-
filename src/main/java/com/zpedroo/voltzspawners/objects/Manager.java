package com.zpedroo.voltzspawners.objects;

import com.zpedroo.voltzspawners.utils.enums.Permission;

import java.util.List;
import java.util.UUID;

public class Manager {

    private UUID uuid;
    private List<Permission> permissions;

    public Manager(UUID uuid, List<Permission> permissions) {
        this.uuid = uuid;
        this.permissions = permissions;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public void setPermission(Permission permission, boolean status) {
        if (status) {
            getPermissions().add(permission);
        } else {
            getPermissions().remove(permission);
        }
    }
}