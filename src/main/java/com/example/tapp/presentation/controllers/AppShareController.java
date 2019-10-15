package com.example.tapp.presentation.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.ShareContentService;
import com.example.tapp.common.dto.ShareContentDto;

import static com.example.tapp.common.utils.Route.shareContent;

import static com.example.tapp.common.response.ResponseUtils.success;

@RestController
public class AppShareController {

    @Autowired
    private ShareContentService contentService;

    @GetMapping(shareContent)
    public ResponseEntity<?> data() {
        //

        List<ShareContentDto> list = contentService.getShareContent();
        if (!list.isEmpty()) {
            return success.apply(list.get(0));
        }
        return success.apply(new HashMap<>());
    }

}