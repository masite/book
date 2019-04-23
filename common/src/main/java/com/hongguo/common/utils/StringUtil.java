package com.hongguo.common.utils;

/**
 * Created by Administrator on 2016/8/17.
 */
public class StringUtil {

    /**
     * 字符串转unicode
     * @param src
     * @return
     */
    public static String String2Unicode(String src) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            String hex = Integer.toHexString(c);
            unicode.append("\\u");
            for(int j = hex.length(); j < 4; j++){
                unicode.append("0");
            }
            unicode.append(hex);
        }
        return unicode.toString();
    }

    public static String unicode2String(String src) {

        StringBuffer retBuf = new StringBuffer();
        int maxLoop = src.length();
        for (int i = 0; i < maxLoop; i++) {
            if (src.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((src.charAt(i + 1) == 'u') || (src.charAt(i + 1) == 'U'))) {
                    try {
                        retBuf.append((char) Integer.parseInt(src.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(src.charAt(i));
                    }
                } else {
                    retBuf.append(src.charAt(i));
                }
            } else {
                retBuf.append(src.charAt(i));
            }
        }
        return retBuf.toString();
    }
}
