package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.example.tapp.data.dao.MessageDialogDao;
import com.example.tapp.data.entities.MessageClear;
import com.example.tapp.data.entities.MessageDialog;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.repository.MessageClearRepository;
import com.example.tapp.data.repository.MessageDialogRepository;

@Repository
public class MessageDialogDaoImpl implements MessageDialogDao {

    @Autowired
    private MessageDialogRepository dialogRepo;

    @Autowired
    private MessageClearRepository clearRepo;

    @Override
    public MessageDialog save(MessageDialog dialog) {
        dialog.setCreatedOn(new Date());
        dialog.setModifiedOn(new Date());
        dialog.setDeleted(false);
        return dialogRepo.save(dialog);
    }

    @Override
    public MessageDialog update(MessageDialog dialog) {
        dialog.setModifiedOn(new Date());
        return dialogRepo.save(dialog);
    }

    @Override
    public MessageDialog getDialogByOccupants(List<UUID> occupantsId) {
        return dialogRepo.findByOccupantsIdIn(occupantsId).orElse(null);
    }

    @Override
    public MessageDialog getDialoagById(UUID dialogId) throws RecordNotFoundException {
        return dialogRepo.findById(dialogId).orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public List<MessageDialog> getMessageDialogList(List<UUID> ids, Sort sort) {
        return dialogRepo.findByIdIn(ids, dialogRepo.convertHelperSort(sort));
    }

    @Override
    public MessageClear dialogClearSave(MessageClear clear) {
        clear.setCreatedOn(new Date());
        clear.setModifiedOn(new Date());
        clear.setDeleted(false);
        return clearRepo.save(clear);
    }

    @Override
    public MessageClear dialogClearUpdate(MessageClear clear) {
        clear.setModifiedOn(new Date());
        return clearRepo.save(clear);
    }

    @Override
    public MessageClear getMessageClear(UUID dialogId, UUID occupantId) {
        return clearRepo.findByDialogIdAndOccupantId(dialogId, occupantId).orElse(null);
    }

    @Override
    public void deleteDialog(MessageDialog entity) {
        dialogRepo.delete(entity);
    }

}