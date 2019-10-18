package com.rxh.utils;

import com.rxh.exception.PayException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/23
 * Time: 11:32
 * Project: Management
 * Package: com.rxh.utils
 */
public class RSAUtils {
    private String publicKeyPath;
    private String privateKeyPath;
    // 私钥
    private RSAPrivateKey privateKey;
    // 公钥
    private RSAPublicKey publicKey;

    private KeyFactory keyFactory;

    private final Integer rsaVersion;

    private final GlobalConfiguration globalConfiguration;

    public RSAUtils(String publicKeyPath, String privateKeyPath, GlobalConfiguration globalConfiguration) throws InvalidKeySpecException {
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (globalConfiguration.isProduction()) {
            rsaVersion = getRsaVersion(publicKeyPath);
            loadPublicKeyByPath(publicKeyPath);
            loadPrivateKeyByPath(privateKeyPath);
        } else {
            rsaVersion = 0;
        }
        this.globalConfiguration = globalConfiguration;
        this.publicKeyPath = publicKeyPath;
        this.privateKeyPath = privateKeyPath;
    }

    public String encrypt(String data) throws PayException {
        if (!globalConfiguration.isProduction()) {
            return data;
        }
        try {
            if (publicKey == null && StringUtils.isNotBlank(publicKeyPath)) {
                loadPublicKeyByPath(publicKeyPath);
            }
            return Base64.getEncoder().encodeToString(cipherDoFinal(publicKey, Cipher.ENCRYPT_MODE, data.getBytes()));
        } catch (InvalidKeySpecException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            PayException payException = new PayException("", 123);
            payException.setStackTrace(e.getStackTrace());
            throw payException;
        }
    }

    public String decrypt(String data) throws PayException {
        if (!globalConfiguration.isProduction()) {
            return data;
        }
        try {
            if (privateKey == null && StringUtils.isNotBlank(privateKeyPath)) {
                loadPrivateKeyByPath(privateKeyPath);
            }
            return new String(cipherDoFinal(privateKey, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data)));
        } catch (InvalidKeySpecException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            PayException payException = new PayException("", 123);
            payException.setStackTrace(e.getStackTrace());
            throw payException;
        }
    }

    public void loadPrivateKeyByPath(String path) throws InvalidKeySpecException {
        byte[] keyByte = getKeyByte(path);
        privateKey = (RSAPrivateKey) keyFactory.generatePrivate(getPrivateKeySpec(keyByte));
    }

    public void loadPublicKeyByPath(String path) throws InvalidKeySpecException {
        byte[] keyByte = getKeyByte(path);
        publicKey = (RSAPublicKey) keyFactory.generatePublic(getPublicKeySpec(keyByte));
    }

    private byte[] getKeyByte(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuffer sb = new StringBuffer();
            Stream<String> lines = br.lines();
            lines.forEach(s -> {
                if (!s.startsWith("-")) {
                    sb.append(s);
                }
            });
            return Base64.getDecoder().decode(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PKCS8EncodedKeySpec getPrivateKeySpec(byte[] keyByte) {
        return new PKCS8EncodedKeySpec(keyByte);
    }

    private X509EncodedKeySpec getPublicKeySpec(byte[] keyByte) {
        return new X509EncodedKeySpec(keyByte);
    }

    private byte[] cipherDoFinal(Key key, int type, byte[] byteData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(type, key);
        return cipher.doFinal(byteData);
    }

    public Integer getRsaVersion(String publicKeyPath) {
        File file = new File(publicKeyPath);
        return Integer.valueOf(new SimpleDateFormat("yyMMdd").format(new Date(file.lastModified())));
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public Integer getRsaVersion() {
        return rsaVersion;
    }
}
