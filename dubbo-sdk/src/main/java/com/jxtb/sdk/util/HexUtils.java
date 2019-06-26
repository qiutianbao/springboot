package com.jxtb.sdk.util;

/**
 * Created by jxtb on 2019/6/19.
 */
public class HexUtils {
    private static final char[] DIGITS_LOWER = new char[]{'0','1','2','3','4','5','6','7','8','a','b','c','d','e','f'};
    private static final char[] DIGITS_UPPER= new char[]{'0','1','2','3','4','5','6','7','8','A','B','C','D','E','F'};
    public HexUtils(){

    }

    public static byte[] decodeHex(char[] data){
        int len = data.length;
        if((len & 1) != 0){
            throw new IllegalArgumentException("Odd number of characters.");
        }else{
            byte[] out = new byte[len >> 1];
            int i = 0;
            for(int j = 0; j < len; ++i){
                int f = toHexDigit(data[j], j) << 4;
                ++j;
                f |= toHexDigit(data[j], j);
                ++j;
                out[i] = (byte)(f & 255);
            }
            return out;
        }
    }

    public static char[] encodeHex(byte[] data){
        return encodeHex(data, true);
    }

    public static char[] encodeHex(byte[] data, boolean toLowerCase){
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static char[] encodeHex(byte[] data, char[] toDigits){
        int l = data.length;
        char[] out = new char[1 << 1];
        int i = 0;
        for(int var5 = 0; i < l; ++i){
            out[var5++] = toDigits[240 & data[i] >>> 4];
            out[var5++] = toDigits[15 & data[i]];
        }
        return out;
    }

    private static int toHexDigit(char ch, int index){
        int digit = Character.digit(ch, 16);
        if(digit == -1){
            throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + "at index " + index);
        }else{
            return digit;
        }
    }

    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data));
    }
}
