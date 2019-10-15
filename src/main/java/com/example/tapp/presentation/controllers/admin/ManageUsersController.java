package com.example.tapp.presentation.controllers.admin;


import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import static com.example.tapp.common.response.ResponseUtils.success;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.UserService;
import com.example.tapp.common.dto.UserDto;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.common.utils.KeyUtils;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.UserNotFoundException;

@RestController
@RequestMapping("/admin")
public class ManageUsersController {

    @Autowired
    private UserService userService;


    @GetMapping("/manageUser/userlist")
    public ResponseEntity<?> profile(HttpServletRequest request) throws GeneralException, UserNotFoundException {


        return success.apply(userService.getUserProfileList(setOptions(request)));
    }

    @GetMapping("/manageUser/getlistforProfile/{userId}")
    public ResponseEntity<?> profile(@PathVariable("userId") UUID userId) throws GeneralException {
        return success.apply(userService.getProfile(userId, null, null));
    }

    @PostMapping("/manageUser/profile")
    public ResponseEntity<?> profile(@RequestBody HashMap<String, Object> params) throws GeneralException {
        userService.profile(params);
        return success.apply("User profile successfully updated.");
    }

    @RequestMapping(value = "/manageUser/delete/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteManageUser(@PathVariable("id") UUID id) {
        userService.deleteManageUser(id);
        return success.apply("User successfully deleted.");
    }

    public HashMap<String, Object> setOptions(HttpServletRequest request) {
        HashMap<String, Object> options = new HashMap<>();
        HashMap<String, Object> page = new HashMap<>();
        // Page.setPageOptions(options, request);
        Sort.setSortOptions(options, request);
        HashMap<String, String> filters = new HashMap<>();
        if (request.getParameter(Filter.USER_NAME) != null && !request.getParameter(Filter.USER_NAME).isEmpty()) {
            filters.put(Filter.USER_NAME, request.getParameter(Filter.USER_NAME));
        }
        if (request.getParameter(UserDto.STATUS) != null && !request.getParameter(UserDto.STATUS).isEmpty()) {
            filters.put(UserDto.STATUS, request.getParameter(UserDto.STATUS));
        }
        if (request.getParameter(KeyUtils.SEARCH) != null && !request.getParameter(KeyUtils.SEARCH).isEmpty()) {
            filters.put(KeyUtils.SEARCH, request.getParameter(KeyUtils.SEARCH));
        }

        if (request.getParameter(KeyUtils.OFFSET) != null) {
            page.put(KeyUtils.OFFSET, new Integer(request.getParameter(KeyUtils.OFFSET)));
        }
        if (request.getParameter(KeyUtils.LIMIT) != null) {
            page.put(KeyUtils.LIMIT, new Integer(request.getParameter(KeyUtils.LIMIT)));
        }
        options.put(Filter.FILTER, filters);
        // options.put(KeyUtils.PAGE, page);
        return options;
    }
}