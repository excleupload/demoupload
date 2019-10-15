package com.example.tapp.ws.validation;

import java.util.HashMap;

import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.list.helper.Page;

public class Validation {

    public static HashMap<String, String> message(MessageDto message) {
        HashMap<String, String> errors = new HashMap<>();
        if (message.getDialogId() == null) {
            errors.put(MessageDto.DIALOG_ID, "Dialog id is required.");
        }

        if (message.getMessage() == null || message.getMessage().isEmpty()) {
            errors.put(MessageDto.MESSAGE, "Message is required.");
        }

        if (message.getType() == null) {
            errors.put(MessageDto.TYPE, "Message type is required.");
        }
        return errors;
    }

    public static HashMap<String, String> messageList(HashMap<String, Object> params) {
        HashMap<String, String> errors = new HashMap<>();
        if (params.get(MessageDialogDto.DIALOG_ID) == null) {
            errors.put(MessageDialogDto.DIALOG_ID, "Dialog id is required.");
        }
        if (params.get(Page.LIMIT) == null) {
            errors.put(Page.LIMIT, "Limit is required.");
        }
        if (params.get(Page.OFFSET) == null) {
            errors.put(Page.OFFSET, "Offset is required.");
        }
        return errors;
    }

}

