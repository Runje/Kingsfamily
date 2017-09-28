package com.koenig.communication;



import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Thomas on 14.02.2015.
 */
public class MessageConverter
{
    public static final int sizeLength = 4;
    public static final String encoding = "ISO-8859-1";
    public static final int idLength = 36;
    private static int DatabaseItemLength = 4 + 4 + 72 + 16 + 1;
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

   }
