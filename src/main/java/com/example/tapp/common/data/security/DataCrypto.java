package com.example.tapp.common.data.security;
public class DataCrypto {
    private static final String KEY = "0123456789abcdef";
       private static final String IV = "fedcba9876543210";
    

       public static String decrypt(String source) {
           return AES.decrypt(source, KEY, IV);
       }

       public static String encrypt(String source) {
           return AES.encrypt(source, KEY, IV);
       }

}