package com.example.tapp.presentation.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.TutorialService;
import com.example.tapp.common.dto.TutorialPagesDto;

import static com.example.tapp.common.utils.Route.tutorial;

import static com.example.tapp.common.response.ResponseUtils.success;
@RestController
public class AppTutorialController {

    @Autowired
    private TutorialService tutorialService;

    @RequestMapping(tutorial)
    public ResponseEntity<?> tutorial() {
        //
        List<TutorialPagesDto> list = tutorialService.getTutorial();
        if (!list.isEmpty()) {
            return success.apply(list.get(0));
        }
        return success.apply(new HashMap<>());
    }

}
