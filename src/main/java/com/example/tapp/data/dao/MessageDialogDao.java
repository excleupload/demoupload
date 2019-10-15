package com.example.tapp.data.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;

import com.example.tapp.data.entities.MessageClear;
import com.example.tapp.data.entities.MessageDialog;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface MessageDialogDao {

	MessageDialog save(MessageDialog dialog);

	MessageDialog update(MessageDialog dialog);

	MessageDialog getDialogByOccupants(List<UUID> occupantsId);

	MessageDialog getDialoagById(UUID dialogId) throws RecordNotFoundException;

	//List<MessageDialog> getMessageDialogList(List<UUID> ids, com.example.tapp.common.list.helper.Sort sort);

	MessageClear dialogClearSave(MessageClear clear);

	MessageClear dialogClearUpdate(MessageClear clear);

	MessageClear getMessageClear(UUID dialogId, UUID occupantId);

	void deleteDialog(MessageDialog entity);

	List<MessageDialog> getMessageDialogList(List<UUID> ids, Sort sort);

}
