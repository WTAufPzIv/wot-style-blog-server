package com.example.blog.common.utils;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;

@Slf4j
public class RsaUtils {
    private static final String RSA_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String BLOCK_SEPARATOR = Pattern.quote("|::|");

    // 加载私钥
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        String cleanKey = privateKeyStr
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
        byte[] keyBytes = Base64.getDecoder().decode(cleanKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    // RSA解密
    public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] data = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(data));
    }
}
