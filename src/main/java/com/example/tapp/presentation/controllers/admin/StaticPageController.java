package com.example.tapp.presentation.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.StaticPagesService;
import com.example.tapp.common.dto.StaticPagesDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.presentation.validation.StaticPagesValidation;

import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.response.ResponseUtils.errorList;

@RestController
@RequestMapping("/admin")
public class StaticPageController {

    @Autowired
    private StaticPagesService staticPagesService;

    @PostMapping("/staticPagesService/updateContextType")
    public ResponseEntity<?> updateContextType(@RequestBody StaticPagesDto staticpagesDto, BindingResult result)
            throws GeneralException, RecordNotFoundException {
        new StaticPagesValidation().validate(staticpagesDto, result);
        if (result.hasErrors()) {
            return errorList.apply(result.getFieldErrors());
        }
        return success.apply(staticPagesService.save(staticpagesDto));
    }

    @PostMapping("/staticPagesService/saveContextType")
    public ResponseEntity<?> saveContextType(@RequestBody StaticPagesDto staticpagesDto, BindingResult result)
            throws GeneralException, RecordNotFoundException {
        new StaticPagesValidation().validate(staticpagesDto, result);
        if (result.hasErrors()) {
            return errorList.apply(result.getFieldErrors());
        }
        return success.apply(staticPagesService.saveorExitCheck(staticpagesDto));
    }

    @GetMapping("/staticPagesService/listOfType")
    public ResponseEntity<?> getStaticTypeData() {
        return success.apply(staticPagesService.getList());
    }

    @GetMapping("/staticPagesService/details/{id}")
    public ResponseEntity<?> details(@PathVariable("id") Long id) throws RecordNotFoundException {
        return success.apply(staticPagesService.getDetailById(id));
    }

    @RequestMapping(value = "/staticPagesService/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteStaticType(@PathVariable("id") Long id) {
        staticPagesService.deletePage(id);
        return success.apply("Static Page successfully deleted.");
    }

}