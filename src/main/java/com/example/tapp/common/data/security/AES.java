package com.example.tapp.common.data.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.LoggerFactory;

import com.example.tapp.common.utils.AppUtils;

public class AES {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AES.class);
    private static final String DATA_ALGO = "AES/CBC/PKCS5Padding";

    public static String encrypt(String source, String _key, String _iv) {
        byte[] row;
        String encryptedString = null;
        SecretKeySpec skeySpec;
        byte[] encrtypted = source.getBytes();
        Cipher cipher;
        try {
            row = _key.getBytes();
            IvParameterSpec iv = new IvParameterSpec(_iv.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(row, "AES");
            cipher = Cipher.getInstance(DATA_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            encryptedString = AppUtils.byteToHex(cipher.doFinal(encrtypted));
        } catch (Exception ex) {
            log.info("AES::encrypt : Error in encryption.");
            log.info("AES::decrypt  Message : " + ex.getMessage());
        }
        return encryptedString;
    }

    public static String decrypt(String source, String _key, String _iv) {
        Cipher cipher;
        String dectyptedString = null;
        byte[] encryptedText = null;
        byte[] row;
        SecretKeySpec skeySpec;
        try {
            row = _key.getBytes();
            encryptedText = AppUtils.hexToByte(source);
            IvParameterSpec iv = new IvParameterSpec(_iv.getBytes("UTF-8"));
            skeySpec = new SecretKeySpec(row, "AES");
            cipher = Cipher.getInstance(DATA_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            dectyptedString = new String(cipher.doFinal(encryptedText));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("AES::decrypt : Error in encryption.");
            log.info("AES::decrypt  Message : " + ex.getMessage());
        }
        return dectyptedString;
    }

}

