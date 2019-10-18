package com.rxh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/12/5
 * Time: 10:45
 * Project: Management
 * Package: com.rxh.utils
 */
public class EncryptionUtils {
    private final static Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

    /**
     * DESede_ECB_PKCS5Padding TripleDES加密，ECB算法，PKCS5Padding填充（同PKCS7Padding，Java不存在PKCS7Padding填充）
     *
     * @param data 需加密数据
     * @param key  密钥
     * @return 加密数据
     */
    public static String DESede_ECB_PKCS5Padding(String data, String key) {
        if (data != null && key != null) {
            try {
                SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.US_ASCII), "DESede");
                Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] doFinal = cipher.doFinal(data.getBytes());
                return new String(Base64.getEncoder().encode(doFinal));
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                logger.error("DESede/ECB/PKCS5Padding加密失败！", e);
            }
        }
        return null;
    }
}