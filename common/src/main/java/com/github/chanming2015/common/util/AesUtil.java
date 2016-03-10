package com.github.chanming2015.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 
 * Create Date:2016年3月10日 下午9:45:57
 * @author XuMaoSen
 * Version:1.0.0
 */
public class AesUtil
{
    private static final Logger log = LoggerFactory.getLogger(AesUtil.class);

    private static final String ALGORITHM = "AES";

    private static final String CHARSET = "utf-8";

    /**
     * Description: AES加密
     * Create Date:2016年3月10日 下午10:31:28
     * @author XuMaoSen
     * @param keyString 加密密钥
     * @param content 待加密内容
     * @return 16进制字符串(密文)
     */
    public static String encryptAesString(String keyString, String content)
    {
        return parseByte2HexStr(encryptAes(keyString, content));
    }

    /**
     * Description: AES解密
     * Create Date:2016年3月10日 下午9:54:43
     * @author XuMaoSen
     * @param keyString 密钥
     * @param content 待解密内容(16进制)
     * @return 明文字符串
     */
    public static String decryptAesString(String keyString, String content)
    {
        try
        {
            return new String(decryptAes(keyString, content), CHARSET);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("decryptAesString UnsupportedEncodingException", e);
        }
        return null;
    }

    /**
     * Description: AES加密
     * Create Date:2016年3月10日 下午9:54:43
     * @author XuMaoSen
     * @param keyString 密钥
     * @param content 待加密内容
     */
    public static byte[] encryptAes(String keyString, String content)
    {
        try
        {
            byte[] byteKey = keyString.getBytes(CHARSET);
            byte[] byteContent = content.getBytes(CHARSET);

            return doAes(byteKey, byteContent, true);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("encryptAes UnsupportedEncodingException", e);
        }
        return new byte[0];
    }

    /**
     * Description: AES解密
     * Create Date:2016年3月10日 下午9:54:43
     * @author XuMaoSen
     * @param keyString 密钥
     * @param content 待解密内容(16进制)
     */
    public static byte[] decryptAes(String keyString, String content)
    {
        try
        {
            byte[] byteKey = keyString.getBytes(CHARSET);
            byte[] byteContent = parseHexStr2Byte(content);

            return doAes(byteKey, byteContent, false);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("encryptAes UnsupportedEncodingException", e);
        }
        return new byte[0];
    }

    /**
     * Description: 执行AES加密或解密算法
     * Create Date:2016年3月10日 下午9:37:52
     * @author XuMaoSen
     * @param byteKey 密钥
     * @param byteContent 待加密或解密内容
     * @param flag 加密或解密标识，true=加密，false=解密
     */
    private static byte[] doAes(byte[] byteKey, byte[] byteContent, boolean flag)
    {
        try
        {
            // 1.生成加密密钥
            KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
            kgen.init(128, new SecureRandom(byteKey));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormart = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormart, ALGORITHM);

            // 2.创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            // 3.初始化
            if (flag)
            {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            else
            {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }

            // 4.加密（解密）
            byte[] result = cipher.doFinal(byteContent);
            return result;
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error("doAes NoSuchAlgorithmException", e);
        }
        catch (NoSuchPaddingException e)
        {
            log.error("doAes NoSuchPaddingException", e);
        }
        catch (InvalidKeyException e)
        {
            log.error("doAes InvalidKeyException", e);
        }
        catch (IllegalBlockSizeException e)
        {
            log.error("doAes IllegalBlockSizeException", e);
        }
        catch (BadPaddingException e)
        {
            log.error("doAes BadPaddingException", e);
        }

        return new byte[0];
    }

    /**
     * Description: 将二进制转换成16进制 
     * Create Date:2016年3月10日 下午10:06:23
     * @author XuMaoSen
     */
    public static String parseByte2HexStr(byte[] buf)
    {
        if (buf == null || buf.length < 1)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (byte element : buf)
        {
            String hex = Integer.toHexString(element & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase(Locale.US));
        }

        return sb.toString();
    }

    /**
     * Description: 将16进制转换成二进制 
     * Create Date:2016年3月10日 下午10:22:44
     * @author XuMaoSen
     */
    public static byte[] parseHexStr2Byte(String hexStr)
    {
        if (hexStr == null || hexStr.length() < 1)
        {
            return new byte[0];
        }

        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++)
        {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;

    }

    private static final String WORD = "0123456789abcdefjhijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ";

    /**
     * Description: 随机生成字符串
     * Create Date:2016年3月10日 下午10:42:43
     * @author XuMaoSen
     */
    public static String randomString(int length)
    {
        StringBuilder sb = new StringBuilder();
        SecureRandom r = new SecureRandom();
        int range = WORD.length();
        for (int i = 0; i < length; i++)
        {
            sb.append(WORD.charAt(r.nextInt(range)));
        }

        return sb.toString();
    }
}
