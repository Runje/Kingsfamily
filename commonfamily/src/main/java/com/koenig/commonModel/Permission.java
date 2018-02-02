package com.koenig.commonModel;

import java.nio.ByteBuffer;
import java.util.HashMap;

public enum Permission {
    WRITE, READ, READ_AND_WRITE, NONE;

    public static HashMap<Component, Permission> CreateNonePermissions() {
        Component[] allComponents = Component.values();
        HashMap<Component, Permission> permissions = new HashMap<>(allComponents.length);
        for (Component component : allComponents) {
            permissions.put(component, Permission.NONE);
        }

        return permissions;
    }

    public static Permission read(ByteBuffer buffer) {
        return Permission.valueOf(Byteable.Companion.byteToString(buffer));
    }

    public void write(ByteBuffer buffer) {
        buffer.put(Byteable.Companion.stringToByte(name()));
    }

}
