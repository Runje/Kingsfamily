package com.koenig.commonModel;


import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

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
        try {
            ByteBuffer buffer = ByteBuffer.allocate(2 + string.length());
            if (string.length() > Short.MAX_VALUE) {
                throw new RuntimeException("String longer than Short.Max_value");
            }
            buffer.putShort((short) string.length());
            buffer.put(string.getBytes(encoding));
            return buffer.array();
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            return null;
        }
    }

    public static String byteToString(ByteBuffer buffer) {
        short length = buffer.getShort();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, Charset.forName(encoding)).trim();
    }

    public static byte booleanToByte(boolean b) {
        return (byte) (b ? 1 : 0);
    }

    public static boolean byteToBoolean(Byte b) {
        return b == (byte) 1;
    }

    public static DateTime byteToDateTime(ByteBuffer buffer) {
        return new DateTime(buffer.getLong());
    }

    public static int getDateLength() {
        return 8;
    }

    public static byte[] dateTimeToBytes(DateTime dateTime) {
        ByteBuffer buffer = ByteBuffer.allocate(getDateLength());
        buffer.putLong(dateTime.getMillis());
        return buffer.array();
    }

    public abstract int getByteLength();

    public final byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getByteLength());
        writeBytes(buffer);
        return buffer.array();
    }

    public abstract void writeBytes(ByteBuffer buffer);

}
