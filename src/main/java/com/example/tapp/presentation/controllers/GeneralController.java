package com.example.tapp.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.example.tapp.common.response.ResponseUtils.success;

@RestController
public class GeneralController {

    @RequestMapping("/ping")
    public ResponseEntity<?> ping() {
        return success.apply("1");
    }

    @RequestMapping("/")
    public ResponseEntity<?> welcome() {
        return success.apply("Welcome! from Tapp application server.");
    }
}