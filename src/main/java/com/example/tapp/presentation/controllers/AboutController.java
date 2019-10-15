package com.example.tapp.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.StaticPagesService;
import com.example.tapp.data.exception.RecordNotFoundException;
import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.utils.Route.aboutListofType;
import static com.example.tapp.common.utils.Route.aboutdetails;

@RestController

public class AboutController {

    @Autowired
    private StaticPagesService staticPagesService;

    @GetMapping(aboutListofType)
    public ResponseEntity<?> getStaticTypeData() {
        return success.apply(staticPagesService.getList());
    }

    @GetMapping(aboutdetails)
    public ResponseEntity<?> details(@PathVariable("type") String type) throws RecordNotFoundException {
        return success.apply(staticPagesService.getPageTypeForApi(type));
    }

}
