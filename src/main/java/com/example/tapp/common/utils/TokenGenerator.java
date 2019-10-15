package com.example.tapp.common.utils;

import java.util.UUID;

import com.example.tapp.common.data.security.DataCrypto;

public class TokenGenerator {

    public static final String SEPRATOR = "::";

    public static String createToken(UUID uuid, String email) {
        String str = new StringBuffer().append(uuid.toString()).append(SEPRATOR).append(email).append(SEPRATOR)
                .append(System.currentTimeMillis()).toString();
        return generator(str);
    }

    public static String createToken(Integer id, String email) {
        String str = new StringBuffer().append(id).append(SEPRATOR).append(email).append(SEPRATOR)
                .append(System.currentTimeMillis()).toString();
        return generator(str);
    }

    public static String[] extract(String token) {
        String str = AppUtils.decode(token);
        str = DataCrypto.decrypt(str);
        String[] data = str.split(SEPRATOR);
        return data;
    }

    private static String generator(String str) {
        return AppUtils.encode(DataCrypto.encrypt(str));
    }

    public static long getValidityTime(int day) {
        return (long) (86400000L * day);
    }
}
