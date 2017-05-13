package com.lhcx.utils;

import java.security.MessageDigest;

/**
 * MD5加密
 * @author robot
 *
 */
public class MD5Kit {
    /**
     * MD5加密
     * @param src
     * @return
     * 用法：根据手机号+时间戳 生成唯一身份指令
     */
    public static String encode(String src) {
        if (src == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            for (byte b : md.digest())
                sb.append(Integer.toString(b >>> 4 & 0xF, 16)).append(Integer.toString(b & 0xF, 16));
        } catch (Exception e) {

        }
        return sb.toString();
    }
}
