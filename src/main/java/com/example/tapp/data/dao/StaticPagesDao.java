package com.example.tapp.data.dao;

import java.util.List;

import com.example.tapp.common.discriminator.PageType;
import com.example.tapp.data.entities.StaticPages;
import com.example.tapp.data.exception.RecordNotFoundException;

public interface StaticPagesDao {

	StaticPages save(StaticPages staticPages);

	StaticPages saveorExitCheck(StaticPages staticPages);

	StaticPages update(StaticPages staticPages);

	StaticPages getDetailsById(Long id) throws RecordNotFoundException;

	StaticPages getByPageType(PageType pageType);

	StaticPages getByPageTypeForApi(PageType pageType) throws RecordNotFoundException;

	void deletePage(Long id);

	List<StaticPages> getList();

}
