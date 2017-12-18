package com.koenig.commonModel;


import com.koenig.commonModel.finance.Expenses;
import com.koenig.commonModel.finance.StandingOrder;

import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Thomas on 28.10.2017.
 */

public abstract class Byteable {
    public static final String encoding = "ISO-8859-1";
    public static int stringLengthSize = 2;

    public static int getStringLength(String name) {
        return name.length() + stringLengthSize;
    }

    public static byte[] stringToByte(String string) {
        ByteBuffer buffer = ByteBuffer.allocate(2 + string.length());
        writeString(string, buffer);
        return buffer.array();
    }

    public static void writeString(String string, ByteBuffer buffer) {
        try {
            if (string.length() > Short.MAX_VALUE) {
                throw new RuntimeException("String longer than Short.Max_value");
            }
            buffer.putShort((short) string.length());
            buffer.put(string.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String byteToString(ByteBuffer buffer) {
        short length = buffer.getShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, Charset.forName(encoding)).trim();
    }

    public static Item byteToItem(ByteBuffer buffer) {
        ItemType className = Byteable.byteToEnum(buffer, ItemType.class);
        switch (className) {
            case EXPENSES:
                return new Expenses(buffer);
            case CATEGORY:
                return new Category(buffer);
            case FAMILY:
                return new Family(buffer);
            case OPERATION:
                return new Operation(buffer);
            case STANDING_ORDER:
                return new StandingOrder(buffer);
            case USER:
                return new User(buffer);
        }

        throw new RuntimeException("Unsupported item: " + className.toString());
    }

    public static void writeBool(boolean b, ByteBuffer buffer) {
        buffer.put(booleanToByte(b));
    }

    public static byte booleanToByte(boolean b) {
        return (byte) (b ? 1 : 0);
    }

    public static boolean byteToBoolean(ByteBuffer buffer) {
        return byteToBoolean(buffer.get());
    }

    public static boolean byteToBoolean(Byte b) {
        return b == (byte) 1;
    }

    public static DateTime byteToDateTime(ByteBuffer buffer) {
        return new DateTime(buffer.getLong());
    }

    public static void writeDateTime(DateTime dateTime, ByteBuffer buffer) {
        buffer.put(dateTimeToBytes(dateTime));
    }

    public static int getDateLength() {
        return 8;
    }

    public static byte[] dateTimeToBytes(DateTime dateTime) {
        ByteBuffer buffer = ByteBuffer.allocate(getDateLength());
        buffer.putLong(dateTime.getMillis());
        return buffer.array();
    }

    public static int getEnumLength(Enum operator) {
        return getStringLength(operator.name());
    }

    public static void writeEnum(Enum e, ByteBuffer buffer) {
        writeString(e.name(), buffer);
    }

    public static <T extends Enum<T>> T byteToEnum(ByteBuffer buffer, Class<T> enumClass) {
        return Enum.valueOf(enumClass, byteToString(buffer));
    }

    public static int getItemLength(Item item) {
        return Byteable.getEnumLength(ItemType.fromItem(item)) + item.getByteLength();
    }

    public static <T extends Item> void writeItem(T item, ByteBuffer buffer) {
        writeEnum(ItemType.fromItem(item), buffer);
        item.writeBytes(buffer);
    }

    public static void writeList(List<? extends Byteable> byteables, ByteBuffer buffer) {
        buffer.putShort((short) byteables.size());
        for (Byteable byteable : byteables) {
            byteable.writeBytes(buffer);
        }
    }

    public static int getListLength(List<? extends Byteable> byteables) {
        int size = 2;
        for (Byteable byteable : byteables) {
            size += byteable.getByteLength();
        }

        return size;
    }

    public int getBoolLength() {
        return 1;
    }

    public abstract int getByteLength();

    public final byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteLength());
        writeBytes(buffer);
        return buffer.array();
    }

    public abstract void writeBytes(ByteBuffer buffer);
}
