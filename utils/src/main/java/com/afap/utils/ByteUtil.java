package com.afap.utils;

import java.nio.ByteBuffer;

public class ByteUtil {

    private static final String HEXES = "0123456789ABCDEF";

    private ByteUtil() {
    }

    /**
     * 字节数组转换成16进制的字符串表示: [01, 30, FF, AA]
     *
     * @param array 字节数组
     * @return 16进制字符转
     */
    public static String byteArrayToHexString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        boolean firstEntry = true;
        sb.append('[');

        for (final byte b : array) {
            if (!firstEntry) {
                sb.append(", ");
            }
            sb.append(HEXES.charAt((b & 0xF0) >> 4));
            sb.append(HEXES.charAt((b & 0x0F)));
            firstEntry = false;
        }

        sb.append(']');
        return sb.toString();
    }

    /**
     * 检查一个字节数组是否以另一个字节数组为开始
     *
     * @param array  the array
     * @param prefix the prefix
     * @return boolean
     */
    public static boolean doesArrayBeginWith(final byte[] array, final byte[] prefix) {
        if (array.length < prefix.length) {
            return false;
        }

        for (int i = 0; i < prefix.length; i++) {
            if (array[i] != prefix[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * 将一个长度为2的字节数组转为int
     *
     * @param input the input
     * @return the int from the array
     */
    public static int getIntFrom2ByteArray(final byte[] input) {
        final byte[] result = new byte[4];

        result[0] = 0;
        result[1] = 0;
        result[2] = input[0];
        result[3] = input[1];

        return ByteUtil.getIntFromByteArray(result);
    }

    /**
     * Converts a byte to an int, preserving the sign.
     * For example, FF will be converted to 255 and not -1.
     *
     * @param bite the byte
     * @return the int from byte
     */
    public static int getIntFromByte(final byte bite) {
        return bite & 0xFF;
    }

    /**
     * Converts a byte array to an int.
     *
     * @param bytes the bytes
     * @return the int from byte array
     */
    public static int getIntFromByteArray(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * Converts a byte array to a long.
     *
     * @param bytes the bytes
     * @return the long from byte array
     */
    public static long getLongFromByteArray(final byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }


    /**
     * Inverts an byte array in place.
     *
     * @param array the array
     */
    public static void invertArray(final byte[] array) {
        final int size = array.length;
        byte temp;

        for (int i = 0; i < size / 2; i++) {
            temp = array[i];
            array[i] = array[size - 1 - i];
            array[size - 1 - i] = temp;
        }
    }
}
