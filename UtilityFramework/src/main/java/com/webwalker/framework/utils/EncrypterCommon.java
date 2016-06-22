package com.webwalker.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

/**
 * 对称加密算法用来对敏感数据等信息进行加密，常用的算法包括：
 *
 * DES（Data Encryption Standard）：数据加密标准，速度较快，适用于加密大量数据的场合。
 *
 * 3DES（Triple DES）：是基于DES，对一块数据用三个不同的密钥进行三次加密，强度更高。
 *
 * AES（Advanced Encryption Standard）：高级加密标准，是下一代的加密算法标准，速度快，安全级别高；
 *
 *
 * 常见的非对称加密算法如下：
 *
 * RSA：由 RSA 公司发明，是一个支持变长密钥的公共密钥算法，需要加密的文件块的长度也是可变的；
 *
 * DSA（Digital Signature Algorithm）：数字签名算法，是一种标准的 DSS（数字签名标准）；
 *
 * ECC（Elliptic Curves Cryptography）：椭圆曲线密码编码学。
 *
 */
public class EncrypterCommon {
    private static final String TAG = "spm.CipherUtil";

    private static final String ENCODING_UTF8 = "UTF-8";

    /**
     * 静态Context取Android上下文
     */
    private static Context context;

    /**
     * 密钥字符串获取
     */
    private static String secretKey = "S#$%&*PM";

    /**
     * KeyGenerator 在 DSA 中已经说明 , 在添加 JCE 后在 instance 进可以如下参数
     * DES,DESede,Blowfish[,HmacMD5,HmacSHA1:需要提供Provider]，默认3DES算法
     */
    private static String encryptAlgorithm = "DESede";

    /**
     * 保护构造子，由于是Util类，不需要创建实例，所有方法Static静态调用
     */
    private EncrypterCommon() {
    }

    /**
     * 初始化Android上下文（调用前必须先要初始化）
     *
     * @param context
     */
    public static void initCipher(Context ctx) {
        // 初始化Context
        context = ctx;

        if (context != null) {
            // 获取手机管理服务
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            // IMEI:手机串号
            secretKey = tm.getDeviceId();
            if (StringUtil.isBlank(secretKey)) {
                secretKey = android.os.Build.BRAND;
            }
            if (StringUtil.isBlank(secretKey)) {
                secretKey = "CipherUtil";
            }
        }
    }

    public static String aesEncrypt(String content, String password)
            throws Exception {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);// 加密
            return new String(Base64.encode(result, Base64.DEFAULT), "utf-8");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 对原始字符串进行加密：明文字符串 --》 密文字节流 --》16进制字符串
     *
     * @param rawPass
     *            - 原始明文字符串
     * @return - 十六进制的加密后的密文
     */
    public static String encrypt(String rawPass) {
        if (rawPass == null || (rawPass != null && rawPass.length() == 0)) {
            Log.w(TAG, "待加密的原始字符串为空！");
            return "";
        }

        // 加密密码
        byte[] encryptPass;
        try {
            encryptPass = encryptBytes(rawPass.getBytes(ENCODING_UTF8));
        } catch (Exception e) {
            // throw new RuntimeException("加密密码发生异常：【" + e.getMessage() + "】");
            Log.e(TAG, "加密字符串发生异常：【" + e.getMessage() + "】");
            return "";
        }

        // 对加密后的字节流转换为十六进制字符串
        return byte2hex(encryptPass);
    }

    /**
     * 对加密后的密文进行解密：16进制密文字符串 --》密文字节流 --》原始字符串
     *
     * @param encPass
     *            - 加密后的密文
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String encPass) {
        if (encPass == null || (encPass != null && encPass.length() == 0)) {
            Log.w(TAG, "待解密的密文字符串为空！");
            return "";
        }

        // 16进制的字符串转换为密文字节流
        byte[] unwrapperEncPass = hex2byte(encPass);

        // 解密密文字节流
        byte[] rawPass;
        try {
            rawPass = decryptBytes(unwrapperEncPass);
        } catch (Exception e) {
            // throw new RuntimeException("解密密码发生异常：【" + e.getMessage() + "】");
            Log.i(TAG, "解密字符串发生异常：【" + e.getMessage() + "】");
            return "";
        }

        // 字节流转换成字符串
        return new String(rawPass);
    }

    /**
     * 生成密钥KEY
     *
     * KeyGenerator 在 DSA 中已经说明 , 在添加 JCE 后在 instance 进可以如下参数
     * DES,DESede,Blowfish,HmacMD5,HmacSHA1
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    private static Key getSecretKey() throws NoSuchAlgorithmException,
            UnsupportedEncodingException, InvalidKeyException {
        // 判断密钥有没初始化
        if (secretKey == null
                || (secretKey != null && secretKey.trim().equals(""))) {
            throw new InvalidKeyException("请初始化加密器initCipher！");
        }

        // 密钥用当前手机串号IMEI
        KeyGenerator keyGen = KeyGenerator.getInstance(encryptAlgorithm);
        keyGen.init(new SecureRandom(secretKey.getBytes(ENCODING_UTF8)));

        // 生成密钥
        Key key = keyGen.generateKey();

        return key;
    }

    /**
     * 对明文密码流加密
     *
     * @param rawBytes
     *            - 原始待加密的字节数组
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static byte[] encryptBytes(byte[] rawBytes)
            throws InvalidKeyException, NoSuchAlgorithmException,
            UnsupportedEncodingException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        // 获取密钥
        Key key = getSecretKey();

        // 加密字节数组
        Cipher cipher = Cipher.getInstance(encryptAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(rawBytes);
    }

    /**
     * 对加密的字节流信息进行解密
     *
     * @param enBytes
     *            - 加密字节流
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static byte[] decryptBytes(byte[] enBytes)
            throws InvalidKeyException, NoSuchAlgorithmException,
            UnsupportedEncodingException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        // 获取密钥
        Key key = getSecretKey();

        // 解密字节数组
        Cipher cipher = Cipher.getInstance(encryptAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(enBytes);
    }

    // ========================
    // 十六进制辅助Helper方法

    private static final String HEX_CHARS = "0123456789ABCDEF";

    /**
     * 字符串转换成十六进制值
     *
     * @param bin
     *            String 我们看到的要转换成十六进制的字符串
     * @return
     */
    private static String byte2hex(byte[] bs) {
        char[] digital = HEX_CHARS.toCharArray();
        StringBuffer sb = new StringBuffer("");
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    /**
     * 十六进制转换字符串
     *
     * @param hex
     *            String 十六进制
     * @return String 转换后的字符串
     */
    private static byte[] hex2byte(String hex) {
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = HEX_CHARS.indexOf(hex2char[2 * i]) * 16;
            temp += HEX_CHARS.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return bytes;
    }

}