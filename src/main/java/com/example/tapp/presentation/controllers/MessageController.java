package com.example.tapp.presentation.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tapp.business.service.MessageService;
import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.data.exception.GeneralException;
import static com.example.tapp.common.utils.Route.createDialog;
import static com.example.tapp.common.utils.Route.clearDialog;
import static com.example.tapp.common.response.ResponseUtils.success;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping(createDialog)
    public ResponseEntity<?> createDialog(@RequestBody MessageDialogDto dto) throws GeneralException {
        //
        return success.apply(messageService.create(dto));
    }

    @PostMapping(clearDialog)
    public ResponseEntity<?> clearDialog(@RequestParam("occupantId") UUID occupantId,
            @RequestParam("dialogId") UUID dialogId) {
        //
        messageService.dialogClear(dialogId, occupantId);
        return success.apply("Message dialog clear successfully.");
    }

}