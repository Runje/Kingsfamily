package com.koenig;


import com.koenig.commonModel.Component;
import com.koenig.commonModel.Permission;
import com.koenig.commonModel.User;

import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 14.02.2015.
 */
public class BinaryConverter
{
    public static final int sizeLength = 4;
    public static final String encoding = "ISO-8859-1";
    public static final int idLength = 36;
    public static final int dateTimeLength = 8;
    public static int stringLengthSize = 2;

    public static byte[] stringToByte(String string)
    {
        try
        {
            ByteBuffer buffer = ByteBuffer.allocate(2 + string.length());
            if (string.length() > Short.MAX_VALUE)
            {
                throw new RuntimeException("String longer than Short.Max_value");
            }
            buffer.putShort((short) string.length());
            buffer.put(string.getBytes(encoding));
            return buffer.array();
        } catch (UnsupportedEncodingException e)
        {

            e.printStackTrace();
            return null;
        }
    }

    public static String byteToString(ByteBuffer buffer)
    {
        short length = buffer.getShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, Charset.forName(encoding)).trim();
    }



    public static byte booleanToByte(boolean b)
    {
        return (byte) (b ? 1 : 0);
    }

    public static boolean byteToBoolean(Byte b)
    {
        return b == (byte) 1;
    }

    public static byte[] permissionsToBytes(Map<Component, Permission> permissions) {

        short size = getPermissionLength(permissions);
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putShort((short) permissions.size());
        for (Map.Entry<Component, Permission> entry : permissions.entrySet()) {
            Component component = entry.getKey();
            Permission permission = entry.getValue();
            buffer.put(componentToBytes(component));
            buffer.put(permissionToBytes(permission));
        }

        return buffer.array();
    }

    public static HashMap<Component, Permission> bytesToPermissions(ByteBuffer buffer) {
        short size = buffer.getShort();
        HashMap<Component, Permission> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Component component = bytesToComponent(buffer);
            Permission permission = bytesToPermission(buffer);
            result.put(component, permission);
        }

        return result;
    }


    public static byte[] componentToBytes(Component component) {
        return stringToByte(component.toString());
    }

    public static Component bytesToComponent(ByteBuffer buffer) {
        return Component.valueOf(byteToString(buffer));
    }

    public static byte[] permissionToBytes(Permission permission) {
        return stringToByte(permission.toString());
    }

    public static Permission bytesToPermission(ByteBuffer buffer) {
        return Permission.valueOf(byteToString(buffer));
    }

    public static DateTime byteToDateTime(ByteBuffer buffer) {
        return new DateTime(buffer.getLong());
    }

    public static byte[] dateTimeToBytes(DateTime dateTime) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(dateTime.getMillis());
        return buffer.array();
    }

    public static User byteToUser(ByteBuffer buffer) {
        String name = byteToString(buffer);
        String family = byteToString(buffer);
        String id = byteToString(buffer);
        DateTime birthday = byteToDateTime(buffer);
        HashMap<Component, Permission> permission = bytesToPermissions(buffer);
        return new User(id, name, family, birthday, permission);
    }

    public static byte[] userToBytes(User user) {
        int size = getUserLength(user);
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(stringToByte(user.getName()));
        buffer.put(stringToByte(user.getFamily()));
        buffer.put(stringToByte(user.getId()));
        buffer.put(dateTimeToBytes(user.getBirthday()));
        buffer.put(permissionsToBytes(user.getPermissions()));
        return buffer.array();
    }

    public static int getUserLength(User user) {
        return getStringLength(user.getName()) + getStringLength(user.getFamily()) + getStringLength(user.getId()) + dateTimeLength + getPermissionLength(user.getPermissions());
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

    private static int getStringLength(String name) {
        return name.length() + stringLengthSize;
    }
}
