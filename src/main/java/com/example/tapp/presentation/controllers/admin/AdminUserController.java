package com.example.tapp.presentation.controllers.admin;

import static com.example.tapp.common.response.ResponseUtils.success;
import static com.example.tapp.common.response.ResponseUtils.error;
import static com.example.tapp.common.response.ResponseUtils.errorList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.AdminUserService;
import com.example.tapp.common.dto.AdminChangePassword;
import com.example.tapp.common.dto.AdminUserDto;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminUserDto dto) throws GeneralException {
        //
        return success.apply(adminUserService.login(dto.getEmail(), dto.getPassword()));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> details(@PathVariable("id") Integer id) throws RecordNotFoundException {
        //
        return success.apply(adminUserService.getDetailById(id));
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody AdminChangePassword changePassword, BindingResult result)
            throws RecordNotFoundException {
        //
        new ChangePassword().validate(changePassword, result);
        if (result.hasErrors())
            return errorList.apply(result.getFieldErrors());

        if (adminUserService.changePassword(changePassword)) {
            return success.apply("Password sucessfully changed.");
        }
        return error.apply("Your current Password does not matched.");
    }

    @PostMapping("/change/name")
    public ResponseEntity<?> changeName(@RequestBody AdminUserDto dto, BindingResult result)
            throws RecordNotFoundException {
        //
        new ChangeName().validate(dto, result);
        if (result.hasErrors())
            return errorList.apply(result.getFieldErrors());

        if (adminUserService.changeName(dto)) {
            return success.apply("Name successfully changed.");
        }
        return error.apply("Name not changed.");
    }
}
