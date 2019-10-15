package com.example.tapp.common.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean isMatched(String plainText, String hasedText) {
        return BCrypt.checkpw(plainText, hasedText);
    }
}