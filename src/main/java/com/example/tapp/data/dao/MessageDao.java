package com.example.tapp.data.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.tapp.common.list.helper.Page;
import com.example.tapp.data.entities.Message;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface MessageDao {

	Message save(Message entity);

	Message update(Message entity);

	Message getMessageById(UUID messageId) throws RecordNotFoundException;

	List<Message> pendingMessage(UUID receiverId);

	Object[] list(UUID dialogId, Date clearOn, UUID userId, Page page);

	Long listCount(UUID dialogId, Date clearOn, UUID userId);

	void deleteMessage(UUID dialogId, Date clearDate);

	Message getLastMessage(UUID userId);

}
