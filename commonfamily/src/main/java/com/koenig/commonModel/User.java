package com.koenig.commonModel;

import org.joda.time.DateTime;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class User extends Item {
    private String name;
    private String family;
    private DateTime birthday;
    private Map<Component, Permission> permissions;

    public User() {
        super();
    }

    public User(String id, String name, String family, DateTime birthday, Map<Component, Permission> permissions) {
        super(id);
        this.name = name;
        this.family = family;
        this.birthday = birthday;
        this.permissions = permissions;
    }

    public User(String name, String family, DateTime birthday, Map<Component, Permission> permissions) {
        super();
        this.name = name;
        this.family = family;
        this.birthday = birthday;
        this.permissions = permissions;
    }

    public User(String name, String family, DateTime birthday) {
        super();
        this.name = name;
        this.family = family;
        this.birthday = birthday;
        permissions = Permission.CreateNonePermissions();
    }

    public User(String id, String name, String family, DateTime birthday) {
        super(id);
        this.name = name;
        this.family = family;
        this.birthday = birthday;
        permissions = Permission.CreateNonePermissions();
    }

    public User(ByteBuffer buffer) {
        super(buffer);
        name = byteToString(buffer);
        family = byteToString(buffer);
        birthday = byteToDateTime(buffer);
        permissions = bytesToPermissions(buffer);
    }

    public static byte[] permissionsToBytes(Map<Component, Permission> permissions) {
        ByteBuffer buffer = ByteBuffer.allocate(getPermissionLength(permissions));
        buffer.putShort((short) permissions.size());
        for (Map.Entry<Component, Permission> entry : permissions.entrySet()) {
            Component component = entry.getKey();
            Permission permission = entry.getValue();
            buffer.put(component.toBytes());
            permission.write(buffer);
        }

        return buffer.array();
    }

    public static HashMap<Component, Permission> bytesToPermissions(ByteBuffer buffer) {
        short size = buffer.getShort();
        HashMap<Component, Permission> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Component component = Component.read(buffer);
            Permission permission = Permission.read(buffer);
            result.put(component, permission);
        }

        return result;
    }

    private static short getPermissionLength(Map<Component, Permission> permissions) {
        short size = 2;
        for (Map.Entry<Component, Permission> entry : permissions.entrySet()) {
            Component component = entry.getKey();
            Permission permission = entry.getValue();
            size += component.toString().length() + 2;
            size += permission.toString().length() + 2;
        }
        return size;
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

    @Override
    public int getByteLength() {
        return super.getByteLength() + getStringLength(name) + getStringLength(family) + getDateLength() + getPermissionLength(permissions);
    }

    public void writeBytes(ByteBuffer buffer) {
        super.writeBytes(buffer);
        buffer.put(stringToByte(name));
        buffer.put(stringToByte(family));
        buffer.put(dateTimeToBytes(birthday));
        buffer.put(permissionsToBytes(permissions));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
