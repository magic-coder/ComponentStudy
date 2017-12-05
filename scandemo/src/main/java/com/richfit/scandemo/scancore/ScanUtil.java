package com.richfit.scandemo.scancore;

import java.io.ByteArrayOutputStream;

/**
 * Created by monday on 2017/11/23.
 */

public class ScanUtil {
    //解码中文
    private static final String HEXSTRING = "0123456789ABCDEF";
    /**
     * 条码内容加密
     *
     * @param char_in
     * @return
     */
    public static String CharTrans(String char_in) {
        String char_out = "";
        int char_length = 0;

        char_length = char_in.length();

        int flg_mod = char_length % 2;
        for (int i = 0; i < char_length - 1; i += 2) {
            char_out = char_out + char_in.substring(i + 1, i + 2);
            char_out = char_out + char_in.substring(i, i + 1);
        }

        if (flg_mod != 0) {
            char_out = char_out + char_in.substring(char_length - 1);
        }
        return char_out;
    }


    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    private static String decodeForChinese(String bytes) {
        String str = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((HEXSTRING.indexOf(bytes.charAt(i)) << 4 | HEXSTRING
                    .indexOf(bytes.charAt(i + 1))));
        try {
            str = new String(baos.toByteArray(), "GB2312");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
