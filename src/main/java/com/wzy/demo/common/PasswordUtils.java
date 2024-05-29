package com.wzy.demo.common;


import java.security.SecureRandom;

import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;
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

    // 使用 MD5 哈希算法对密码进行加密
    private static HashService createHashService() {
        DefaultHashService hashService = new DefaultHashService();
        hashService.setDefaultAlgorithmName("MD5");
        return hashService;
    }

    // 使用 HashService 对密码进行加密
    public static String hashPassword(String password, String salt) {
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
        HashService hashService = createHashService();
        HashRequest request = new HashRequest.Builder()
                .setSource(password)
                .setSalt(credentialsSalt)
                .build();
        return hashService.computeHash(request).toHex();
    }
}
