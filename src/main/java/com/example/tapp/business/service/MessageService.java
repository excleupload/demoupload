package com.example.tapp.business.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.data.exception.GeneralException;

public interface MessageService {

	MessageDialogDto create(MessageDialogDto dto) throws GeneralException;

	MessageDto addMessage(MessageDto messageDto) throws GeneralException;

	MessageDto messageSent(UUID messageId) throws GeneralException;

	List<MessageDialogDto> conversationList(UUID userId, HashMap<String, Object> filter);

	List<MessageDto> getPendingMessage(UUID userId);

	PageResponse<MessageDto> messageThread(UUID dialogId, UUID userId, Page page);

	void dialogClear(UUID dialogId, UUID occupantId);

	Date getLastClearTime(UUID dialogId, UUID occupantId);

}
