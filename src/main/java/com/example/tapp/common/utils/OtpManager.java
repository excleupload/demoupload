package com.example.tapp.common.utils;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.example.tapp.common.cache.Cache;
import com.example.tapp.common.cache.CacheException;

@Component
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:utils.yml")
public class OtpManager {

    private static final Logger log = LoggerFactory.getLogger(OtpManager.class);

    @Value("${tapp.application.user.otp.length}")
    private int length;

    @Value("${tapp.application.user.otp.validity}")
    private int validity;

    private static final String NUMBERS = "012345678";

    private Cache<String, String> storage;

    @PostConstruct
    public void init() {
        storage = new Cache<>(validity);
    }

    public synchronized String generate() {
        Random random = new Random();
        char[] otp = new char[length];
        for (int i = 0; i < length; i++) {
            otp[i] = NUMBERS.charAt(random.nextInt(NUMBERS.length()));
        }
        return new String(otp);
    }

    public void storeOtp(String key, String otp) {
        storage.put(key, otp);
    }

    public boolean verifyOtp(String key, String otp) {
        try {
            String chk = storage.get(key);
            return chk.equals(otp);
        } catch (CacheException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    public void removeOtp(String key) {
        storage.remove(key);
    }
}
