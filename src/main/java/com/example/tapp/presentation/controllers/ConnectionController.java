package com.example.tapp.presentation.controllers;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.ConnectionRequestService;
import com.example.tapp.business.service.UserConnectionService;
import com.example.tapp.common.discriminator.ConnectionRequestStatus;
import com.example.tapp.common.dto.ConnectionRequestDto;
import com.example.tapp.common.dto.RequestOperation;
import com.example.tapp.common.list.helper.Filter;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;

import static com.example.tapp.common.utils.Route.connectionRequest;
import static com.example.tapp.common.utils.Route.connectionRequestOperation;
import static com.example.tapp.common.utils.Route.connectionRequestList;
import static com.example.tapp.common.utils.Route.connectionList;
import static com.example.tapp.common.utils.Route.removeConnection;
import static com.example.tapp.common.utils.Route.connectionUserNameList;
import static com.example.tapp.common.utils.Route.unblockConnection;
import static com.example.tapp.common.utils.Route.blockConnection;

import static com.example.tapp.common.response.ResponseUtils.success;
@RestController
public class ConnectionController {

    @Autowired
    private ConnectionRequestService conReqService;

    @Autowired
    private UserConnectionService userConnService;

    @PostMapping(connectionRequest)
    public ResponseEntity<?> connectionRequest(@RequestBody ConnectionRequestDto dto, BindingResult result)
            throws GeneralException {
        //
//        new ConnectionReq().validate(dto, result);
//        if (result.hasErrors())
//            return errorList.apply(result.getFieldErrors());

        return success.apply(conReqService.add(dto));
    }

    @PostMapping(connectionRequestOperation)
    public ResponseEntity<?> operation(@RequestBody RequestOperation operation, BindingResult result)
            throws GeneralException {
        //
//        new ConnectionRequestOperation().validate(operation, result);
//        if (result.hasErrors())
//            return errorList.apply(result.getFieldErrors());

        conReqService.operation(operation);
        return success.apply("Connection Request updated.");
    }

    @GetMapping(connectionRequestList)
    public ResponseEntity<?> connReqList(@RequestParam("userId") UUID userId, HttpServletRequest request) {
        //
        return success.apply(conReqService.getList(userId, setOptions(request)));
    }

    @GetMapping(connectionList)
    public ResponseEntity<?> connectionList(@RequestParam("userId") UUID userId, HttpServletRequest request) {
        //
        return success.apply(userConnService.getListByUserId(userId, setOptions(request)));
    }

    @GetMapping(connectionUserNameList)
    public ResponseEntity<?> connectedUserNameList(@RequestParam("userId") UUID userId) {
        //
        return success.apply(userConnService.getListUserNameByUserId(userId));
    }

    @PostMapping(removeConnection)
    public ResponseEntity<?> removeConnection(@RequestParam("connId") UUID connId) throws RecordNotFoundException {
        //
        userConnService.removeConnection(connId);
        return success.apply("Connection successfully removed.");
    }
    @PostMapping(blockConnection)
    public ResponseEntity<?> block(@RequestParam("connId") UUID connectionId) throws RecordNotFoundException {
        //
        userConnService.changeStatus(connectionId, ConnectionRequestStatus.BLOCKED);
        return success.apply("Connection block successfully.");
    }

    @PostMapping(unblockConnection)
    public ResponseEntity<?> unblock(@RequestParam("connId") UUID connectionId) throws RecordNotFoundException {
        //
        userConnService.changeStatus(connectionId, ConnectionRequestStatus.ACTIVE);
        return success.apply("Connection unblock successfully.");
    }

    public HashMap<String, Object> setOptions(HttpServletRequest request) {
        HashMap<String, Object> options = new HashMap<>();
        Page.setPageOptions(options, request);
        Sort.setSortOptions(options, request);

        HashMap<String, String> filters = new HashMap<>();
        if (request.getParameter(Filter.USER_NAME) != null && !request.getParameter(Filter.USER_NAME).isEmpty()) {
            filters.put(Filter.USER_NAME, request.getParameter(Filter.USER_NAME));
        }

        options.put(Filter.FILTER, filters);
        return options;
    }

}