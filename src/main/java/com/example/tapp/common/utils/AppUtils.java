package com.example.tapp.common.utils;

import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.json.JsonParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AppUtils {

    private static final Pattern EMAIL_REGEX = Pattern.compile("^(.+)@(.+)$");
    private static final Pattern MOBILE_REGEX = Pattern.compile("(0/91)?[7-9][0-9]{9}");

    public static String jsonStringify(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <S> S jsonParse(String jsonStr, Class<S> domainClass)
            throws JsonParseException, JsonMappingException, IOException {
        return new ObjectMapper().readValue(jsonStr.getBytes(), domainClass);
    }

    public static boolean isEmailValid(String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.find();
    }


    public static boolean isMobileValid(String mobile) {
        Matcher matcher = MOBILE_REGEX.matcher(mobile);
        return matcher.find();
    }

    public static String byteToHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02X", b));
        }
        return buffer.toString();
    }

    public static byte[] hexToByte(String source) {
        int len = source.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(source.charAt(i), 16) << 4)
                    + Character.digit(source.charAt(i + 1), 16));
        }
        return data;
    }

    public static String encode(String source) {
        return Base64.getEncoder().encodeToString(source.getBytes());
    }

    public static String decode(String decoded) {
        return new String(Base64.getDecoder().decode(decoded));
       
    }

    public static int getAge(long dob) {
        long base = 24 * 60 * 60 * 1000;
        return (int) (((System.currentTimeMillis() - dob) / base) / 365);
    }

    public static int getAge(int dob) {
        long base = 24 * 60 * 60 * 1000;
        return (int) (((System.currentTimeMillis() - dob) / base) / 365);
    }
}