package com.koenig.commonModel;

import org.joda.time.DateTime;

import java.util.Map;

public class User {
    private String name;
    private String family;
    private DateTime birthday;
    private Map<Component, Permission> permissions;

    public User() {
    }

    public User(String name, String family, DateTime birthday, Map<Component, Permission> permissions) {
        this.name = name;
        this.family = family;
        this.birthday = birthday;
        this.permissions = permissions;
    }

    public User(String name, String family, DateTime birthday) {
        this.name = name;
        this.family = family;
        this.birthday = birthday;
        permissions = Permission.CreateNonePermissions();
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public Permission getPermission(Component component) {
        return permissions.get(component);
    }

    public Permission setPermission(Component component, Permission permission) {
        return permissions.put(component, permission);
    }

    public Map<Component, Permission> getPermissions() {
        return permissions;
    }
}
