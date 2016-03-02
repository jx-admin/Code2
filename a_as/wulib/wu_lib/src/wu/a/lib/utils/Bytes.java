package wu.a.lib.utils;

import android.annotation.SuppressLint;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

// NOT tested
// http://grepcode.com/file/repository.cloudera.com/content/repositories/releases/com.cloudera.hbase/hbase/0.89.20100924-28/org/apache/hadoop/hbase/util/Bytes.java#Bytes.0SIZEOF_LONG
public final class Bytes {
    private final static String TAG = "Bytes";

    public static final int SIZE_OF_BOOLEAN = Byte.SIZE / Byte.SIZE;
    public static final int SIZE_OF_BYTE = SIZE_OF_BOOLEAN;
    public static final int SIZE_OF_CHAR = Character.SIZE / Byte.SIZE;
    public static final int SIZE_OF_INT = Integer.SIZE / Byte.SIZE;
    public static final int SIZE_OF_LONG = Long.SIZE / Byte.SIZE;
    public static final int SIZE_OF_SHORT = Short.SIZE / Byte.SIZE;
    public static final int SIZE_OF_DOUBLE = Double.SIZE / Byte.SIZE;
    public static final int SIZE_OF_FLOAT = Float.SIZE / Byte.SIZE;

    /**
     * Put bytes at the specified byte array position.
     * @param dst the byte array
     * @param dstOffset position in the array
     * @param src array to write out
     * @param srcOffset source offset
     * @param srcLength source length
     * @return incremented offset
     */
    public static int putBytes(byte[] dst
            , int dstOffset, byte[] src, int srcOffset, int srcLength) {
        System.arraycopy(src, srcOffset, dst, dstOffset, srcLength);
        return dstOffset + srcLength;
    }

    /**
     * Write a single byte out to the specified byte array position.
     * @param bytes the byte array
     * @param offset position in the array
     * @param b byte to write out
     * @return incremented offset
     */
    public static int putByte(byte[] bytes, int offset, byte b) {
        bytes[offset] = b;
        return offset + 1;
    }

    /**
     * Returns a new byte array, copied from the passed ByteBuffer.
     * @param bb A ByteBuffer
     * @return the byte array
     */
    public static byte[] toBytes(ByteBuffer bb) {
        int length = bb.limit();
        byte[] result = new byte[length];
        System.arraycopy(bb.array(), bb.arrayOffset(), result, 0, length);
        return result;
    }

    /**
     * Convert a byte array to a String.
     * @param b Presumed UTF-8 encoded byte array
     * @return String made from the byte array
     */
    public static String toString(final byte[] b) {
        if (b == null) {
            return null;
        }
        return toString(b, 0, b.length);
    }

    /**
     * Joins two byte arrays together using a separator.
     * @param b1 the first byte array
     * @param separator the separator to use
     * @param b2 the second byte array
     * @return the joined array
     */
    public static String toString(final byte[] b1, String separator, final byte[] b2) {
        return toString(b1, 0, b1.length) + separator + toString(b2, 0, b2.length);
    }

    /**
     * Convert utf8 encoded bytes into a string. If an UnsupportedEncodingException occurs,
     * this method will eat it and return null instead.
     * @param b presumed UTF-8 encoded byte array
     * @param off offset into array
     * @param len length of the UTF-8 sequence
     * @return String made from the byte array or null
     */
    @SuppressLint("NewApi")
	public static String toString(final byte [] b, int off, int len) {
        if (null == b) {
            return null;
        }
        if (0 == len) {
            return "";
        }
        return new String(b, off, len, Charset.forName("UTF-8"));
    }

    public static String toHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        /*StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();*/
        BigInteger bigInt = new BigInteger(1, bytes);
        String output = bigInt.toString(16);
        // Fill to 32 chars
        output = String.format("%32s", output).replace(' ', '0');
        return output;
    }

    /**
     * Convert a String to a UTF-8 byte array.
     * @param s String
     * @return the byte array
     */
    @SuppressLint("NewApi")
	public static byte[] toBytes(String s) {
        return s.getBytes(Charset.forName("UTF-8"));
    }

    /**
     * Convert a boolean to a byte array. True becomes -1 and false becomes 0.
     * @param b value
     * @return a byte array
     */
    public static byte[] toBytes(final boolean b) {
        return new byte[] { b ? (byte) -1 : (byte) 0 };
    }

    /**
     * Convert a byte array to a boolean value.
     * @param b the byte array
     * @return
     */
    public static boolean toBoolean(final byte[] b) {
        if (1 != b.length) {
            throw new IllegalArgumentException("Array has wrong size: " + b.length);
        }
        return b[0] != (byte) 0;
    }

    /**
     * Convert a long value to a byte array
     * @param x the long value to be converted
     * @return the byte array
     */
    public static byte[] toBytes(long x) {
        byte[] b = new byte[8];
        for (int i = 7; i > 0; i--) {
            b[i] = (byte) x;
            x >>>= 8;
        }
        b[0] = (byte) x;
        return b;
    }

    public static long toLong(byte[] bytes) {
        return toLong(bytes, 0, SIZE_OF_LONG);
    }

    public static long toLong(byte[] bytes, int offset) {
        return toLong(bytes, offset, SIZE_OF_LONG);
    }

    /**
     * Converts a byte array to a long value.
     * @param bytes
     * @param offset
     * @param length
     * @return
     * @throws java.lang.IllegalArgumentException if length is not SIZE_OF_LONG
     *         or if there's not enough room in the array at the offset indicated.
     */
    public static long toLong(byte[] bytes, int offset, final int length) {
        int N = offset + length;
        if (SIZE_OF_LONG != bytes.length || N > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZE_OF_LONG);
        }
        long l = 0;
        for (int i = offset; i < N; i++) {
            l <<= 8;
            l ^= bytes[i] & 0xFF;
        }
        return l;
    }

    private static IllegalArgumentException explainWrongLengthOrOffset(final byte[] bytes
            , final int offset, final int length, final int expectedLength) {
        String reason = length != expectedLength
                ? "Wrong length: " + length + ", expected " + expectedLength
                : "Offset (" + offset + ") + length (" + length + ") exceeded the "
                + "capacity of the array: " + bytes.length;
        return new IllegalArgumentException(reason);
    }

    /**
     * Convert an int value to a byte array
     * @param x the int value to be converted
     * @return the byte array
     */
    public static byte[] toBytes(int x) {
        byte[] b = new byte[4];
        for (int i = 3; i > 0; i--) {
            b[i] = (byte) x;
            x >>>= 8;
        }
        b[0] = (byte) x;
        return b;
    }

    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0, SIZE_OF_INT);
    }

    public static int toInt(byte[] bytes, int offset) {
        return toInt(bytes, offset, SIZE_OF_INT);
    }

    /**
     * Converts a byte array to a int value.
     * @param bytes
     * @param offset
     * @param length
     * @return
     * @throws java.lang.IllegalArgumentException if length is not SIZE_OF_INT
     *         or if there's not enough room in the array at the offset indicated.
     */
    public static int toInt(byte[] bytes, int offset, final int length) {
        int N = offset + length;
        if (SIZE_OF_INT != length || N > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZE_OF_INT);
        }

        if (true) {
            String t = toString(bytes, offset, length);
            return Integer.parseInt(t);
        }
        int n = 0;
        for (int i = 0; i < SIZE_OF_INT; i++) {
            int shift = (4 - 1 - i) << 3;
            n += (bytes[i] & 0xFF) << shift;
        }
        return n;
    }
}
