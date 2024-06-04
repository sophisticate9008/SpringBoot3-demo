package com.wzy.demo.common;


import java.security.SecureRandom;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.util.ByteSource;


public class PasswordUtils {



    // 生成随机盐值
    public static String generateRandomSalt() {
        // 使用 SecureRandom 生成随机盐值
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        // 将随机盐值转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 对密码进行加密
    public static String hashPassword(String password, String salt) {
        return (new SimpleHash(Constast.AlgorithmName, (Object) password, ByteSource.Util.bytes(salt), Constast.HASHITERATIONS)).toHex();
    }
}
