package com.example.tapp.data.dao;

import java.util.List;

import com.example.tapp.data.entities.ShareContent;

public interface ShareContentDao {

	ShareContent save(ShareContent content);

	ShareContent update(ShareContent content);

	ShareContent getById(Integer id);

	List<ShareContent> getList();

}
