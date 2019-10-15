package com.example.tapp.data.dao.implementation;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.tapp.data.dao.ShareContentDao;
import com.example.tapp.data.entities.ShareContent;
import com.example.tapp.data.repository.ShareContentRepository;

@Repository
public class ShareContentDaoImpl implements ShareContentDao {

    @Autowired
    private ShareContentRepository contentRepo;

    @Override
    public ShareContent save(ShareContent content) {
        content.setDeleted(false);
        content.setCreatedOn(new Date());
        content.setModifiedOn(new Date());
        return contentRepo.save(content);
    }

    @Override
    public ShareContent update(ShareContent content) {
        content.setModifiedOn(new Date());
        return contentRepo.save(content);
    }

    @Override
    public ShareContent getById(Integer id) {
        return contentRepo.findById(id).orElse(null);
    }

    @Override
    public List<ShareContent> getList() {
        return contentRepo.findAll();
    }

}

